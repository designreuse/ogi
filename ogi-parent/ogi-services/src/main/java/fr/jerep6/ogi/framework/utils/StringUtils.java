package fr.jerep6.ogi.framework.utils;

import java.text.Normalizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(StringUtils.class);

	public static String stripAccents(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	}

}
