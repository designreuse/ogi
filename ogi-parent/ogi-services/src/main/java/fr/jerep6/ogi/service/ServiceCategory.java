package fr.jerep6.ogi.service;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;

public interface ServiceCategory extends TransactionalService<Category, Integer> {
	/**
	 * Read a category by this business code
	 * 
	 * @param code
	 */
	Category readByCode(EnumCategory enumCategory);
}
