package fr.jerep6.ogi.service.external.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorProperty;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.external.ServicePartner;

@Service("serviceAnnoncesJaunes")
public class ServiceAnnoncesJaunesImpl extends AbstractService implements ServicePartner {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceAnnoncesJaunesImpl.class);

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Override
	public void validate(RealProperty item) throws MultipleBusinessException {
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (item.getAddress() == null) {
			mbe.add(EnumBusinessErrorProperty.NO_ADDRESS, item.getReference());
		}

		if (item.getSale() == null && item.getRent() == null) {
			mbe.add(EnumBusinessErrorProperty.NO_SALE, item.getReference());
			mbe.add(EnumBusinessErrorProperty.NO_RENT, item.getReference());
		}
		if (item.getSale() != null) {
			if (Strings.isNullOrEmpty(item.getSale().getMandateReference())) {
				mbe.add(EnumBusinessErrorProperty.NO_MANDAT_REFERENCE_SALE, item.getReference());
			}
		}

		if (item.getRent() != null) {
			if (item.getRent().getPrice() == null || item.getRent().getPrice() <= 0F) {
				mbe.add(EnumBusinessErrorProperty.NO_RENT_PRICE, item.getReference());
			}

			if (item.getRent().getCommission() == null || item.getRent().getCommission() < 0F) {
				mbe.add(EnumBusinessErrorProperty.NO_RENT_COMMISSION, item.getReference());
			}
		}

		if (item.getType() == null) {
			mbe.add(EnumBusinessErrorProperty.NO_TYPE, item.getReference());
		}

		Description description = item.getDescription(EnumDescriptionType.WEBSITE_OTHER);
		if (description == null || description.getLabel() == null) {
			mbe.add(EnumBusinessErrorProperty.NO_DESCRIPTION_WEBSITE_OTHER, item.getReference());
		}

		if (item instanceof RealPropertyLivable) {
			if (((RealPropertyLivable) item).getNbRoom() == null) {
				mbe.add(EnumBusinessErrorProperty.NO_ROOM_NUMBER, item.getReference());
			}
		}

		mbe.checkErrors();
	}
}