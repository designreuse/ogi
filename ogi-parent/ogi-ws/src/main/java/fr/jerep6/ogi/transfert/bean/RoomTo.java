package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Getter
@Setter
@EqualsAndHashCode(of = { "techid" })
public class RoomTo {
	private Integer	techid;
	private String	roomType;
	private Integer	area;
	private Boolean	carrezLaw;
	private Boolean	livable;
	private String	orientation;
	private String	floor;
	private String	wall;
	private String	view;
	private Integer	floorLevel;
	private String	description;
	private Integer	nbWindow;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("area", area).toString();
	}
}