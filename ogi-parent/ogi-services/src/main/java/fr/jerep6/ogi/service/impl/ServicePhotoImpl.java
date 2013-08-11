package fr.jerep6.ogi.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Photo;
import fr.jerep6.ogi.persistance.dao.DaoPhoto;
import fr.jerep6.ogi.service.ServicePhoto;

@Service("servicePhoto")
@Transactional(propagation = Propagation.REQUIRED)
public class ServicePhotoImpl extends AbstractTransactionalService<Photo, Integer> implements ServicePhoto {

	@Autowired
	private DaoPhoto	daoPhoto;

	@Override
	public List<Photo> getPhotos(String reference) {
		List<Photo> photos = daoPhoto.getPhotos(reference);
		return photos;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoPhoto);
	}

}
