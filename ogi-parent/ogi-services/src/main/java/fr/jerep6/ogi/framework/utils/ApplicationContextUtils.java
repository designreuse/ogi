package fr.jerep6.ogi.framework.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author jerep6
 */
@Component
public final class ApplicationContextUtils {
	private static ApplicationContext	applicationContext;

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtils.applicationContext = applicationContext;
	}

}
