package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Owner;

public interface ServiceOwner extends TransactionalService<Owner, Integer> {
	List<Owner> readByProperty(String prpRef);

	/**
	 * Create or update owners. If techid exist => update. If techid doesn't exist => create
	 * Don't crate/update property association
	 * 
	 * @param ownersBo
	 * @return
	 */
	Owner createOrUpdate(Owner owner);

	/**
	 * Add a property from an owner.
	 * 
	 * @param ownerTechid
	 *            technical id of owner
	 * @param prpRef
	 *            fonctional reference of property
	 */
	void addProperty(Integer ownerTechid, String prpRef);

	/**
	 * Delete a property from an owner
	 * 
	 * @param ownerTechid
	 *            technical id of owner
	 * @param prpRef
	 *            fonctional reference of property
	 */
	void deleteProperty(Integer ownerTechid, String prpRef);

	Set<Owner> read(Set<Integer> techids);

	Set<Owner> merge(Set<Owner> ownersBD, Set<Owner> ownersModify);
}
