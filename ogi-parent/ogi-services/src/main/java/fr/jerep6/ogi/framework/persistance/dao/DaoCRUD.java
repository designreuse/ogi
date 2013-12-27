package fr.jerep6.ogi.framework.persistance.dao;

import java.io.Serializable;
import java.util.Collection;

public interface DaoCRUD<T, PK extends Serializable> {
	T read(PK id);

	T save(T bo);

	/**
	 * Update the entity. The return will managed. It is a new instance of bo
	 * 
	 * @param bo
	 *            business object to managed
	 * @return
	 */
	T update(T bo);

	Collection<T> listAll();

	void remove(T bo);

	void remove(PK pk);

	T merge(T bo);

}
