package fr.jerep6.ogi.framework.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.exception.AbstractException;
import fr.jerep6.ogi.framework.exception.BusinessException;

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

		String codeException = null;
		Object[] args = new Object[] {};
		if (exception instanceof AbstractException) {
			Object[] tmpArgs = Objects.firstNonNull(((AbstractException) exception).getArguments(), new Object[]{});
			List<Object> argsList = new ArrayList<>();

			for (Object arg : tmpArgs) {
				// Argument may be a property. If no property corresponding to argument, value is argument itself
				DefaultMessageSourceResolvable mess = new DefaultMessageSourceResolvable(
						new String[] { String.valueOf(arg) }, String.valueOf(arg));
				argsList.add(ContextUtils.getMessage(mess));
			}
			args = argsList.toArray();

			if (exception instanceof BusinessException) {
				codeException = ((BusinessException) exception).getCode();
			}
		}

		// Priority for exception code
		String code = codeException != null ? codeException : exception.getClass().getCanonicalName();

		String tmpMsg = ContextUtils.getMessage(code, args);
		if (!Strings.isNullOrEmpty(tmpMsg)) {
			i18nMsg = tmpMsg;
		}

		return i18nMsg;
	}
}
