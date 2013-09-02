package fr.jerep6.ogi.framework.utils;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.exception.AbstractException;

/**
 * 
 * @author jerep6
 */
@Component
public final class ExceptionUtils {
	/**
	 * Translate message of exception according to property.
	 * Properties key = complete class name of exception.
	 * 
	 * @param exception
	 * @return
	 */
	public static <T extends Exception> String i18n(T exception) {
		String i18nMsg = exception.getMessage();

		Object[] args = new Object[] {};
		if (exception instanceof AbstractException) {
			args = ((AbstractException) exception).getArguments();
		}

		String tmpMsg = ContextUtils.getMessage(exception.getClass().getCanonicalName(), args);
		if (!Strings.isNullOrEmpty(tmpMsg)) {
			i18nMsg = tmpMsg;
		}

		return i18nMsg;
	}
}
