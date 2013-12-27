package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Owner;

public interface ServiceOwner extends TransactionalService<Owner, Integer> {
	List<Owner> readByProperty(String prpRef);

	/**
	 * Associate the owners list to a property. <br />
	 * Owners must contain techid
	 * 
	 * @param prpRef
	 *            property reference
	 * @param ownersBo
	 */
	void associate(String prpRef, List<Owner> ownersBo);

	/**
	 * Create or update owners. If techid exist => update. If techid doesn't exist => create
	 * 
	 * @param ownersBo
	 * @return
	 */
	List<Owner> createOrUpdate(List<Owner> ownersBo);
}
