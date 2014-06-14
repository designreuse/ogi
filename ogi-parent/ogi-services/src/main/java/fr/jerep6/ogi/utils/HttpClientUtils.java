package fr.jerep6.ogi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.framework.utils.JSONUtils;

@Component
public class HttpClientUtils {
	private static final Logger		LOGGER			= LoggerFactory.getLogger(HttpClientUtils.class);

	public static final ContentType	TEXT_PLAIN_UTF8	= ContentType.create("text/plain", Consts.UTF_8);

	private static Integer			timeoutConnection;

	private static Integer			timeoutSocket;

	/**
	 * Build an HttpClient with timeout config
	 * 
	 * @return
	 */
	public static HttpClient buildClient() {
		RequestConfig config = RequestConfig.custom()//
				.setSocketTimeout(timeoutConnection * 1000)//
				.setConnectTimeout(timeoutSocket * 1000)//
				.setConnectionRequestTimeout(timeoutSocket * 1000)//
				.build();

		return HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy())
				.setDefaultRequestConfig(config).build();
	}

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

	@Value("${httpclient.timeout.connection}")
	private void setTimeoutConnection(Integer timeout) {
		timeoutConnection = timeout;
	}

	@Value("${httpclient.timeout.socket}")
	private void setTimeoutSocket(Integer timeout) {
		timeoutSocket = timeout;
	}
}
