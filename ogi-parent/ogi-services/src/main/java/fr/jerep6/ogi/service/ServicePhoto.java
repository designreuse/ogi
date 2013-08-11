package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Photo;

public interface ServicePhoto extends TransactionalService<Photo, Integer> {

	/**
	 * Get photos of property with reference as parameter
	 * 
	 * @param reference
	 *            reference of property
	 * @return
	 */
	List<Photo> getPhotos(String reference);
}
