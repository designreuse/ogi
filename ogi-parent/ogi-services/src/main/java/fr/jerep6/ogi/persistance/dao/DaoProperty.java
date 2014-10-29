package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumSortByDirection;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface DaoProperty extends DaoCRUD<RealProperty, Integer> {

	List<RealProperty> readByReference(List<String> references);

	void test();

	Long getReference();

	List<RealProperty> list(Integer pageNumber, Integer itemNumberPerPage, String sortBy, EnumSortByDirection sortDir);

	List<Object[]> readTechids(List<String> references);

	List<Object[]> readReferences(List<Integer> techid);
}
