package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.dao.DaoType;

@Repository("daoType")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoTypeImpl extends AbstractDao<Type, Integer> implements DaoType {
	Logger						LOGGER			= LoggerFactory.getLogger(DaoTypeImpl.class);

	private static final String	PARAM_LABEL		= "LABEL";
	private static final String	PARAM_CATEGORIE	= "CATEGORIE";

	@Override
	public List<Type> readByCategory(EnumCategory category) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT t FROM " + Type.class.getName() + " t");
		q.append(" WHERE t.category.code = :" + PARAM_CATEGORIE);
		q.append(" ORDER BY t.label ASC");

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_CATEGORIE, category);

		@SuppressWarnings("unchecked")
		List<Type> types = query.getResultList();
		return types;
	}

	@Override
	public Type readByLabel(String label) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT t FROM " + Type.class.getName() + " t");
		q.append(" WHERE t.label = :" + PARAM_LABEL);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_LABEL, label);

		@SuppressWarnings("unchecked")
		List<Type> types = query.getResultList();

		// Return null if no result
		return Iterables.getFirst(types, null);
	}
}
