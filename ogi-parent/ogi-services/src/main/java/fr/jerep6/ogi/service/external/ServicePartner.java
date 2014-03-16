package fr.jerep6.ogi.service.external;

import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.transfert.WSResult;

public interface ServicePartner extends Service {

	WSResult createOrUpdate(RealProperty prp);

	Boolean exist(String prpReference);

	/**
	 * Delete a property from partner.
	 * 
	 * @param prpReference
	 *            functional reference
	 * @param techidForAck
	 *            technical identifying in order to create ack
	 * @return
	 */
	WSResult delete(String prpReference, Integer techidForAck);
}