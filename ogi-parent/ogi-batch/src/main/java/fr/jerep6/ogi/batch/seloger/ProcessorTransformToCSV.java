package fr.jerep6.ogi.batch.seloger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.jerep6.ogi.service.external.ServicePartner;

public class ProcessorTransformToCSV implements ItemProcessor<ExtractSeLoger, RealPropertyCSV> {
	private static Logger						LOGGER		= LoggerFactory.getLogger(ProcessorTransformToCSV.class);

	private static final String					MEUBLEE		= "Location meublée";

	private static final String					MODE_SALE	= "SALE";
	private static final String					MODE_RENT	= "RENT";
	private static final SimpleDateFormat		spFormater	= new SimpleDateFormat("dd/MM/yyyy");

	private ServicePartner						serviceSeLoger;

	/** Matching between OGI category and seloger category */
	private static Map<EnumCategory, String>	mapTypeBien	= new HashMap<>();

	static {
		mapTypeBien.put(EnumCategory.APARTMENT, "appartement,");
		mapTypeBien.put(EnumCategory.HOUSE, "maison");
		mapTypeBien.put(EnumCategory.PLOT, "terrain");
	}
	private String								estateCode;

	private void populateCommon(RealProperty item, RealPropertyCSV r) {
		r.setBienTechid(item.getTechid().toString());
		r.setBienReference(item.getReference());

		Address addr = item.getAddress();
		r.setAdresseCP(addr.getPostalCode());
		r.setAdresseVille(addr.getCity());

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
		}
	}

	private void populatePhotos(RealProperty item, RealPropertyCSV r) {
		List<Document> photos = item.getPhotos();

		if (photos.size() >= 1) {
			r.setPhoto1(photos.get(0).getPath());
		}
		if (photos.size() >= 2) {
			r.setPhoto2(photos.get(1).getPath());
		}
		if (photos.size() >= 3) {
			r.setPhoto3(photos.get(2).getPath());
		}
		if (photos.size() >= 4) {
			r.setPhoto4(photos.get(3).getPath());
		}
		if (photos.size() >= 5) {
			r.setPhoto5(photos.get(4).getPath());
		}
		if (photos.size() >= 6) {
			r.setPhoto6(photos.get(5).getPath());
		}

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

			if (rent.getFurnished()) {
				r.setNatureBail(MEUBLEE);
			}

			if (rent.getFreeDate() != null) {
				r.setDateDisponibilite(spFormater.format(rent.getFreeDate().getTime()));
			}
		}

	}

	private void populateSale(RealProperty item, RealPropertyCSV r) {
		if (item.getSale() != null) {
			r.setPrix(item.getSale().getPriceFinal().toString());
			r.setMandatNumero(item.getSale().getMandateReference());
		}
	}

	@Override
	public RealPropertyCSV process(ExtractSeLoger extract) throws Exception {
		RealProperty item = extract.getProperty();

		try {
			serviceSeLoger.validate(item);

			RealPropertyCSV r = new RealPropertyCSV(item);
			r.setAgenceId(estateCode);

			// Choose mode according to extract
			switch (extract.getMode()) {
				case MODE_SALE:
					r.setTypeAnnonce("vente");
					populateSale(item, r);
					break;
				case MODE_RENT:
					r.setTypeAnnonce("location");

					populateRent(item, r);
					break;

				default:
					break;
			}

			r.setTypeBien(mapTypeBien.get(item.getCategory().getCode()));

			populateCommon(item, r);
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

	public void setServiceSeLoger(ServicePartner serviceSeLoger) {
		this.serviceSeLoger = serviceSeLoger;
	}

	private String toBoolean(Boolean b) {
		return b == null ? "" : b ? "OUI" : "NON";
	}

}