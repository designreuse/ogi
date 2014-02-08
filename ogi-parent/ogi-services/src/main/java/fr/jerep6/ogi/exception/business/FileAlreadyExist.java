package fr.jerep6.ogi.exception.business;

import fr.jerep6.ogi.framework.exception.BusinessException;

public class FileAlreadyExist extends BusinessException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	public FileAlreadyExist() {
		this(null);
	}

	public FileAlreadyExist(Object... args) {
		super("FAE01", "File already exist", args);
	}

}
