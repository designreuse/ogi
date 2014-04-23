package fr.jerep6.ogi.persistance.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.dao.DaoOwner;

@Repository("daoOwner")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoOwnerImpl extends AbstractDao<Owner, Integer> implements DaoOwner {
	Logger						LOGGER			= LoggerFactory.getLogger(DaoOwnerImpl.class);

	private static final String	PARAM_PRP		= "LABEL";
	private static final String	PARAM_TECHID	= "TECHID";

	@Override
	public List<Owner> read(Collection<Integer> techids) {
		Preconditions.checkNotNull(techids);
		if (techids.isEmpty()) {
			return Collections.emptyList();
		}

		StringBuilder q = new StringBuilder();
		q.append("SELECT o FROM " + Owner.class.getName() + " o");
		q.append(" WHERE o.techid IN(:" + PARAM_TECHID + ")");

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_TECHID, techids);

		@SuppressWarnings("unchecked")
		List<Owner> owners = query.getResultList();
		return owners;
	}

	@Override
	public List<Owner> readByProperty(String prpRef) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT o FROM " + Owner.class.getName() + " o");
		q.append(" JOIN o.properties p");
		q.append(" WHERE p.reference = :" + PARAM_PRP);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_PRP, prpRef);

		@SuppressWarnings("unchecked")
		List<Owner> owners = query.getResultList();
		return owners;
	}
}
