package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Photo;
import fr.jerep6.ogi.persistance.dao.DaoPhoto;

@Repository("daoPhoto")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoPhotoImpl extends AbstractDao<Photo, Integer> implements DaoPhoto {
	Logger						LOGGER			= LoggerFactory.getLogger(DaoPhotoImpl.class);

	private static final String	PARAM_REFERENCE	= "REFERENCE";

	@Override
	public List<Photo> getPhotos(String reference) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT p FROM " + Photo.class.getName() + " p");
		q.append(" WHERE p.property.reference = :" + PARAM_REFERENCE);

		Query query = entityManager.createQuery(q.toString());
		query.setParameter(PARAM_REFERENCE, reference);

		@SuppressWarnings("unchecked")
		List<Photo> photos = query.getResultList();
		return photos;
	}
}
