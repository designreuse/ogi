package fr.jerep6.ogi.framework.utils;

public class ObjectUtils {

	/**
	 * Return toString of an object. If object is null return empty string
	 * 
	 * @param o
	 * @return
	 */
	public static String toString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

}
