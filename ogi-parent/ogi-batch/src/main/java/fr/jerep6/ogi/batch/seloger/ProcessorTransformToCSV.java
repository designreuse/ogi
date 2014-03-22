package fr.jerep6.ogi.batch.seloger;

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
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessError;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.utils.ObjectUtils;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.service.ServiceUrl;

public class ProcessorTransformToCSV implements ItemProcessor<RealProperty, RealPropertyCSV> {
	private static Logger						LOGGER		= LoggerFactory.getLogger(ProcessorTransformToCSV.class);

	/** Matching between OGI category and seloger category */
	private static Map<EnumCategory, String>	mapTypeBien	= new HashMap<>();
	static {
		mapTypeBien.put(EnumCategory.APARTMENT, "appartement,");
		mapTypeBien.put(EnumCategory.HOUSE, "maison");
		mapTypeBien.put(EnumCategory.PLOT, "terrain");
	}

	private ServiceUrl							serviceUrl;
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

	@Override
	public RealPropertyCSV process(RealProperty item) throws Exception {
		try {
			validate(item);

			RealPropertyCSV r = new RealPropertyCSV(item);
			r.setAgenceId(estateCode);

			if (item.getSale() != null) {
				r.setTypeAnnonce("vente");
				r.setMandatNumero(item.getSale().getMandateReference());
			} else {
				r.setTypeAnnonce("location");
			}
			r.setTypeBien(mapTypeBien.get(item.getCategory().getCode()));

			// Price
			r.setPrix(item.getSale().getPriceFinal().toString());
			r.setHonoraires(""); // Pas de location pour le moment

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

	public void setServiceUrl(ServiceUrl serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	private String toBoolean(boolean b) {
		return b ? "OUI" : "NON";
	}

	private void validate(RealProperty item) throws MultipleBusinessException {
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (item.getAddress() == null) {
			mbe.add(EnumBusinessError.NO_ADDRESS);
		}

		if (item.getSale() == null) {
			mbe.add(EnumBusinessError.NO_SALE);
		} else if (Strings.isNullOrEmpty(item.getSale().getMandateReference())) {
			mbe.add(EnumBusinessError.NO_MANDAT_REFERENCE);
		}

		if (item.getType() == null) {
			mbe.add(EnumBusinessError.NO_TYPE);
		}
		if (item.getDescription(EnumDescriptionType.WEBSITE_OTHER) == null) {
			mbe.add(EnumBusinessError.NO_DESCRIPTION_WEBSITE_OTHER);
		}

		mbe.checkErrors();
	}
}