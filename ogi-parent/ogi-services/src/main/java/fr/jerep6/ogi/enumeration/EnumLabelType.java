package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumLabelType {
	HEATING("HEATING"), //
	HOTWATER("HOTWATER"), //
	ROOMTYPE("ROOMTYPE"), //
	PARKING("PARKING");

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumLabelType valueOfByCode(String code) {
		for (EnumLabelType oneEnum : EnumLabelType.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumLabelType.class.getSimpleName() + "for " + code);
	}

	private String	code;

}
