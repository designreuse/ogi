package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCategory {
	APARTMENT("APT"), //
	HOUSE("HSE"), //
	BUSINESS("BSN"), //
	GARAGE("GRG"), //
	PLOT("PLT");

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumCategory valueOfByCode(String code) {
		for (EnumCategory oneEnum : EnumCategory.values()) {
			if (oneEnum.getCode().equals(code)) { return oneEnum; }
		}

		throw new IllegalArgumentException("No " + EnumCategory.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
