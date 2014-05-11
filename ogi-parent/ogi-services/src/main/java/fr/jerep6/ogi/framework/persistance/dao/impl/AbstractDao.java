package fr.jerep6.ogi.framework.persistance.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;

public class AbstractDao<T, PK extends Serializable> implements DaoCRUD<T, PK> {

	private final Class<T>	persistentClass;

	@PersistenceContext
	protected EntityManager	entityManager;

	@SuppressWarnings("unchecked")
	public AbstractDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@Override
	public void flush() {
		entityManager.flush();
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<T> listAll() {
		Query query = entityManager.createQuery("SELECT t FROM " + persistentClass.getName() + " t");
		return query.getResultList();
	}

	@Override
	public T merge(T bo) {
		entityManager.refresh(bo);
		return bo;
	}

	@Override
	public T read(PK id) {
		return entityManager.find(persistentClass, id);
	}

	@Override
	public void remove(PK pk) {
		T bo = read(pk);
		remove(bo);
	}

	@Override
	public void remove(T bo) {
		entityManager.remove(bo);
	}

	@Override
	public T save(T bo) {
		entityManager.persist(bo);
		return bo;
	}

	@Override
	public T update(T bo) {
		return entityManager.merge(bo);
	}
}
