package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.DocumentType;
import fr.jerep6.ogi.persistance.dao.DaoDocumentType;

@Repository("daoDocumentType")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoDocumentTypeImpl extends AbstractDao<DocumentType, Integer> implements DaoDocumentType {
	private static final String	PARAM_ZONE	= "ZONELIST";
	Logger						LOGGER		= LoggerFactory.getLogger(DaoDocumentTypeImpl.class);

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
}
