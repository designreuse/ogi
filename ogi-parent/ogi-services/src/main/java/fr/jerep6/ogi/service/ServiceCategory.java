package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Equipment;

public interface ServiceCategory extends TransactionalService<Category, Integer> {
	/**
	 * Read a category by this business code
	 * 
	 * @param code
	 */
	Category readByCode(EnumCategory enumCategory);

	/**
	 * Get all equipments from a category
	 * 
	 * @param category
	 * @return
	 */
	List<Equipment> readEquipments(EnumCategory category);
}
