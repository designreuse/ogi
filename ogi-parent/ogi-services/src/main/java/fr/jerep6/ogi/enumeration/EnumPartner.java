package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumPartner {
	ACIMFLO("acimflo"), //
	DIAPORAMA("diaporama"), //
	SE_LOGER("seloger"), //
	LE_BONCOIN("leboncoin"), //
	ANNONCES_JAUNES("annoncesjaunes"),
	BIEN_ICI("bienici");

	/**
	 * Get the enumeration from this code
	 *
	 * @param code
	 * @return
	 */
	public static EnumPartner valueOfByCode(String code) {
		for (EnumPartner oneEnum : EnumPartner.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumPartner.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
