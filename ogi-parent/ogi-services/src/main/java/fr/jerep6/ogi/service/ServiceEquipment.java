package fr.jerep6.ogi.service;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Equipment;

public interface ServiceEquipment extends TransactionalService<Equipment, Integer> {
	/**
	 * Read an equipment by this label and this category.
	 * 
	 * 
	 * @param label
	 *            equipment label
	 * @param category
	 *            category code of the equipment
	 * @return corresponding equipment. Null otherwise
	 */
	Equipment readByLabel(String label, EnumCategory category);

}
