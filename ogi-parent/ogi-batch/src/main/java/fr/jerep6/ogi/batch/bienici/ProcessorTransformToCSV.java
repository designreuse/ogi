package fr.jerep6.ogi.batch.bienici;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.enumeration.EnumOrientation;
import fr.jerep6.ogi.framework.utils.ObjectUtils;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.utils.Functions;

public class ProcessorTransformToCSV implements ItemProcessor<ExtractBienIci, RealPropertyCSV> {
	private static Logger						LOGGER		= LoggerFactory.getLogger(ProcessorTransformToCSV.class);

	private static final String					MEUBLEE		= "Location meublée";

	private static final String					MODE_SALE	= "SALE";
	private static final String					MODE_RENT	= "RENT";

	private ServicePartner						serviceBienIci;

	/** Matching between OGI category and seloger category */
	private static Map<EnumCategory, String>	mapTypeBien	= new HashMap<>();

	static {
		mapTypeBien.put(EnumCategory.APARTMENT, "Appartement");
		mapTypeBien.put(EnumCategory.HOUSE, "Maison");
		mapTypeBien.put(EnumCategory.PLOT, "Terrain");
	}

	private String								estateCode;
	private String								photoDirName;

	private void populateCommon(RealProperty item, RealPropertyCSV r, Function<String, String> fReference) {
		r.setBienTechid(item.getTechid().toString());
		if (fReference != null) {
			r.setBienReference(fReference.apply(item.getReference()));
		}

		Address addr = item.getAddress();
		r.setAdresseCP(addr.getPostalCode());
		r.setAdresseVille(addr.getCity());
		r.setAdresseRue(addr.getStreet());

		r.setSurfaceTerrain(ObjectUtils.toString(item.getLandArea()));

		r.setLibelle(item.getType().getLabel());
		r.setDescriptif(item.getDescription(EnumDescriptionType.WEBSITE_OTHER).getLabel().replace("\n", "<br />"));

		// Liveable will erase this. Set here to plot
		r.setNbrePiece("0");
	}

	private void populateLiveable(RealProperty item, RealPropertyCSV r) {
		if (item instanceof RealPropertyLivable) {
			RealPropertyLivable liv = (RealPropertyLivable) item;
			if (liv.getBuildDate() != null) {
				r.setAnneeConstruction(String.valueOf(liv.getBuildDate().get(Calendar.YEAR)));
			}

			r.setEtage(ObjectUtils.toString(liv.getFloorLevel()));
			r.setEtageNbre(ObjectUtils.toString(liv.getNbFloor()));

			r.setNbreChambre(ObjectUtils.toString(liv.getNbBedRoom()));
			r.setNbrePiece(ObjectUtils.toString(liv.getNbRoom()));
			r.setNbreSalleDeBain(ObjectUtils.toString(liv.getNbBathRoom()));
			r.setNbreSalleEau(ObjectUtils.toString(liv.getNbShowerRoom()));

			r.setSurface(ObjectUtils.toString(liv.getArea()));

			EnumOrientation o = liv.getOrientation();
			if (o != null) {
				r.setOrientationEst(toBoolean(EnumOrientation.EAST.equals(o) || EnumOrientation.NORTH_EAST.equals(o)
						|| EnumOrientation.SOUTH_EAST.equals(o)));
				r.setOrientationNord(toBoolean(EnumOrientation.NORTH.equals(o) || EnumOrientation.NORTH_WEST.equals(o)
						|| EnumOrientation.NORTH_EAST.equals(o)));
				r.setOrientationOuest(toBoolean(EnumOrientation.WEST.equals(o) || EnumOrientation.NORTH_WEST.equals(o)
						|| EnumOrientation.NORTH_WEST.equals(o)));
				r.setOrientationSud(toBoolean(EnumOrientation.SOUTH.equals(o) || EnumOrientation.SOUTH_EAST.equals(o)
						|| EnumOrientation.SOUTH_EAST.equals(o)));
			}

			// Travaux
			if (liv.getState() != null) {
				r.setTravauxAPrevoir(toBoolean(liv.getState().getOrder() <= 2));
			}

			// DPE
			DPE dpe = liv.getDpe();
			if (dpe != null) {
				LOGGER.debug("No DPE for {}", liv.getReference());
				r.setDpeGesClassification(Strings.nullToEmpty(dpe.getClassificationGes()));
				r.setDpeKwhClassification(Strings.nullToEmpty(dpe.getClassificationKWh()));
				r.setDpeGes(ObjectUtils.toString(dpe.getGes()));
				r.setDpeKwh(ObjectUtils.toString(dpe.getKwh()));
			}

			// Copropriété
			if (liv.getCoOwnership() != null && liv.getCoOwnership()) {
				r.setCopropriete(toBoolean(liv.getCoOwnership()));
				r.setCoproprieteChargeAnnuelle(ObjectUtils.toString(liv.getCoOwnershipCharges()));
				r.setCoproprieteNbreLots(ObjectUtils.toString(liv.getCoOwnershipLotNumber()));
				r.setCoproprieteSyndicatProcedure(toBoolean(liv.getCoOwnershipSyndicateProceedings()));
			}
		}
	}

	/**
	 * Populate only six photos max
	 *
	 * @param item
	 * @param r
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private void populatePhotos(RealProperty item, RealPropertyCSV r) throws NoSuchMethodException,
	IllegalAccessException, InvocationTargetException {
		// Compute max 6 photos
		Integer nbPhotosToCompute = 6;

		List<Document> photos = item.getPhotos();
		List<Document> photosToCopy = new ArrayList<>(nbPhotosToCompute);

		String reference = item.getReference();
		for (int i = 0; i < photos.size() && i < nbPhotosToCompute; i++) {
			// Utilisation d'un path pour supprimer le 1er / si photoDirName est vide
			String p = Paths.get(photoDirName).resolve(reference + "_" + photos.get(i).getAbsolutePath().getFileName())
					.toString();
			MethodUtils.invokeExactMethod(r, "setPhoto" + (i + 1), p);
			photosToCopy.add(photos.get(i));
		}
		r.setPhotos(photosToCopy);
	}

	private void populatePlot(RealProperty item, RealPropertyCSV r) {
		if (item instanceof RealPropertyPlot) {
			RealPropertyPlot plt = (RealPropertyPlot) item;
			r.setTerrainConstructible(toBoolean(plt.getBuilding()));
		}
	}

	private void populateRent(RealProperty item, RealPropertyCSV r) {
		Rent rent = item.getRent();
		if (rent != null) {
			r.setPrix(rent.getPrice().toString());
			r.setLoyerCC(toBoolean(rent.getServiceChargeIncluded()));
			r.setHonoraires(ObjectUtils.toString(rent.getCommission()));
			r.setCharges(ObjectUtils.toString(rent.getServiceCharge()));
			r.setDepotDeGarantie(ObjectUtils.toString(rent.getDeposit()));
			r.setMandatExclusif(toBoolean(rent.getExclusive()));
			r.setMeuble(toBoolean(rent.getFurnished()));

			if (rent.getFurnished()) {
				r.setNatureBail(MEUBLEE);
			}

			if (rent.getFreeDate() != null) {
				SimpleDateFormat spFormater = new SimpleDateFormat("dd/MM/yyyy");
				r.setDateDisponibilite(spFormater.format(rent.getFreeDate().getTime()));
			}
		}

	}

	private void populateSale(RealProperty item, RealPropertyCSV r) {
		Sale sale = item.getSale();
		if (sale != null) {
			r.setPrix(sale.getPriceFinal().toString());
			r.setMandatNumero(sale.getMandateReference());
			r.setMandatExclusif(toBoolean(sale.getExclusive()));
		}
	}

	@Override
	public RealPropertyCSV process(ExtractBienIci extract) throws Exception {
		RealProperty item = extract.getProperty();

		try {
			serviceBienIci.validate(item);

			RealPropertyCSV r = new RealPropertyCSV(item);
			r.setAgenceId(estateCode);

			Function<String, String> fReference = null;
			// Choose mode according to extract
			switch (extract.getMode()) {
				case MODE_SALE:
					fReference = Functions::returnReference;
					r.setTypeAnnonce("vente");
					populateSale(item, r);
					break;
				case MODE_RENT:
					fReference = Functions::returnReference;
					r.setTypeAnnonce("location");
					populateRent(item, r);
					break;

				default:
					break;
			}

			r.setTypeBien(mapTypeBien.get(item.getCategory().getCode()));

			populateCommon(item, r, fReference);
			populatePhotos(item, r);
			populateLiveable(item, r);
			populatePlot(item, r);

			return r;
		} catch (Exception e) {
			LOGGER.error("Error processing item " + item.getReference(), e);
			throw e;
		}
	}

	public void setEstateCode(String estateCode) {
		this.estateCode = estateCode;
	}

	public void setPhotoDirName(String photoDirName) {
		this.photoDirName = photoDirName;
	}

	public void setServiceBienIci(ServicePartner serviceBienIci) {
		this.serviceBienIci = serviceBienIci;
	}

	private String toBoolean(Boolean b) {
		return b == null ? "" : b ? "OUI" : "NON";
	}

}