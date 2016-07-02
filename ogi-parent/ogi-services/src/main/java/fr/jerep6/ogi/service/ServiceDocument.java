package fr.jerep6.ogi.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.jerep6.ogi.enumeration.EnumGestionMode;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.DocumentType;
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
	 *            identifiant of document type 1 for photo
	 * @return
	 * @throws IOException
	 */
	FileUpload copyToDirectory(InputStream is, String fileName, String reference, String type) throws IOException;

	/**
	 * Copy document from temp directory into property directory
	 *
	 * @param documents
	 *            list of document. Copy only documents stored into temp folder
	 * @param reference
	 *            reference of property
	 */
	Set<Document> copyTempToDirectory(Collection<Document> documents, String reference);

	/**
	 * Delete documents into filesystem
	 *
	 * @param documents
	 *            documents to delete
	 * @return success deletion
	 */
	Set<Document> deleteDocuments(Collection<Document> documents);

	Set<Document> merge(String prpReference, Set<Document> documentsBD, Set<Document> documentsModif);

	List<DocumentType> listDocumentType(EnumGestionMode zone);

}
