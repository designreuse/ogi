package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumPageSize {
	A4("A4"), //
	A3("A3"); //

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumPageSize valueOfByCode(String code) {
		for (EnumPageSize oneEnum : EnumPageSize.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumPageSize.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
