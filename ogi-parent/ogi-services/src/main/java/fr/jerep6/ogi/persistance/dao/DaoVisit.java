package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.Visit;

public interface DaoVisit extends DaoCRUD<Visit, Integer> {
	List<Visit> readByProperty(String prpRef);
}
