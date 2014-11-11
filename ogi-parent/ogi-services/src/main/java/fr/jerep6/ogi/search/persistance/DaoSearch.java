package fr.jerep6.ogi.search.persistance;

import java.util.List;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchCriteria;

public interface DaoSearch {
	void search(SearchCriteria criteria);

	void index(List<RealProperty> realProperty);

	void delete(List<String> references);
}
