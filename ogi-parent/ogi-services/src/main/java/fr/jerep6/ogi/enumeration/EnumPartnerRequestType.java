package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumPartnerRequestType {
	ADD_UPDATE("push"), //
	ADD_UPDATE_ACK("push_ack"), //
	DELETE("delete"), //
	DELETE_ACK("delete_ack");

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumPartnerRequestType valueOfByCode(String code) {
		for (EnumPartnerRequestType oneEnum : EnumPartnerRequestType.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumPartnerRequestType.class.getSimpleName() + " for " + code);
	}

	private String	code;

}
