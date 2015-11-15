package fr.jerep6.ogi.batch.annoncesjaunes;

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
import fr.jerep6.ogi.persistance.bo.RealPropertyBuilt;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.utils.Functions;

public class ProcessorTransformToCSV implements ItemProcessor<ExtractSeLoger, RealPropertyCSV> {
	private static Logger						LOGGER		= LoggerFactory.getLogger(ProcessorTransformToCSV.class);

	private static final String					MEUBLEE		= "Location meublée";

	private static final String					MODE_SALE	= "SALE";
	private static final String					MODE_RENT	= "RENT";

	private ServicePartner						serviceSeLoger;

	/** Matching between OGI categories and annonces jaune categories */
	private static Map<EnumCategory, String>	mapTypeBien	= new HashMap<>();
	static {
		mapTypeBien.put(EnumCategory.APARTMENT, "Appartement");
		mapTypeBien.put(EnumCategory.HOUSE, "Maison");
		mapTypeBien.put(EnumCategory.PLOT, "Terrain");
	}
	
	/** Matching between OGI states and annonces jaunes states */
	private static Map<String, String>	mapStates	= new HashMap<>();
	static {
		mapStates.put("1", "Travaux à prévoir");
		mapStates.put("2", "Travaux à prévoir");
		mapStates.put("3", "Bon état");
		mapStates.put("4", "Neuf");
	}
	

	private String								estateCode;
	private String								photoDirName;

	private String computeHeating(String heating) {
		if(Strings.isNullOrEmpty(heating)) {
			return "";
		}
		
		String normalizedHeating = heating.toLowerCase().trim();
		if(normalizedHeating.startsWith("au sol")) {
			return "Sol";
		}
		else if(normalizedHeating.startsWith("gaz")) {
			return "Gaz";
		}
		else if(normalizedHeating.startsWith("fioul")) {
			return "Fuel";
		}
		else if(normalizedHeating.startsWith("electrique") || normalizedHeating.startsWith("électrique")) {
			return "Electrique";
		}
		else {
			return "Autres";
		}
	}
	
	private void populateCommon(RealProperty item, RealPropertyCSV r) {
		r.setBienReference(item.getReference());

		Address addr = item.getAddress();
		r.setAdresseCP(addr.getPostalCode());
		r.setAdresseVille(addr.getCity());

		r.setSurfaceTerrain(ObjectUtils.toString(item.getLandArea()));

		r.setTitre(item.getType().getLabel() + " à " + addr.getCity());
		r.setDescriptif(item.getDescription(EnumDescriptionType.WEBSITE_OTHER).getLabel());

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
			r.setNbreWC(ObjectUtils.toString(liv.getNbWC()));

			r.setSurfaceHabitable(ObjectUtils.toString(liv.getArea()));

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
				if(liv.getCoOwnership()) {
					r.setCopropriete(toBoolean(liv.getCoOwnership()));
				}
				
				r.setCoproprieteChargeAnnuelle(ObjectUtils.toString(liv.getCoOwnershipCharges()));
				r.setCoproprieteNbreLots(ObjectUtils.toString(liv.getCoOwnershipLotNumber()));
				r.setCoproprieteSyndicatProcedure(toBoolean(liv.getCoOwnershipSyndicateProceedings()));
			}
			
			// Etat
			if(liv.getState() != null && mapStates.get(liv.getState().getOrder()) != null) {
				r.setEtatGeneral(mapStates.get(liv.getState().getOrder()));
			}
			
			r.setChauffage(computeHeating(liv.getHeating()));
		}
	}

	/**
	 * Populate only six photos max
	 *
	 * @param item
	 * @param r
	 */
	private void populatePhotos(RealProperty item, RealPropertyCSV r) {
		List<Document> photos = item.getPhotos();
		if (photos.size() >= 1) {
			r.setPhoto1(photoDirName + "/" + Functions.addReferenceToPhotoName(photos.get(0).getPath(), item.getReference()));
		}
		if (photos.size() >= 2) {
			r.setPhoto2(photoDirName + "/" + Functions.addReferenceToPhotoName(photos.get(1).getPath(), item.getReference()));
		}
		if (photos.size() >= 3) {
			r.setPhoto3(photoDirName + "/" + Functions.addReferenceToPhotoName(photos.get(2).getPath(), item.getReference()));
		}
		if (photos.size() >= 4) {
			r.setPhoto4(photoDirName + "/" + Functions.addReferenceToPhotoName(photos.get(3).getPath(), item.getReference()));
		}
		if (photos.size() >= 5) {
			r.setPhoto5(photoDirName + "/" + Functions.addReferenceToPhotoName(photos.get(4).getPath(), item.getReference()));
		}
		if (photos.size() >= 6) {
			r.setPhoto6(photoDirName + "/" + Functions.addReferenceToPhotoName(photos.get(5).getPath(), item.getReference()));
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
			r.setHonoraires(ObjectUtils.toString(rent.getCommission()));
			r.setDepotDeGarantie(ObjectUtils.toString(rent.getDeposit()));
			r.setMandatExclusif(toBoolean(rent.getExclusive()));
			r.setMeuble(toBoolean(rent.getFurnished()));
			
			// No charge included
			if(rent.getServiceChargeIncluded() == null || !rent.getServiceChargeIncluded()) {
				r.setLoyerSC(ObjectUtils.toString(rent.getPrice()));
				r.setCharges(ObjectUtils.toString(rent.getServiceCharge()));
			}
			else {
				r.setLoyerCC(ObjectUtils.toString(rent.getPriceFinal()));
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
			r.setHonoraires(sale.getCommission().toString());
			r.setMandatExclusif(toBoolean(sale.getExclusive()));
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
					r.setTypeAnnonce("Vente");
					populateSale(item, r);
					break;
				case MODE_RENT:
					r.setTypeAnnonce("Location");
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

	public void setPhotoDirName(String photoDirName) {
		this.photoDirName = photoDirName;
	}

	public void setServiceSeLoger(ServicePartner serviceSeLoger) {
		this.serviceSeLoger = serviceSeLoger;
	}

	private String toBoolean(Boolean b) {
		return b == null ? "" : b ? "O" : "N";
	}

}