package fr.jerep6.ogi.persistance.dao.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import fr.jerep6.ogi.enumeration.EnumSortByDirection;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Book;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.transfert.database.CustomObj;

@Repository("daoProperty")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoPropertyImpl extends AbstractDao<RealProperty, Integer> implements DaoProperty {
	Logger								LOGGER			= LoggerFactory.getLogger(DaoPropertyImpl.class);

	private static final String			PARAM_REFERENCE	= "REFERENCE";
	private static final String			PARAM_TECHID	= "TECHID";

	private static Map<String, String>	allowSortBy		= new HashMap<String, String>();
	static {
		allowSortBy.put("reference", "r.reference");
		allowSortBy.put("type", "categ.label");
		allowSortBy.put("city", "addr.city");
	}

	@Override
	public Long getReference() {
		StringBuilder q = new StringBuilder();
		q.append("SELECT nextval('SEQ_PROPERTY')");

		Query query = entityManager.createNativeQuery(q.toString());

		BigInteger ref = Iterables.getFirst(query.getResultList(), new BigInteger("1"));

		return ref.longValue();
	}

	@Override
	public List<RealProperty> list(Integer pageNumber, Integer itemNumberPerPage, String sortBy,
			EnumSortByDirection sortDir) {
		Preconditions.checkNotNull(pageNumber);
		Preconditions.checkNotNull(itemNumberPerPage);
		Preconditions.checkNotNull(sortDir);

		StringBuilder q = new StringBuilder();
		q.append("SELECT distinct r ").append(" FROM ").append(RealProperty.class.getName()).append(" r ");
		// I don't want a select for each many to one : prefer join
		q.append(" LEFT JOIN fetch r.address addr");
		q.append(" LEFT JOIN fetch r.category categ");
		q.append(" LEFT JOIN fetch r.type");
		q.append(" LEFT JOIN fetch r.descriptions");
		q.append(" LEFT JOIN fetch r.documents");
		q.append(" LEFT JOIN fetch r.sale");
		q.append(" LEFT JOIN fetch r.rent");
		q.append(" LEFT JOIN fetch r.state");
		// Don't fetch room because it's a list. Hibernate ajoute dans la liste des
		// rooms à chaque fois qu'une jointure dupliquante est faite (documents, descriptions ...). Si le bien a 2
		// documents et 3 pièces, alors la liste contiendra 2 * 3 pièces
		// q.append(" LEFT JOIN fetch r.rooms");
		q.append(" LEFT JOIN fetch r.equipments");
		q.append(" LEFT JOIN fetch r.owners");
		q.append(" LEFT JOIN fetch r.diagnosisProperty");

		// Check that param sortBy is allow
		String sanitizedSortBy = allowSortBy.get(sortBy);
		if (sanitizedSortBy != null) {
			q.append(" ORDER BY " + sanitizedSortBy + " " + sortDir.getCode());
		}

		TypedQuery<RealProperty> query = entityManager.createQuery(q.toString(), RealProperty.class);
		query.setFirstResult(pageNumber * itemNumberPerPage - itemNumberPerPage);
		query.setMaxResults(itemNumberPerPage);

		return query.getResultList();
	}

	@Override
	public List<RealProperty> readByReference(List<String> references) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT distinct r ").append(" FROM ").append(RealProperty.class.getName()).append(" r ");
		// I don't want a select for each many to one : prefer join
		q.append(" LEFT JOIN fetch r.address");
		q.append(" LEFT JOIN fetch r.category");
		q.append(" LEFT JOIN fetch r.type");
		q.append(" LEFT JOIN fetch r.descriptions");
		q.append(" LEFT JOIN fetch r.documents");
		q.append(" LEFT JOIN fetch r.sale");
		q.append(" LEFT JOIN fetch r.rent");
		q.append(" LEFT JOIN fetch r.state");
		// Don't fetch room because it's a list. Hibernate ajoute dans la liste des rooms à chaque fois quu'ne jointure
		// dupliquante est faite (documents, descriptions ...). Si le bien a 2 documents et 3 pièces, alors la liste
		// contiendra 2 * 3 pièces
		// q.append(" LEFT JOIN fetch r.rooms");
		q.append(" LEFT JOIN fetch r.equipments");
		q.append(" LEFT JOIN fetch r.owners");
		q.append(" LEFT JOIN fetch r.diagnosisProperty");
		q.append(" WHERE r.reference IN (:").append(PARAM_REFERENCE).append(")");

		TypedQuery<RealProperty> query = entityManager.createQuery(q.toString(), RealProperty.class);
		query.setParameter(PARAM_REFERENCE, references);

		return query.getResultList();
	}

	@Override
	public List<Object[]> readReferences(List<Integer> techid) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT techid, reference FROM " + RealProperty.class.getName() + " r");
		q.append(" WHERE r.techid IN ( :" + PARAM_TECHID + ")");

		TypedQuery<Object[]> query = entityManager.createQuery(q.toString(), Object[].class);
		query.setParameter(PARAM_TECHID, techid);

		return query.getResultList();
	}

	@Override
	public List<Object[]> readTechids(List<String> references) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT techid, reference FROM " + RealProperty.class.getName() + " r");
		q.append(" WHERE r.reference IN ( :" + PARAM_REFERENCE + ")");

		TypedQuery<Object[]> query = entityManager.createQuery(q.toString(), Object[].class);
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
