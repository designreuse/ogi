package fr.jerep6.ogi.exception.technical;

import fr.jerep6.ogi.framework.exception.TechnicalException;

public class PartnerTechnicalException extends TechnicalException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	public PartnerTechnicalException(String msg, Object... args) {
		super(msg, args);
		code = "PARTNER_TECHNICAL_ERROR";
	}

}
