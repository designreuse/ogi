package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescriptionType {
	SHOP_FRONT("VITRINE"), //
	WEBSITE_OWN("WEBSITE_PERSO"), //
	WEBSITE_AD("WEBSITE_AUTRE"), //
	STATE("ETAT"), //
	WORK("TRAVAUX");

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

		throw new IllegalArgumentException("No " + EnumDescriptionType.class.getSimpleName() + "for " + code);
	}

	private String	code;

}
