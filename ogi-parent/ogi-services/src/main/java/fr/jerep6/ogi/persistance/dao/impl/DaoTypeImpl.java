package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.dao.DaoType;

@Repository("daoType")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoTypeImpl extends AbstractDao<Type, Integer> implements DaoType {
	Logger						LOGGER		= LoggerFactory.getLogger(DaoTypeImpl.class);

	private static final String	PARAM_LABEL	= "CODE";

	@Override
	public Type readByLabel(String label) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT t FROM " + Type.class.getName() + " t");
		q.append(" WHERE t.label = :" + PARAM_LABEL);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_LABEL, label);

		@SuppressWarnings("unchecked")
		List<Type> type = query.getResultList();

		// Return null if no result
		return type.isEmpty() ? null : type.get(0);
	}
}
