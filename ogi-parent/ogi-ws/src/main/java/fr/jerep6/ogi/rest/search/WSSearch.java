package fr.jerep6.ogi.rest.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.rest.AbtractWS;
import fr.jerep6.ogi.search.obj.SearchCriteria;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilterRange;
import fr.jerep6.ogi.search.obj.SearchCriteriaFilterTerm;
import fr.jerep6.ogi.search.obj.SearchEnumFilter;
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
	public SearchResult search(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "categories", required = false) List<String> categories,//
			@RequestParam(value = "cities", required = false) List<String> cities, //
			@RequestParam(value = "modes", required = false) List<String> modes, //
			@RequestParam(value = "priceMin", required = false) Float priceMin, //
			@RequestParam(value = "priceMax", required = false) Float priceMax, //
			@RequestParam(value = "areaMin", required = false) Integer areaMin, //
			@RequestParam(value = "areaPriceMax", required = false) Integer areaMax, //
			@RequestParam(value = "landAreaMin", required = false) Integer landAreaMin, //
			@RequestParam(value = "landAreaPriceMax", required = false) Integer landAreaMax //
	) {

		SearchCriteria criteria = new SearchCriteria();
		criteria.setKeywords(keyword);
		if (categories != null && !categories.isEmpty()) {
			criteria.addFilter(new SearchCriteriaFilterTerm(SearchEnumFilter.CATEGORIE, categories.toArray()));
		}
		if (cities != null && !cities.isEmpty()) {
			criteria.addFilter(new SearchCriteriaFilterTerm(SearchEnumFilter.CITY, cities.toArray()));
		}
		if (modes != null && !modes.isEmpty()) {
			criteria.addFilter(new SearchCriteriaFilterTerm(SearchEnumFilter.MODE, modes.toArray()));
		}

		// Sale price
		if (priceMin != null || priceMax != null) {
			// Add SALE PRICE filter
			SearchCriteriaFilterRange filterSalePrice = new SearchCriteriaFilterRange(SearchEnumFilter.SALE_PRICE,
					priceMin == null ? null : priceMin.toString(), priceMax == null ? null : priceMax.toString());
			filterSalePrice.setOrIdentifiant("price");
			criteria.addFilter(filterSalePrice);

			// Add RENT PRICE filter
			SearchCriteriaFilterRange filterRentPrice = new SearchCriteriaFilterRange(SearchEnumFilter.RENT_PRICE,
					priceMin == null ? null : priceMin.toString(), priceMax == null ? null : priceMax.toString());
			filterRentPrice.setOrIdentifiant("price");
			criteria.addFilter(filterRentPrice);
		}

		// Area
		if (areaMin != null || areaMax != null) {
			criteria.addFilter(new SearchCriteriaFilterRange(SearchEnumFilter.AREA, areaMin == null ? null : areaMin
					.toString(), areaMax == null ? null : areaMax.toString()));
		}

		// Land Area
		if (landAreaMin != null || landAreaMax != null) {
			criteria.addFilter(new SearchCriteriaFilterRange(SearchEnumFilter.AREA, landAreaMin == null ? null
					: landAreaMin.toString(), landAreaMax == null ? null : landAreaMax.toString()));
		}

		return serviceSearch.search(criteria);
	}
}