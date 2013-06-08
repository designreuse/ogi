package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Diagnosis;
import fr.jerep6.ogi.persistance.dao.DaoDiagnosis;

@Repository("daoDiagnosis")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoDiagnosisImpl extends AbstractDao<Diagnosis, Integer> implements DaoDiagnosis {
	Logger						LOGGER		= LoggerFactory.getLogger(DaoDiagnosisImpl.class);

	private static final String	PARAM_LABEL	= "LABEL";

	@Override
	public Diagnosis readByLabel(String label) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT d FROM " + Diagnosis.class.getName() + " d");
		q.append(" WHERE d.label = :" + PARAM_LABEL);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_LABEL, label);

		@SuppressWarnings("unchecked")
		List<Diagnosis> types = query.getResultList();

		// Return null if no result
		return Iterables.getFirst(types, null);
	}
}
