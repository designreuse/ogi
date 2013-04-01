package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Type;

public interface DaoType extends DaoCRUD<Type, Integer> {

	Type readByLabel(String label);
}
