package fr.jerep6.ogi.framework.exception;

import java.util.Arrays;
import java.util.List;

/**
 * @author jerep6 Mar 17, 2013
 */
public abstract class AbstractException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;

	// Arguments
	private final Object[]		arguments;

	public AbstractException() {
		super();
		arguments = null;
	}

	/**
	 * @param message
	 *            exception message
	 */
	public AbstractException(final String message) {
		super(message);
		arguments = null;
	}

	/**
	 * 
	 * @param message
	 *            exception message
	 * @param args
	 *            : Arguments
	 */
	public AbstractException(final String message, final Object... args) {
		super(message);
		this.arguments = args;
	}

	/**
	 * @param message
	 *            exception message
	 * @param cause
	 *            : mother causse of exception
	 */
	public AbstractException(final String message, Throwable cause) {
		super(message, cause);
		this.arguments = null;
	}

	public Object[] getArguments() {
		if (arguments == null) {
			return null;
		}

		List<Object> elements = Arrays.asList(arguments);
		return elements.toArray(new Object[elements.size()]);
	}

}
