package fr.jerep6.ogi.rest.search.transfert;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;

@Getter
@Setter
public class SearchResultTo {
	private List<RealPropertyTo>	prp;
	private Integer					total;

}
