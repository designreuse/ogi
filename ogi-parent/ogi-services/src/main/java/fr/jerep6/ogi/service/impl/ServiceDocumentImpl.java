package fr.jerep6.ogi.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDocumentType;
import fr.jerep6.ogi.exception.business.FileAlreadyExist;
import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.obj.PhotoDimension;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.dao.DaoDocument;
import fr.jerep6.ogi.service.ServiceDocument;
import fr.jerep6.ogi.transfert.FileUpload;
import fr.jerep6.ogi.utils.DocumentUtils;

@Service("serviceDocument")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDocumentImpl extends AbstractTransactionalService<Document, Integer> implements ServiceDocument {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceDocumentImpl.class);

	@Autowired
	private DaoDocument		daoDocument;

	@Override
	public Set<Document> copyTempToDirectory(Collection<Document> documents, String reference) {
		Preconditions.checkNotNull(documents);
		Preconditions.checkNotNull(reference);

		Set<Document> documentOK = new HashSet<>(documents.size());

		// Determine absolute path of reference
		Path root = DocumentUtils.getDirectory(reference);

		// Create root directory if not exist
		if (!Files.isDirectory(root)) {
			try {
				LOGGER.info("Create directory {}", root);
				Files.createDirectories(root);
			} catch (IOException ioe) {
				LOGGER.error("Error creating directory from property " + reference, ioe);
				throw new TechnicalException();
			}
		}

		for (Document aDoc : documents) {
			Path relativeDocPath = Paths.get(aDoc.getPath());
			// If document is in temp folder => move it into property document
			if (relativeDocPath.startsWith(DocumentUtils.DIR_TMP)) {
				Path absoluteDestinationFile = root.resolve(Paths.get(DocumentUtils.getDirectoryName(aDoc.getType()),
						relativeDocPath.getFileName().toString()));

				try {
					// Create parent directory of file (photo for example)
					Files.createDirectories(absoluteDestinationFile.getParent());

					// Copy because it may be replay if error
					Files.copy(//
							DocumentUtils.absolutize(relativeDocPath), //
							absoluteDestinationFile, //
							StandardCopyOption.REPLACE_EXISTING);

					// Add current document to list success list
					documentOK.add(aDoc);
				} catch (IOException ioe) {
					LOGGER.error("Error coping temp file to prp directory", ioe);
				}
			}

		}
		return documentOK;

	}

	@Override
	public FileUpload copyToDirectory(InputStream is, String fileName, String reference, EnumDocumentType type)
			throws IOException {
		Preconditions.checkNotNull(is);
		Preconditions.checkNotNull(type);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(fileName));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));

		// Temp directory for photos
		Path root = DocumentUtils.getTempDirectory(reference, type);

		// Create temp directory if not exist
		if (!Files.isDirectory(root)) {
			LOGGER.info("Create directory {}", root);
			Files.createDirectories(root);
		}

		Path doc = root.resolve(fileName);

		// If file exist => exception
		if (Files.exists(doc)) {
			throw new FileAlreadyExist(doc.getFileName().toString());
		}

		// Copy file into temp directory
		Files.copy(is, doc);

		FileUpload f = new FileUpload.Builder() //
				.name(fileName) //
				.size(Files.size(doc)) //
				.type(Files.probeContentType(doc)) //
				.url(DocumentUtils.buildUrl(doc)) //
				.thumbnailUrl(DocumentUtils.buildUrl(doc, PhotoDimension.THUMB)) //
				.deleteUrl(DocumentUtils.buildUrl(doc)) //
				.deleteType("DELETE") //
				.document(new Document(DocumentUtils.relativize(doc).toString(), fileName, 1, type)) //
				.build();

		return f;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDocument);

	}

}
