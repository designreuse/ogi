package fr.jerep6.ogi.framework.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;

/**
 *
 * @author jerep6
 */
@Component
public final class ContextUtils {
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static String getMessage(DefaultMessageSourceResolvable resolvable) {
		return applicationContext.getMessage(resolvable, Locale.FRANCE);
	}

	public static String getMessage(String code) {
		return getMessage(code, null);
	}

	public static String getMessage(String code, Object... args) {
		DefaultMessageSourceResolvable resolvable = new DefaultMessageSourceResolvable(new String[] { code }, args, "");
		return getMessage(resolvable);
	}

	public static String getProperty(String code) {
		return applicationContext.getBeanFactory().resolveEmbeddedValue("${" + code + "}");
	}

	/** Hold server uri. Populate with FilterRequestContext */
	public static final ThreadLocal<String>			threadLocalRequestURI	= new ThreadLocal<String>();

	private static ConfigurableApplicationContext	applicationContext;

	@Autowired
	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		ContextUtils.applicationContext = applicationContext;
	}

}
