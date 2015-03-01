package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDocumentZoneList {
	NO("NO"), //
	ADMINISTRATIF_SALE("ADM_SALE"), //
	ADMINISTRATIF_RENT("ADM_RENT");//

	/**
	 * Get the enumeration from this code. No case sensitive
	 *
	 * @param code
	 * @return
	 */
	public static EnumDocumentZoneList valueOfByCode(String code) {
		for (EnumDocumentZoneList oneEnum : EnumDocumentZoneList.values()) {
			if (oneEnum.getCode().equalsIgnoreCase(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumDocumentZoneList.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
