package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumMandateType {
	SIMPLE("SI"), //
	EXCLUSIF("EX"); //

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumMandateType valueOfByCode(String code) {
		for (EnumMandateType oneEnum : EnumMandateType.values()) {
			if (oneEnum.getCode().equals(code)) { return oneEnum; }
		}

		throw new IllegalArgumentException("No " + EnumMandateType.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
