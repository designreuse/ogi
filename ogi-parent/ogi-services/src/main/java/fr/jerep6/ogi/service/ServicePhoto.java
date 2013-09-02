package fr.jerep6.ogi.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Photo;
import fr.jerep6.ogi.transfert.FileUpload;

public interface ServicePhoto extends TransactionalService<Photo, Integer> {

	/**
	 * Get photos of property with reference as parameter
	 * 
	 * @param reference
	 *            reference of property
	 * @return
	 */
	List<Photo> getPhotos(String reference);

	/**
	 * Copy inputstream into photo directory
	 * 
	 * @param is
	 *            phoyo file inputstream
	 * @param fileName
	 *            name of photo
	 * @param reference
	 *            reference of property to create directory
	 * @return
	 * @throws IOException
	 */
	FileUpload copyToPhotosDirectory(InputStream is, String fileName, String reference) throws IOException;

}
