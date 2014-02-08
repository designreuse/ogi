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
	public Integer getMax() {
		StringBuilder q = new StringBuilder();
		q.append("SELECT max(techid) FROM ").append(RealProperty.class.getName());

		TypedQuery<Integer> query = entityManager.createQuery(q.toString(), Integer.class);

		return Iterables.getFirst(query.getResultList(), 0);

	}

	@Override
	public List<RealProperty> readByReference(List<String> references) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT r ").append(" FROM ").append(RealProperty.class.getName()).append(" r ");
		// I don't want a select for each many to one : prefer join
		q.append(" LEFT JOIN fetch r.address");
		q.append(" LEFT JOIN fetch r.category");
		q.append(" LEFT JOIN fetch r.type");
		q.append(" LEFT JOIN fetch r.descriptions");
		q.append(" LEFT JOIN fetch r.documents");
		q.append(" LEFT JOIN fetch r.rooms");
		q.append(" LEFT JOIN fetch r.equipments");
		q.append(" WHERE r.reference IN (:").append(PARAM_REFERENCE).append(")");

		TypedQuery<RealProperty> query = entityManager.createQuery(q.toString(), RealProperty.class);
		query.setParameter(PARAM_REFERENCE, references);

		return query.getResultList();
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
