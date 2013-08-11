package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

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
		// I don't want a select for each many to one : prefer join
		q.append(" JOIN fetch r.address");
		q.append(" JOIN fetch r.category");
		q.append(" JOIN fetch r.type");
		q.append(" JOIN fetch r.descriptions");
		q.append(" JOIN fetch r.photos");
		q.append(" JOIN fetch r.rooms");
		q.append(" JOIN fetch r.equipments");
		q.append(" WHERE r.reference= :").append(PARAM_REFERENCE);

		TypedQuery<RealProperty> query = entityManager.createQuery(q.toString(), RealProperty.class);
		query.setParameter(PARAM_REFERENCE, reference);

		return Iterables.getFirst(query.getResultList(), null);
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
