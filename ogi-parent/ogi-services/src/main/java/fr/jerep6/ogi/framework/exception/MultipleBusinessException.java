package fr.jerep6.ogi.framework.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * 
 * @author jerep6 11 janv. 2014
 */
public class MultipleBusinessException extends BusinessException implements Iterable<BusinessException> {
	private static final long		serialVersionUID	= 1L;

	private List<BusinessException>	exceptions			= new ArrayList<>();

	public MultipleBusinessException() {
		super();
	}

	public void add(ErrorCode errorCode) {
		Preconditions.checkNotNull(errorCode);
		exceptions.add(new BusinessException(errorCode));
	}

	public void checkErrors() {
		if (!exceptions.isEmpty()) {
			throw this;
		}
	}

	public List<BusinessException> getExceptions() {
		return exceptions;
	}

	@Override
	public Iterator<BusinessException> iterator() {
		return exceptions.iterator();
	}
}
