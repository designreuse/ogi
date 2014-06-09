package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessErrorLabel implements ErrorCode {
	NO_TEXT("LABEL_NO_TEXT", "Aucun libéllé"), //
	LABEL_SIZE("LABEL_SIZE", "Taille trop grande"), //
	;

	private String	code;
	private String	message;

	EnumBusinessErrorLabel(String code, String message) {
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
