package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumGestionMode {
	NO("NO"), //
	ADMINISTRATIF_SALE("SALE"), //
	ADMINISTRATIF_RENT("RENT");//

	/**
	 * Get the enumeration from this code. No case sensitive
	 *
	 * @param code
	 * @return
	 */
	public static EnumGestionMode valueOfByCode(String code) {
		for (EnumGestionMode oneEnum : EnumGestionMode.values()) {
			if (oneEnum.getCode().equalsIgnoreCase(code)) {
				return oneEnum;
			}
		}
		throw new IllegalArgumentException("No " + EnumGestionMode.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
