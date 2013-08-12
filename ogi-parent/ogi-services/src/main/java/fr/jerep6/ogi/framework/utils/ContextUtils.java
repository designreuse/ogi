package fr.jerep6.ogi.framework.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author jerep6
 */
@Component
public final class ContextUtils {
	private static ConfigurableApplicationContext	applicationContext;

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static String getMessage(String code) {
		return applicationContext.getMessage(code, null, Locale.FRANCE);
	}

	public static String getProperty(String code) {
		return applicationContext.getBeanFactory().resolveEmbeddedValue("${" + code + "}");
	}

	@Autowired
	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		ContextUtils.applicationContext = applicationContext;
	}

}
