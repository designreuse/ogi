package fr.jerep6.ogi.framework.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * @author jerep6 11 janv. 2014
 */
public class MultipleTechnicalException extends AbstractException implements Iterable<TechnicalException> {
	private static final long				serialVersionUID	= 1L;

	private final List<TechnicalException>	exceptions			= new ArrayList<>();

	public MultipleTechnicalException() {
		super();
	}

	public void add(ErrorCode errorCode) {
		Preconditions.checkNotNull(errorCode);
		exceptions.add(new TechnicalException(errorCode));
	}

	public void checkErrors() {
		if (!exceptions.isEmpty()) {
			throw this;
		}
	}

	public List<TechnicalException> getExceptions() {
		return exceptions;
	}

	@Override
	public Iterator<TechnicalException> iterator() {
		return exceptions.iterator();
	}

}
