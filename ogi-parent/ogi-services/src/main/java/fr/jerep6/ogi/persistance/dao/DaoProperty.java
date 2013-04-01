package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface DaoProperty extends DaoCRUD<RealProperty, Integer> {

	void readByReference(String reference);

	void test();
}
