package fr.jerep6.ogi.service.external.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessError;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.service.ServicePartnerRequest;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.transfert.WSResult;

@Service("serviceSeLoger")
public class ServiceSeLogerImpl extends AbstractService implements ServicePartner {
	private final Logger			LOGGER	= LoggerFactory.getLogger(ServiceSeLogerImpl.class);

	@Autowired
	private ServicePartnerRequest	servicePartnerExistence;

	@Autowired
	private ServiceRealProperty		serviceRealProperty;

	@Override
	public WSResult createOrUpdate(RealProperty prp) {
		LOGGER.info("Create/update property {}. Do nothing because batch will process it later");
		return new WSResult(prp.getReference(), "WAIT", "");
	}

	@Override
	public WSResult delete(String prpReference, Integer techidForAck) {
		LOGGER.info("Delete property {}. Do nothing because batch will process it later");
		return new WSResult(prpReference, "WAIT", "");
	}

	@Override
	public Boolean exist(String prpReference) {
		Integer prpTechid = serviceRealProperty.readTechid(prpReference);

		// Détermine si le bien existe à partir du journal des demandes
		boolean b = servicePartnerExistence.lastRequestIs(EnumPartner.SE_LOGER, prpTechid,
				EnumPartnerRequestType.ADD_UPDATE, EnumPartnerRequestType.ADD_UPDATE_ACK);
		return b;
	}

	@Override
	public void validate(RealProperty item) throws MultipleBusinessException {
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (item.getAddress() == null) {
			mbe.add(EnumBusinessError.NO_ADDRESS, item.getReference());
		}

		if (item.getSale() == null && item.getRent() == null) {
			mbe.add(EnumBusinessError.NO_SALE, item.getReference());
			mbe.add(EnumBusinessError.NO_RENT, item.getReference());
		}
		if (item.getSale() != null) {
			if (Strings.isNullOrEmpty(item.getSale().getMandateReference())) {
				mbe.add(EnumBusinessError.NO_MANDAT_REFERENCE, item.getReference());
			}
		}

		if (item.getRent() != null) {
			if (item.getRent().getPrice() == null || item.getRent().getPrice() <= 0F) {
				mbe.add(EnumBusinessError.NO_RENT_PRICE, item.getReference());
			}

			if (item.getRent().getCommission() == null || item.getRent().getCommission() < 0F) {
				mbe.add(EnumBusinessError.NO_RENT_COMMISSION, item.getReference());
			}
		}

		if (item.getType() == null) {
			mbe.add(EnumBusinessError.NO_TYPE, item.getReference());
		}
		if (item.getDescription(EnumDescriptionType.WEBSITE_OTHER) == null) {
			mbe.add(EnumBusinessError.NO_DESCRIPTION_WEBSITE_OTHER, item.getReference());
		}

		if (item instanceof RealPropertyLivable) {
			if (((RealPropertyLivable) item).getNbRoom() == null) {
				mbe.add(EnumBusinessError.NO_ROOM_NUMBER, item.getReference());
			}
		}

		mbe.checkErrors();
	}
}