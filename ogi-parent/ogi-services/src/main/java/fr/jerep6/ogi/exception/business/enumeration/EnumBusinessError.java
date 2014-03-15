package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessError implements ErrorCode {
	SALE_MANDAT_DATE("SAL_01", "Wrong  mandate dates"), //
	ACIMFLO_IDENTIFIANTS_KO("ACIMFLO_IDENTIFIANTS_KO", "Wrong  login/pwd"), //
	DIAPORAMA_IDENTIFIANTS_KO("DIAPORAMA_IDENTIFIANTS_KO", "Wrong  login/pwd"), //
	NO_SALE("NO_SALE", "Property haven't sale"), //
	NO_DESCRIPTION_WEBSITE_OWN("NO_DESCRIPTION_WEBSITE_OWN", "Property haven't description WEBSITE_OWN"), //
	NO_DESCRIPTION_WEBSITE_OTHER("NO_DESCRIPTION_WEBSITE_OTHER", "Property haven't description WEBSITE_OTHER"), //
	NO_ADDRESS("NO_ADDRESS", "Property haven't address"), //
	NO_TYPE("NO_TYPE", "Property haven't type"), //
	NO_MANDAT_REFERENCE("NO_MANDAT_REFERENCE", "Property haven't mandat reference"), //
	;

	private String	code;
	private String	message;

	EnumBusinessError(String code, String message) {
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
