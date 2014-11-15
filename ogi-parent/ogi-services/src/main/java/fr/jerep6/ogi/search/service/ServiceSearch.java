package fr.jerep6.ogi.search.service;

import java.util.List;

import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchResult;

public interface ServiceSearch extends Service {

	SearchResult search(SearchCriteria criteria);

	void index(RealProperty r);

	void index(List<RealProperty> r);

	void delete(List<String> references);

}
