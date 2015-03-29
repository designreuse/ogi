package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import fr.jerep6.ogi.persistance.bo.DocumentType;

@Getter
@AllArgsConstructor
public enum EnumDocumentType {
	PHOTO("PHOTO"); //

	/**
	 * Get the enumeration from this code. No case sensitive
	 *
	 * @param code
	 * @return
	 */
	public static EnumDocumentType valueOfByCode(String code) {
		for (EnumDocumentType oneEnum : EnumDocumentType.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumDocumentType.class.getSimpleName() + " for " + code);
	}

	private String	code;

	public Boolean equalsWithDocumentType(DocumentType type) {
		return type != null && code.equals(type.getCode());
	}

}
