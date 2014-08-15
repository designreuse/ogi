package fr.jerep6.ogi.framework.utils;

import java.text.Normalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	public static String stripAccents(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}

	public static String truncate(String s, int maxSize) {
		if (s == null) {
			return null;
		}

		int inLength = s.length();
		if (inLength >= maxSize) {
			return s.substring(0, maxSize - 1);
		}

		return s;
	}

	private static final Logger	LOGGER	= LoggerFactory.getLogger(StringUtils.class);
}
