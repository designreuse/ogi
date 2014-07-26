package fr.jerep6.ogi.transfert.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.jerep6.ogi.transfert.mapping.json.JsonCalendarDeserializer;
import fr.jerep6.ogi.transfert.mapping.json.JsonCalendarSerializer;

// Lombok
@Getter
@Setter
public abstract class RealPropertyBuiltTo extends RealPropertyTo {
	private Float			area;
	@JsonSerialize(using = JsonCalendarSerializer.class)
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	private Calendar		buildDate;
	private Integer			nbFloor;
	private Integer			floorLevel;
	private String			orientation;
	private String			parking;
	private Integer			nbGarage;
	private StateTo			state;
	private String			insulation;
	private DPETo			dpe;
	private List<RoomTo>	rooms		= new ArrayList<>(0);
	private Boolean			coOwnership	= null;
	private Float			coOwnershipCharges;
	private Integer			coOwnershipLotNumber;

}
