package fr.jerep6.ogi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.framework.utils.JSONUtils;

@Component
public class HttpClientUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(HttpClientUtils.class);

	public static <T> T convertToJson(HttpResponse response, Class<T> classe) throws IOException {
		String s = convertToString(response);
		return JSONUtils.toObject(s, classe);
	}

	public static String convertToString(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder result = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
}
