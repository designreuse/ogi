package fr.jerep6.ogi.transfert.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealPropertyLivableTo extends RealPropertyBuiltTo {
	private Integer	nbRoom;
	private Integer	nbBedRoom;
	private Integer	nbBathRoom;
	private Integer	nbShowerRoom;
	private Integer	nbWC;
	private String	heating;
	private String	hotWater;
	private String	wall;
	private String	roof;

	public RealPropertyLivableTo() {}

}
