package fr.jerep6.ogi.exception.technical;

import fr.jerep6.ogi.framework.exception.TechnicalException;

public class JasperTechnicalException extends TechnicalException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	public JasperTechnicalException(final String message, Throwable cause) {
		super("JASP", message, cause);
	}

}
