package fr.jerep6.ogi.transfert.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealPropertyBusinessTo extends RealPropertyBuiltTo {
	private Boolean		water;
	private Boolean		electricity;

	public RealPropertyBusinessTo() {
		super();
	}

}
