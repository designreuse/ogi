package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessErrorOwner implements ErrorCode {
	NO_SURNAME("OWNER_NO_REFERENCE", "Owner haven't surname"), //
	;

	private String	code;
	private String	message;

	EnumBusinessErrorOwner(String code, String message) {
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
