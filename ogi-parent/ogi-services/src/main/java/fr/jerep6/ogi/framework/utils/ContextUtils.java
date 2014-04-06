package fr.jerep6.ogi.framework.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

/**
 * 
 * @author jerep6
 */
@Component
public final class ContextUtils {
	/** Hold server uri. Populate with FilterRequestContext */
	public static final ThreadLocal<String>			threadLocalRequestURI	= new ThreadLocal<String>();
	private static ConfigurableApplicationContext	applicationContext;

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static String getMessage(String code) {
		return getMessage(code, null);
	}

	public static String getMessage(String code, Object... args) {
		String msg = "";
		try {
			msg = applicationContext.getMessage(code, args, Locale.FRANCE);
		} catch (NoSuchMessageException nsme) {
			// No message was found.
		}
		return msg;
	}

	public static String getProperty(String code) {
		return applicationContext.getBeanFactory().resolveEmbeddedValue("${" + code + "}");
	}

	@Autowired
	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		ContextUtils.applicationContext = applicationContext;
	}

}
