package fr.jerep6.ogi.framework.exception;

public class BusinessException extends AbstractException {
	private static final long	serialVersionUID	= 1L;

	public BusinessException() {
		super();
	}

	/**
	 * @param message
	 *            exception message
	 */
	public BusinessException(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            exception message
	 * @param args
	 *            : Arguments
	 */
	public BusinessException(final String message, final Object... args) {
		super(message, args);
	}

	/**
	 * @param message
	 *            exception message
	 * @param cause
	 *            : mother causse of exception
	 */
	public BusinessException(final String message, Throwable cause) {
		super(message, cause);
	}

}
