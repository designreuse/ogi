package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumError implements ErrorCode {
	SALE_MANDAT_DATE("SAL_01", "Wrong  mandate dates");

	private String	code;
	private String	message;

	EnumError(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
