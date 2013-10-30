package fr.jerep6.ogi.service;

import java.io.IOException;
import java.io.InputStream;

import fr.jerep6.ogi.enumeration.EnumDocumentType;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.transfert.FileUpload;

public interface ServiceDocument extends TransactionalService<Document, Integer> {

	/**
	 * Copy inputstream into photo directory
	 * 
	 * @param is
	 *            phoyo file inputstream
	 * @param fileName
	 *            name of photo
	 * @param reference
	 *            reference of property to create directory
	 * @param type
	 *            type of document (PHOTO, MISC ...)
	 * @return
	 * @throws IOException
	 */
	FileUpload copyToDirectory(InputStream is, String fileName, String reference, EnumDocumentType type)
			throws IOException;

}
