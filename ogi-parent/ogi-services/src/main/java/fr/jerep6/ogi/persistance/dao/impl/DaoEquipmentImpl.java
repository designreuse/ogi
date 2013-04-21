package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.dao.DaoEquipment;

@Repository("daoEquipment")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoEquipmentImpl extends AbstractDao<Equipment, Integer> implements DaoEquipment {
	Logger						LOGGER			= LoggerFactory.getLogger(DaoEquipmentImpl.class);

	private static final String	PARAM_LABEL		= "LABEL";
	private static final String	PARAM_CATEGORY	= "CATEGORY";

	@Override
	public Equipment readByLabel(String label, EnumCategory category) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT e FROM " + Equipment.class.getName() + " e");
		q.append(" WHERE e.label = :" + PARAM_LABEL);
		q.append(" AND e.category.code = :" + PARAM_CATEGORY);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_LABEL, label);
		query.setParameter(PARAM_CATEGORY, category);

		@SuppressWarnings("unchecked")
		List<Equipment> eqpts = query.getResultList();

		// Return null if no result
		return Iterables.getFirst(eqpts, null);
	}
}
