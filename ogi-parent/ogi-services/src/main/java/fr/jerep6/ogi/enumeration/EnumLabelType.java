package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumLabelType {
	HEATING("HEATING"), //
	SANITATION("SANITATION"), //
	HOTWATER("HOTWATER"), //
	ROOMTYPE("ROOMTYPE"), //
	ROOF("ROOF"), //
	WALL("WALL"), //
	INSULATION("INSULATION"), //
	FLOOR("FLOOR"), //
	ROOM("ROOM"), //
	PARKING("PARKING");

	/**
	 * Get the enumeration from this code. No case sensitive
	 * 
	 * @param code
	 * @return
	 */
	public static EnumLabelType valueOfByCode(String code) {
		for (EnumLabelType oneEnum : EnumLabelType.values()) {
			if (oneEnum.getCode().equalsIgnoreCase(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumLabelType.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
