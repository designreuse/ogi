package fr.jerep6.ogi.framework.exception;

public class BusinessException extends AbstractException {
	private static final long	serialVersionUID	= 1L;

	private String				code				= "";

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		code = errorCode.getCode();
	}

	public BusinessException(ErrorCode errorCode, Object... args) {
		this(errorCode.getCode(), errorCode.getMessage(), args);
	}

	/**
	 * @param message
	 *            exception message
	 */
	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * 
	 * @param message
	 *            exception message
	 * @param args
	 *            : Arguments
	 */
	public BusinessException(String code, String message, Object... args) {
		super(message, args);
		this.code = code;
	}

	/**
	 * @param message
	 *            exception message
	 * @param cause
	 *            : mother causse of exception
	 */
	public BusinessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
