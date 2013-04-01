package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Category;

public interface DaoCategory extends DaoCRUD<Category, Integer> {

	Category readByCode(EnumCategory enumCategory);

}
