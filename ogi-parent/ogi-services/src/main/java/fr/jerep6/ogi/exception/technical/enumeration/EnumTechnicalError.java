package fr.jerep6.ogi.exception.technical.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumTechnicalError implements ErrorCode {
	DPE("DPE_01", "Error genating dpe");

	private String	code;
	private String	message;

	EnumTechnicalError(String code, String message) {
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
