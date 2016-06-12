package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.bo.Visit;
import fr.jerep6.ogi.persistance.dao.DaoRent;
import fr.jerep6.ogi.persistance.dao.DaoVisit;

@Repository("daoVisit")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoVisitImpl extends AbstractDao<Visit, Integer> implements DaoVisit {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoVisitImpl.class);
	
	private static final String			PARAM_PRP		= "REFERENCE";

	@Override
	public List<Visit> readByProperty(String prpRef) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT v FROM " + Visit.class.getName() + " v");
		q.append(" JOIN v.property p");
		q.append(" WHERE p.reference = :" + PARAM_PRP);

		TypedQuery<Visit> query = entityManager.createQuery(q.toString(), Visit.class);
		query.setParameter(PARAM_PRP, prpRef);

		List<Visit> owners = query.getResultList();
		return owners;
	}
}
