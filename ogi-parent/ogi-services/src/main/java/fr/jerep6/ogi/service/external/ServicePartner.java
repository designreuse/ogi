package fr.jerep6.ogi.service.external;

import java.util.function.Function;

import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.transfert.WSResult;

public interface ServicePartner extends Service {
	public static Function<String, String>	computeReferenceSale	= (String ref) -> ref + "V";

	WSResult createOrUpdate(RealProperty prp);

	Boolean exist(RealProperty prp);

	/**
	 * Delete a property from partner.
	 * 
	 * @param prp
	 *            realproperty to delete
	 * @return
	 */
	WSResult delete(RealProperty prp);

	/**
	 * Validate property. If property doesn't respect partner interface throw MultipleBusinessException
	 * 
	 * @param item
	 *            property to validate
	 * @throws MultipleBusinessException
	 */
	void validate(RealProperty item) throws MultipleBusinessException;
}
