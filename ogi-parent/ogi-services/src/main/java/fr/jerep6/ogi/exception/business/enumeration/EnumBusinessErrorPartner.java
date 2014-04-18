package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessErrorPartner implements ErrorCode {
	ACIMFLO_IDENTIFIANTS_KO("ACIMFLO_IDENTIFIANTS_KO", "Wrong  login/pwd"), //
	DIAPORAMA_IDENTIFIANTS_KO("DIAPORAMA_IDENTIFIANTS_KO", "Wrong  login/pwd"), //
	;

	private String	code;
	private String	message;

	EnumBusinessErrorPartner(String code, String message) {
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
