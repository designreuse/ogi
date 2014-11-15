package fr.jerep6.ogi.search.obj;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import fr.jerep6.ogi.search.model.SearchRealProperty;

@Getter
@AllArgsConstructor
public class SearchResult {
	private long										totalResults;
	private List<SearchRealProperty>					property;
	private Map<String, List<SearchResultAggregation>>	aggregations;

}
