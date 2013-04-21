package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Equipment;

public interface DaoEquipment extends DaoCRUD<Equipment, Integer> {

	Equipment readByLabel(String label, EnumCategory category);
}
