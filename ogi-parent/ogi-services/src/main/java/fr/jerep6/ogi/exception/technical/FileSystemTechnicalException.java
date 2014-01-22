package fr.jerep6.ogi.exception.technical;

import fr.jerep6.ogi.framework.exception.TechnicalException;

public class FileSystemTechnicalException extends TechnicalException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	public FileSystemTechnicalException(final String message, Throwable cause, Object... args) {
		super("FIL", message, cause, args);
	}

}
