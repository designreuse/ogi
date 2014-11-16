package fr.jerep6.ogi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.utils.ContextUtils;

@Component
public class MyUrlUtils {
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

	public static String urlDocument(String p) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(p));

		StringBuilder sb = new StringBuilder();
		sb.append(ContextUtils.threadLocalRequestURI.get());
		sb.append(photoContext);
		sb.append(MyUrlUtils.replace(p.toString()));
		return sb.toString();
	}

	public static String urlProperty(String reference) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));

		StringBuilder sb = new StringBuilder();
		sb.append(ContextUtils.threadLocalRequestURI.get());
		sb.append(restUrlContext).append("/");
		sb.append(PATH_PROPERTY).append("/");
		sb.append(reference);

		return sb.toString();
	}

	private static final String	PATH_PROPERTY	= "property";

	private static final Logger	LOGGER			= LoggerFactory.getLogger(MyUrlUtils.class);

	private static String		restUrlContext;

	private static String		photoContext;

	@Value("${document.url.context}")
	public void setPhotoContext(String photoContext) {
		MyUrlUtils.photoContext = photoContext;
	}

	@Value("${rest.url.context}")
	public void setRestUrlContext(String restUrlContext) {
		MyUrlUtils.restUrlContext = restUrlContext;
	}

}
