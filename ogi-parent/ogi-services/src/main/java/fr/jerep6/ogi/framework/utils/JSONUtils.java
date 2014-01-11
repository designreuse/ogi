package fr.jerep6.ogi.framework.utils;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public final class JSONUtils {
	private static Logger		LOGGER	= LoggerFactory.getLogger(JSONUtils.class);
	public static ObjectMapper	mapper	= new ObjectMapper();

	/**
	 * Transforme un objet java en flux json
	 * 
	 * @param o
	 *            objet à transformer
	 * @return
	 */
	public static String toJson(Object o) {
		try {
			StringWriter sw = new StringWriter();
			MappingJsonFactory jsonFactory = new MappingJsonFactory();
			JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
			JSONUtils.mapper.writeValue(jsonGenerator, o);
			sw.close();
			return sw.getBuffer().toString();
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

	/**
	 * Private constructor : don't allow instantiation of this class
	 */
	private JSONUtils() {}

}