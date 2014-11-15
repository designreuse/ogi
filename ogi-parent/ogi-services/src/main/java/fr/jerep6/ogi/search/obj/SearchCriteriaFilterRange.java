package fr.jerep6.ogi.search.obj;

public class SearchCriteriaFilterRange extends SearchCriteriaFilter {
	private String	min;
	private String	max;

	public SearchCriteriaFilterRange(SearchEnumFilter filtre, String min, String max) {
		super();
		this.filtre = filtre;
		this.min = min;
		this.max = max;
	}

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

}