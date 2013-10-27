package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.enumeration.EnumLabelType;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.persistance.dao.DaoLabel;

@Repository("daoLabel")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoLabelImpl extends AbstractDao<Label, Integer> implements DaoLabel {
	Logger						LOGGER		= LoggerFactory.getLogger(DaoLabelImpl.class);

	private static final String	PARAM_LABEL	= "LABEL";
	private static final String	PARAM_TYPE	= "TYPE";

	@Override
	public Label read(EnumLabelType type, String label) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT l FROM " + Label.class.getName() + " l");
		q.append(" WHERE l.type = :" + PARAM_TYPE);
		q.append(" AND l.label = :" + PARAM_LABEL);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_TYPE, type);
		query.setParameter(PARAM_LABEL, label);

		@SuppressWarnings("unchecked")
		List<Label> labels = query.getResultList();
		// Return null if no result
		return Iterables.getFirst(labels, null);
	}

	@Override
	public List<Label> readByType(EnumLabelType type) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT l FROM " + Label.class.getName() + " l");
		q.append(" WHERE l.type = :" + PARAM_LABEL);
		q.append(" ORDER BY l.label ASC");

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_LABEL, type);

		@SuppressWarnings("unchecked")
		List<Label> labels = query.getResultList();
		return labels;
	}
}
