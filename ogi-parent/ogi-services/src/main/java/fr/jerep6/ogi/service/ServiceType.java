package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Type;

public interface ServiceType extends TransactionalService<Type, Integer> {
	/**
	 * Read a category by this label
	 * 
	 * @param code
	 * @return null if code doesn't exist
	 */
	Type readByLabel(String label);

	/**
	 * Return corresponding type or create it if not exist in database
	 * 
	 * @param label
	 *            label to read or create if not exist
	 * @param category
	 *            category for a new type
	 * @return never null. Always a type
	 */
	Type readOrInsert(String label, Category category);
}
