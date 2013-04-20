package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Book;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.transfert.database.CustomObj;

@Repository("daoProperty")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoPropertyImpl extends AbstractDao<RealProperty, Integer> implements DaoProperty {
	Logger						LOGGER			= LoggerFactory.getLogger(DaoPropertyImpl.class);

	private static final String	PARAM_REFERENCE	= "REFERENCE";

	@Override
	public RealProperty readByReference(String reference) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT r ").append(" FROM ").append(RealProperty.class.getName()).append(" r ");
		q.append(" WHERE r.reference= :").append(PARAM_REFERENCE);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_REFERENCE, reference);

		return (RealProperty) query.getSingleResult();
	}

	@Override
	public void test() {
		StringBuilder q = new StringBuilder();
		q.append("SELECT new " + CustomObj.class.getName() + "(b.techid, b.authors)");
		q.append(" FROM Book b");
		// q.append(" JOIN b.authors as aut");
		q.append(" WHERE b.techid=1");

		Query query = entityManager.createQuery(q.toString());
		List<Book> results = query.getResultList();
		System.out.println(results);

	}
}
