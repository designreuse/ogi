package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface DaoProperty extends DaoCRUD<RealProperty, Integer> {

	List<RealProperty> readByReference(List<String> references);

	void test();

	/**
	 * Return maximum techid into database for property
	 * 
	 * @return
	 */
	Integer getMax();
}
