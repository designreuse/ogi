package fr.jerep6.ogi.persistance.dao.impl;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.dao.DaoSale;
import fr.jerep6.ogi.transfert.ExpiredMandate;

@Repository("daoSale")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoSaleImpl extends AbstractDao<Sale, Integer> implements DaoSale {
	Logger						LOGGER					= LoggerFactory.getLogger(DaoSaleImpl.class);

	private static final String	PARAM_PRP_REFERENCE		= "PRP_REFERENCE";
	private static final String	PARAM_MANDAT_DATE_BEGIN	= "MANDAT_DATE_BEGIN";
	private static final String	PARAM_MANDAT_DATE_END	= "MANDAT_DATE_END";
	private static final String	PARAM_SOLD				= "SOLD";

	@SuppressWarnings("unchecked")
	@Override
	public List<ExpiredMandate> listMandats(Optional<ZonedDateTime> begin, Optional<ZonedDateTime> end) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT new " + ExpiredMandate.class.getName()
				+ "('SALE', s.property.reference, s.mandateReference, s.mandateEndDate) ");
		q.append(" FROM " + Sale.class.getName() + " s ");
		q.append(" WHERE 1=1 ");
		q.append(" AND s.sold= :" + PARAM_SOLD);

		if (begin.isPresent()) {
			q.append(" AND mandateEndDate > :" + PARAM_MANDAT_DATE_BEGIN);
		}
		if (end.isPresent()) {
			q.append(" AND mandateEndDate < :" + PARAM_MANDAT_DATE_END);
		}
		q.append(" ORDER BY mandateEndDate ASC");

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_SOLD, false);
		if (begin.isPresent()) {
			query.setParameter(PARAM_MANDAT_DATE_BEGIN, GregorianCalendar.from(begin.get()));
		}
		if (end.isPresent()) {
			query.setParameter(PARAM_MANDAT_DATE_END, GregorianCalendar.from(end.get()));
		}

		return query.getResultList();
	}

	@Override
	public Sale readFromPropertyReference(String prpReference) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT s FROM " + Sale.class.getName() + " s");
		q.append(" WHERE s.property.reference = :" + PARAM_PRP_REFERENCE);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_PRP_REFERENCE, prpReference);

		@SuppressWarnings("unchecked")
		List<Sale> sales = query.getResultList();

		// Return null if no result
		return Iterables.getFirst(sales, null);
	}
}
