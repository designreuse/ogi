package fr.jerep6.ogi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class MyUrlUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(MyUrlUtils.class);

	/**
	 * Replace in string specials char
	 * <ul>
	 * <li>\ => /</li>
	 * </ul>
	 * 
	 * @param s
	 *            string to replace
	 * @return
	 */
	public static String replace(String s) {
		String url = "";
		if (!Strings.isNullOrEmpty(s)) {
			url = s.replace("\\", "/");
		}
		return url;
	}

}
