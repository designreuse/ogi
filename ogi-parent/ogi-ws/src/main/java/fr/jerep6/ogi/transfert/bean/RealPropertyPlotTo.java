package fr.jerep6.ogi.transfert.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealPropertyPlotTo extends RealPropertyTo {
	private Boolean	building;
	private String	zone;

	public RealPropertyPlotTo() {
		super();
	}

}
