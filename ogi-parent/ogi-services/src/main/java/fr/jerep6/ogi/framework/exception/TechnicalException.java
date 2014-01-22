package fr.jerep6.ogi.framework.exception;

public class TechnicalException extends AbstractException {
	private static final long	serialVersionUID	= 1L;

	protected String			code				= "";

	public TechnicalException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		code = errorCode.getCode();
	}

	public TechnicalException(ErrorCode errorCode, Object... args) {
		super(errorCode.getMessage(), args);
		code = errorCode.getCode();
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

	public TechnicalException(String code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * @param message
	 *            exception message
	 * @param cause
	 *            : mother cause of exception
	 */
	public TechnicalException(String code, final String message, Throwable cause) {
		super(message, cause);
	}

	public String getCode() {
		return code;
	}
}
