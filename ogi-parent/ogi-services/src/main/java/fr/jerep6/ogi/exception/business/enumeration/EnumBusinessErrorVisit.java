package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessErrorVisit implements ErrorCode {
	NO_DESCRIPTION("VISIT_DESCRIPTION", "Visit haven't description"), //
	NO_DATE("VISIT_DATE", "Visit haven't date"), //
	;

	private String	code;
	private String	message;

	EnumBusinessErrorVisit(String code, String message) {
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
