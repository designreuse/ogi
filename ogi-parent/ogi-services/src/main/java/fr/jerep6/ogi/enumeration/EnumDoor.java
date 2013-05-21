package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDoor {
	NONE("N"), //
	MANUAL("M"), //
	ELECTRIC("E");

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumDoor valueOfByCode(String code) {
		for (EnumDoor oneEnum : EnumDoor.values()) {
			if (oneEnum.getCode().equals(code)) { return oneEnum; }
		}

		throw new IllegalArgumentException("No " + EnumDoor.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
