package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDocumentType {
	PHOTO("PHOTO"), //
	MISC("MISC");

	/**
	 * Get the enumeration from this code. No case sensitive
	 * 
	 * @param code
	 * @return
	 */
	public static EnumDocumentType valueOfByCode(String code) {
		for (EnumDocumentType oneEnum : EnumDocumentType.values()) {
			if (oneEnum.getCode().equalsIgnoreCase(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumDocumentType.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
