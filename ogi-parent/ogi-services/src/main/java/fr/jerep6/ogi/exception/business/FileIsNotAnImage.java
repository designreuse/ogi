package fr.jerep6.ogi.exception.business;

import java.nio.file.Path;

import fr.jerep6.ogi.framework.exception.BusinessException;

public class FileIsNotAnImage extends BusinessException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	public FileIsNotAnImage(Path p) {
		this(new Object[]{p});
	}

	public FileIsNotAnImage(Object... args) {
		super("FILE02", "File is not an image", args);
	}

}
