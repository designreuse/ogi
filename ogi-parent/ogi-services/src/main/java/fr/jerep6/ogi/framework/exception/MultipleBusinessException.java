package fr.jerep6.ogi.framework.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;

/**
 * 
 * @author jerep6 11 janv. 2014
 */
public class MultipleBusinessException extends AbstractException implements Iterable<BusinessException> {
	private static final long				serialVersionUID	= 1L;

	private final List<BusinessException>	exceptions			= new ArrayList<>();

	public MultipleBusinessException() {
		super();
	}

	public void add(ErrorCode errorCode) {
		Preconditions.checkNotNull(errorCode);
		exceptions.add(new BusinessException(errorCode));
	}

	public void add(ErrorCode errorCode, Object... params) {
		Preconditions.checkNotNull(errorCode);
		exceptions.add(new BusinessException(errorCode, params));
	}

	public void checkErrors() {
		if (!exceptions.isEmpty()) {
			throw this;
		}
	}

	@Override
	public void forEach(Consumer<? super BusinessException> action) {
		exceptions.forEach(action);
	}

	public List<BusinessException> getExceptions() {
		return exceptions;
	}

	@Override
	public Iterator<BusinessException> iterator() {
		return exceptions.iterator();
	}

	@Override
	public Spliterator<BusinessException> spliterator() {
		return exceptions.spliterator();
	}

}
