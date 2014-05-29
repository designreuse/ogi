package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSortByDirection {
	ASC("asc"), //
	DESC("desc");

	/**
	 * Get the enumeration from this code
	 * If code is unknow return ASC
	 * 
	 * @param code
	 * @return
	 */
	public static EnumSortByDirection valueOfByCode(String code) {
		for (EnumSortByDirection oneEnum : EnumSortByDirection.values()) {
			if (oneEnum.getCode().equals(code)) {
				return oneEnum;
			}
		}//
		return ASC;
	}

	private String	code;

}
