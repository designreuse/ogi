package fr.jerep6.ogi.exception.business;

import fr.jerep6.ogi.framework.exception.BusinessException;

public class RealPropertyNotFoundBusinessException extends BusinessException {
	private static final long	serialVersionUID	= 1L;

	public RealPropertyNotFoundBusinessException() {
		super();
	}

	public RealPropertyNotFoundBusinessException(Object... args) {
		super("RPNF01", "Realproperty not found", args);
	}

}
