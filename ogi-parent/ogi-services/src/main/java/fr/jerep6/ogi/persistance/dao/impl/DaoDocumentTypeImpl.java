package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.DocumentType;
import fr.jerep6.ogi.persistance.dao.DaoDocumentType;

@Repository("daoDocumentType")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoDocumentTypeImpl extends AbstractDao<DocumentType, Integer> implements DaoDocumentType {
	private static Logger		LOGGER		= LoggerFactory.getLogger(DaoDocumentTypeImpl.class);
	private static final String	PARAM_ZONE	= "ZONELIST";
	private static final String	PARAM_CODE	= "CODE";

	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentType> listDocumentType(EnumDocumentZoneList zone) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT d FROM " + DocumentType.class.getName() + " d");
		if (zone != null) {
			q.append(" WHERE d.zoneList = :" + PARAM_ZONE);
		}

		Query query = entityManager.createQuery(q.toString());
		if (zone != null) {
			query.setParameter(PARAM_ZONE, zone);
		}
		return query.getResultList();
	}

	@Override
	public Optional<DocumentType> readByCode(String code) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT d FROM " + DocumentType.class.getName() + " d");
		q.append(" WHERE d.code = :" + PARAM_CODE);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_CODE, code);

		@SuppressWarnings("unchecked")
		List<DocumentType> types = query.getResultList();
		return Optional.ofNullable(Iterables.getFirst(types, null));
	}
}
