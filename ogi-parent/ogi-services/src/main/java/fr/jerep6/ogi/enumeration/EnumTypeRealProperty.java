package fr.jerep6.ogi.enumeration;

public enum EnumTypeRealProperty {
	HSE("HSE"), LND("LND");

	private String	code;

	private EnumTypeRealProperty(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
