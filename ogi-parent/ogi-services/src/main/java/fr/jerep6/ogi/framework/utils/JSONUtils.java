package fr.jerep6.ogi.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public final class JSONUtils {
	/**
	 * Transforme un objet java en flux json
	 *
	 * @param o
	 *            objet à transformer
	 * @return
	 */
	public static String toJson(Object o) {
		try {
			return JSONUtils.mapper.writeValueAsString(o);
		} catch (Exception e) {
			LOGGER.error("Erreur lors de la transformation de l'objet java en flux json : ", e);
			throw new RuntimeException("Erreur conversion Object-->JSON", e);
		}
	}

	/**
	 * Converti le flux json en objet de la classe spécifiée
	 *
	 * @param fluxJson
	 *            flux json dont on souhaite obtenir un objet
	 * @param classe
	 *            classe de l'objet à instancier
	 * @return
	 */
	public static <T> T toObject(String fluxJson, Class<T> classe) {
		T o = null;
		if (!Strings.isNullOrEmpty(fluxJson)) {
			try {
				o = JSONUtils.mapper.readValue(fluxJson, classe);
			} catch (Exception e) {
				JSONUtils.LOGGER.error("Erreur lors de la transformation du flux json en objet java : ", e);
				throw new RuntimeException("Erreur conversion JSON-->Object", e);
			}
		}
		return o;
	}

	private static Logger		LOGGER	= LoggerFactory.getLogger(JSONUtils.class);

	public static ObjectMapper	mapper	= new ObjectMapper();

	/**
	 * Private constructor : don't allow instantiation of this class
	 */
	private JSONUtils() {}

}