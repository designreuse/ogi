package fr.jerep6.ogi.rest.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.rest.AbtractWS;
import fr.jerep6.ogi.rest.search.transfert.SearchResultTo;
import fr.jerep6.ogi.search.model.SearchResult;
import fr.jerep6.ogi.search.obj.EnumRechercheFiltre;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilterRange;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilterTerm;
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
	public SearchResultTo search(@RequestParam("keyword") String keyword,
			@RequestParam(value = "categories", required = false) List<String> categories,//
			@RequestParam(value = "cities", required = false) List<String> cities, //
			@RequestParam(value = "modes", required = false) List<String> modes, //
			@RequestParam(value = "salePriceMin", required = false) Float salePriceMin, //
			@RequestParam(value = "salePriceMax", required = false) Float salePriceMax, //
			@RequestParam(value = "rentPriceMin", required = false) Float rentPriceMin, //
			@RequestParam(value = "rentPriceMax", required = false) Float rentPriceMax //
			) {

		SearchCriteria criteria = new SearchCriteria();
		criteria.setKeywords(keyword);
		if (categories != null && !categories.isEmpty()) {
			criteria.ajouterFiltre(new SearchCriteriaFilterTerm(EnumRechercheFiltre.CATEGORIE, categories.toArray()));
		}
		if (cities != null && !cities.isEmpty()) {
			criteria.ajouterFiltre(new SearchCriteriaFilterTerm(EnumRechercheFiltre.CITY, cities.toArray()));
		}
		if (modes != null && !modes.isEmpty()) {
			criteria.ajouterFiltre(new SearchCriteriaFilterTerm(EnumRechercheFiltre.MODE, modes.toArray()));
		}

		if (salePriceMin != null || salePriceMax != null) {
			criteria.ajouterFiltre(new SearchCriteriaFilterRange(EnumRechercheFiltre.SALE_MAX_PRICE,
					salePriceMin == null ? null : salePriceMin.toString(), salePriceMax == null ? null : salePriceMax
							.toString()));
		}

		SearchResult result = serviceSearch.search(criteria);
		return mapper.map(result, SearchResultTo.class);
	}
}