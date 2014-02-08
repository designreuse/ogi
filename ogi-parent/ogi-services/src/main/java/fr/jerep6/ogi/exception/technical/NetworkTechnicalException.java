package fr.jerep6.ogi.exception.technical;

import fr.jerep6.ogi.framework.exception.ErrorCode;
import fr.jerep6.ogi.framework.exception.TechnicalException;

public class NetworkTechnicalException extends TechnicalException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	public NetworkTechnicalException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		code = errorCode.getCode();
	}

	public NetworkTechnicalException(ErrorCode errorCode, Object... args) {
		super(errorCode.getMessage(), args);
		code = errorCode.getCode();
	}

	public NetworkTechnicalException(Throwable cause) {
		super("NETW", "Network error", cause);
	}

}
