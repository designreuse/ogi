package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumOrientation {
	NORTH("N"), //
	SOUTH("S"), //
	EAST("E"), //
	WEST("W"), //
	NORTH_EAST("NE"), //
	NORTH_WEST("NW"), //
	SOUTH_EAST("SE"), //
	SOUTH_WEST("SW");

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumOrientation valueOfByCode(String code) {
		for (EnumOrientation oneEnum : EnumOrientation.values()) {
			if (oneEnum.getCode().equals(code)) { return oneEnum; }
		}

		throw new IllegalArgumentException("No " + EnumOrientation.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
