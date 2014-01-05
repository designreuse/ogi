package fr.jerep6.ogi.framework.exception;

public class TechnicalException extends AbstractException {
	private static final long	serialVersionUID	= 1L;

	public TechnicalException() {
		super();
	}

	/**
	 * @param message
	 *            exception message
	 */
	public TechnicalException(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            exception message
	 * @param args
	 *            : Arguments
	 */
	public TechnicalException(final String message, final Object... args) {
		super(message, args);
	}

	/**
	 * @param message
	 *            exception message
	 * @param cause
	 *            : mother cause of exception
	 */
	public TechnicalException(final String message, Throwable cause) {
		super(message, cause);
	}
}
