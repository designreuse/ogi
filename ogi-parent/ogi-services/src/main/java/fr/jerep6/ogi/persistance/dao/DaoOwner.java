package fr.jerep6.ogi.persistance.dao;

import java.util.Collection;
import java.util.List;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Owner;

public interface DaoOwner extends DaoCRUD<Owner, Integer> {

	List<Owner> readByProperty(String prpRef);

	List<Owner> read(Collection<Integer> techids);

}
