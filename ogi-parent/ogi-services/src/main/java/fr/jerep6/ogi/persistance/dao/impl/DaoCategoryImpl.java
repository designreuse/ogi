package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.dao.DaoCategory;

@Repository("daoCategory")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoCategoryImpl extends AbstractDao<Category, Integer> implements DaoCategory {
	Logger						LOGGER		= LoggerFactory.getLogger(DaoCategoryImpl.class);

	private static final String	PARAM_CODE	= "CODE";

	@Override
	public Category readByCode(EnumCategory enumCategory) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT c FROM " + Category.class.getName() + " c");
		q.append(" WHERE c.code =:" + PARAM_CODE);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_CODE, enumCategory);

		return (Category) query.getSingleResult();
	}

	@Override
	public List<Equipment> readEquipments(EnumCategory category) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT e FROM " + Equipment.class.getName() + " e");
		q.append(" WHERE e.category.code = :" + PARAM_CODE);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_CODE, category);

		@SuppressWarnings("unchecked")
		List<Equipment> eqpts = query.getResultList();
		return eqpts;
	}
}
