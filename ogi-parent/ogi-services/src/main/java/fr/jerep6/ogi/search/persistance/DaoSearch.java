package fr.jerep6.ogi.search.persistance;

import java.util.List;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchResult;

public interface DaoSearch {
	SearchResult search(SearchCriteria criteria);

	void index(List<RealProperty> realProperty);

	void delete(List<String> references);
}
