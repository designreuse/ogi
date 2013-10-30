package fr.jerep6.ogi.framework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public final class UrlUtils {
	private static final Logger	LOGGER				= LoggerFactory.getLogger(UrlUtils.class);
	private static final String	CHARSET_ENCODAGE	= "UTF-8";

	/**
	 * Construit une url a partir d'une url de base et d'un ensemble de paramètres
	 * 
	 * @param base
	 * @param params
	 * @return
	 */
	public static String addParameter(String base, Map<String, String> params) {
		try {
			StringBuilder url = new StringBuilder();
			StringBuilder data = new StringBuilder();

			String dataTemp = "";
			url.append(base);

			if (params == null) {
				return url.toString();
			}
			if (params.size() > 0) {
				for (Entry<String, String> entry : params.entrySet()) {
					data.append(URLEncoder.encode(entry.getKey(), UrlUtils.CHARSET_ENCODAGE)) //
							.append("=") //
							.append(URLEncoder.encode(entry.getValue(), UrlUtils.CHARSET_ENCODAGE))//
							.append("&");

				}
				dataTemp = data.toString();
			}
			String result = url.append("?").append(dataTemp.substring(0, dataTemp.length() - 1)).toString();
			return result;
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("Error encoding {}", params);
		}
		return base;
	}

	/**
	 * Ajoute un paramètre à une Url. Détermine s'il faut un "?" ou un "&"
	 * 
	 * @param url
	 *            url à laquelle ajouter le paramètre
	 * @param name
	 *            nom du paramètre
	 * @param value
	 *            valeur du paramètre (sera encodé en UTF8)
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String addParameter(String url, String name, String value) {
		try {
			int qpos = url.indexOf('?');
			int hpos = url.indexOf('#');
			char sep = qpos == -1 ? '?' : '&';
			String seg = sep + name + '=' + URLEncoder.encode(value, UrlUtils.CHARSET_ENCODAGE);
			return hpos == -1 ? url + seg : url.substring(0, hpos) + seg + url.substring(hpos);
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("Error encoding {}", value);
		}
		return url;
	}

	public static String deleteQueryParams(String urlWithParam) {
		String u = "";
		if (!Strings.isNullOrEmpty(urlWithParam)) {
			int qpos = urlWithParam.indexOf('?');
			u = urlWithParam.substring(0, qpos);
		}
		return u;
	}

	/**
	 * Retourne dans une map tous les paramètres de l'url
	 * 
	 * @param url
	 *            url dont on souhaite obtenir les paramètres
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, List<String>> getUrlParameters(String url) throws UnsupportedEncodingException {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		String[] urlParts = url.split("\\?");
		if (urlParts.length > 1) {
			String query = urlParts[1];
			for (String param : query.split("&")) {
				String pair[] = param.split("=");
				String key = URLDecoder.decode(pair[0], UrlUtils.CHARSET_ENCODAGE);
				String value = "";
				if (pair.length > 1) {
					value = URLDecoder.decode(pair[1], UrlUtils.CHARSET_ENCODAGE);
				}
				List<String> values = params.get(key);
				if (values == null) {
					values = new ArrayList<String>();
					params.put(key, values);
				}
				values.add(value);
			}
		}
		return params;
	}

	private UrlUtils() {}

}
