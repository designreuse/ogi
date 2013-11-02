package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface DaoProperty extends DaoCRUD<RealProperty, Integer> {

	RealProperty readByReference(String reference);

	void test();

	/**
	 * Return maximum techid into database for property
	 * 
	 * @return
	 */
	Integer getMax();
}
