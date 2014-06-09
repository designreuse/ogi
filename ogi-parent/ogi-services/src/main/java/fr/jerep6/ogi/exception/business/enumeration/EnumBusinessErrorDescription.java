package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessErrorDescription implements ErrorCode {
	NO_LABEL("DESC_NO_LABEL", "Aucun libellé"), //
	LABEL_SIZE("DESC_LABEL_SIZE", "Taille trop grande"), //
	;

	private String	code;
	private String	message;

	EnumBusinessErrorDescription(String code, String message) {
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
