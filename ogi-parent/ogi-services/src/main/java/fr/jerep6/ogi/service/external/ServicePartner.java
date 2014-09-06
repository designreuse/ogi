package fr.jerep6.ogi.service.external;

import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface ServicePartner extends Service {

	/**
	 * Validate property. If property doesn't respect partner interface throw MultipleBusinessException
	 *
	 * @param item
	 *            property to validate
	 * @throws MultipleBusinessException
	 */
	void validate(RealProperty item) throws MultipleBusinessException;
}
