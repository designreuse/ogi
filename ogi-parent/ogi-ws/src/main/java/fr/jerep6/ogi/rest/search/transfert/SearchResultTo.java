package fr.jerep6.ogi.rest.search.transfert;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import fr.jerep6.ogi.search.model.SearchRealProperty;
import fr.jerep6.ogi.search.obj.SearchResultAggregation;

@Getter
@Setter
public class SearchResultTo {
	private long										totalResults;
	private List<SearchRealProperty>					property;
	private Map<String, List<SearchResultAggregation>>	aggregations;

}
