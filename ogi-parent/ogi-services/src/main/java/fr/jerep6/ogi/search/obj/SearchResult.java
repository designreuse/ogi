package fr.jerep6.ogi.search.obj;

import java.util.List;

import lombok.Getter;
import fr.jerep6.ogi.persistance.bo.RealProperty;

@Getter
public class SearchResult {
	private List<RealProperty>	prp;
	private Integer				total;

	public SearchResult(List<RealProperty> prp, Integer total) {
		super();
		this.prp = prp;
		this.total = total;
	}

}
