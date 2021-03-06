package fr.jerep6.ogi.framework.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.framework.service.TransactionalService;

@Transactional(propagation = Propagation.REQUIRED)
public abstract class AbstractTransactionalService<T, PK extends Serializable> extends AbstractService implements
		TransactionalService<T, PK> {
	@SuppressWarnings("unused")
	private final Class<T>	persistentClass;

	private DaoCRUD<T, PK>	dao;

	@SuppressWarnings("unchecked")
	public AbstractTransactionalService() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/**
	 * Subclass MUST override the init method to inject the dao in this super class
	 */
	@PostConstruct
	protected abstract void init();

	@Override
	public Collection<T> listAll() {
		return dao.listAll();
	}

	public List<T> merge(List<T> bos) {
		Preconditions.checkNotNull(bos);

		List<T> l = new ArrayList<>(bos.size());
		for (T t : bos) {
			l.add(merge(t));
		}

		return l;
	}

	@Override
	public T merge(T bo) {
		validate(bo);
		return dao.merge(bo);
	}

	@Override
	@Transactional(readOnly = true)
	public T read(PK id) {
		T bo = dao.read(id);
		return bo;
	}

	@Override
	public void remove(Collection<T> c) {
		for (T bo : c) {
			this.dao.remove(bo);
		}
	}

	@Override
	public void remove(T bo) {
		this.dao.remove(bo);
	}

	@Override
	public void removeByPrimaryKey(Collection<PK> c) {
		for (PK p : c) {
			this.dao.remove(p);
		}
	}

	@Override
	public T save(T bo) {
		validate(bo);
		return dao.save(bo);
	}

	public void setDao(DaoCRUD<T, PK> dao) {
		this.dao = dao;
	}

	@Override
	public T update(T bo) {
		validate(bo);
		return dao.update(bo);
	}

	@Override
	public void validate(T bo) throws BusinessException {

	}

}
