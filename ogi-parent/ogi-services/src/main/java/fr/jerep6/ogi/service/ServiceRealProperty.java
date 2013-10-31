package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface ServiceRealProperty extends TransactionalService<RealProperty, Integer> {
	/**
	 * Read a property by this business reference
	 * 
	 * @param reference
	 */
	RealProperty readByReference(String reference);

	/**
	 * Create a real property. All the associated entities are read in database from theirs business fields.
	 * If entity doesn't exist it will be created.
	 * 
	 * 
	 * 
	 * @param property
	 * @return
	 */
	RealProperty createOrUpdateFromBusinessFields(RealProperty property);

	/**
	 * Delete properties
	 * 
	 * @param reference
	 *            properties references to delete
	 */
	void delete(List<String> reference);

}
