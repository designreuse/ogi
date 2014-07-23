package fr.jerep6.ogi.rest.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.rest.AbtractWS;
import fr.jerep6.ogi.rest.search.transfert.SearchResultTo;
import fr.jerep6.ogi.search.obj.SearchResult;
import fr.jerep6.ogi.search.service.ServiceSearch;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/search", produces = "application/json;charset=UTF-8")
public class WSSearch extends AbtractWS {

	@Autowired
	private ServiceSearch	serviceSearch;

	@Autowired
	private OrikaMapper		mapper;

	@RequestMapping(method = RequestMethod.GET)
	public SearchResultTo search(@RequestParam("keyword") String keyword) {
		SearchResult result = serviceSearch.search(keyword);
		return mapper.map(result, SearchResultTo.class);
	}

}