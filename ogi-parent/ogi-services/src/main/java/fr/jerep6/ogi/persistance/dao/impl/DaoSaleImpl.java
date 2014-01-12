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
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.dao.DaoSale;

@Repository("daoSale")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoSaleImpl extends AbstractDao<Sale, Integer> implements DaoSale {
	Logger						LOGGER				= LoggerFactory.getLogger(DaoSaleImpl.class);

	private static final String	PARAM_PRP_REFERENCE	= "PRP_REFERENCE";

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
