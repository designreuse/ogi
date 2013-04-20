package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

// Lombok
@Getter
@Setter
public abstract class RealPropertyBuiltTo extends RealPropertyTo {
	private Integer		area;
	private Calendar	buildDate;
	private Integer		nbFloor;
	private Integer		floorLevel;
	private String		orientation;
	private String		parking;

}
