package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescriptionType {
	SHOP_FRONT("VITRINE", 2048), //
	WEBSITE_OWN("WEBSITE_PERSO", 300), //
	WEBSITE_OTHER("WEBSITE_AUTRE", 2048), //
	APP("APP", 2048), //
	CLIENT("CLIENT", 2048); //

	/**
	 * Get the enumeration from this code
	 *
	 * @param code
	 * @return
	 */
	public static EnumDescriptionType valueOfByCode(String code) {
		for (EnumDescriptionType oneEnum : EnumDescriptionType.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumDescriptionType.class.getSimpleName() + " for " + code);
	}

	private String	code;
	private Integer	maxLength;

}
