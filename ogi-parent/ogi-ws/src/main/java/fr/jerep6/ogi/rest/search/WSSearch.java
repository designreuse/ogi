package fr.jerep6.ogi.rest.search;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.rest.AbstractJaxRsWS;
import fr.jerep6.ogi.rest.search.transfert.SearchResultTo;
import fr.jerep6.ogi.search.obj.SearchResult;
import fr.jerep6.ogi.search.service.ServiceSearch;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@Controller
@Path("/search")
public class WSSearch extends AbstractJaxRsWS {

	@Autowired
	private ServiceSearch	serviceSearch;

	@Autowired
	private OrikaMapper		mapper;

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public SearchResultTo search(@QueryParam("keyword") String keyword) {
		SearchResult result = serviceSearch.search(keyword);
		return mapper.map(result, SearchResultTo.class);
	}

}