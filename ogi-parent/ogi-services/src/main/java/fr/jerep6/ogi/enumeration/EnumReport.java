package fr.jerep6.ogi.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumReport {
	VITRINE("vitrine"), //
	CLIENT("client"); //

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumReport valueOfByName(String name) {
		for (EnumReport anEnum : EnumReport.values()) {
			if (anEnum.getName().equals(name)) {
				return anEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumReport.class.getSimpleName() + " for " + name);
	}

	private String	name;

}
