package fr.jerep6.ogi.search.service;

import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.search.obj.SearchResult;

public interface ServiceSearch extends Service {

	SearchResult search(String keyword);

}
