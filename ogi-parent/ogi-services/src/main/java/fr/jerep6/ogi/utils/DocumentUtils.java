package fr.jerep6.ogi.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDocumentType;
import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.persistance.bo.DocumentType;

@Component
public class DocumentUtils {
	/**
	 * Return absolute path
	 *
	 * @param relativePath
	 * @return
	 */
	public static Path absolutize(Path relativePath) {
		return documentStorageDir.resolve(relativePath);
	}

	public static String buildUrl(Path absolutePath) {
		return buildUrl(absolutePath, null);
	}

	public static String buildUrl(Path absolutePath, String parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append(ContextUtils.threadLocalRequestURI.get());
		sb.append(documentsUrlContext);
		sb.append(documentStorageDir.relativize(absolutePath).toString().replace("\\", "/"));
		sb.append(parameters != null ? parameters : "");
		return sb.toString();
	}

	/**
	 * Return absolute path to property's directory
	 *
	 * @param reference
	 *            property's reference
	 * @return
	 */
	public static Path getDirectory(String reference) {
		return getDirectory(reference, null);
	}

	/**
	 * Return absolute path to property's directory corresponding to documentType
	 *
	 * @param reference
	 *            property's reference
	 * @param docType
	 *            type of document
	 * @return
	 */
	public static Path getDirectory(String reference, EnumDocumentType docType) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));

		String docPath = "";
		if (docType != null) {
			docPath = getDirectoryName(docType.getCode());
		}

		return documentStorageDir.resolve(Paths.get(reference)).resolve(Paths.get(docPath));
	}

	/**
	 * Return directory only name of a document type
	 *
	 * @param type
	 *            type of document
	 * @return
	 */
	public static String getDirectoryName(String documentTypeCode) {
		String dirName = dirNamesByType.get(documentTypeCode);
		return Objects.firstNonNull(dirName, DIR_MISC_NAME);
	}

	/**
	 * Return path to store temporary data of a property
	 *
	 * @param reference
	 *            property reference. May be a random string
	 * @param type
	 *            type of document. Type will be add to root
	 * @return
	 */
	public static Path getTempDirectory(String reference, String documentTypeCode) {
		return documentStorageDir.resolve(Paths.get(DIR_TMP, reference,
				DocumentUtils.getDirectoryName(documentTypeCode)));
	}

	/**
	 * Compute relative path from url
	 *
	 * @param url
	 *            url in which to extract the path
	 * @return
	 */
	public static Path relativePathFromUrl(String url) {
		if (Strings.isNullOrEmpty(url)) {
			return null;
		}

		StringBuilder ctx = new StringBuilder();
		ctx.append(ContextUtils.threadLocalRequestURI.get());
		ctx.append(documentsUrlContext);
		String relativePathUrl = url.replace(ctx.toString(), "");
		return Paths.get(relativePathUrl);
	}

	/**
	 * Return relative path from document root
	 *
	 * @param absolutePathToDocument
	 * @return
	 */
	public static Path relativize(Path absolutePathToDocument) {
		return documentStorageDir.relativize(absolutePathToDocument);
	}

	private static final Logger						LOGGER			= LoggerFactory.getLogger(DocumentUtils.class);

	// public variables
	public static String							DIR_PHOTO_NAME	= "photos";
	public static String							DIR_MISC_NAME	= "divers";
	public static String							DIR_TMP			= "temp";

	private static String							documentsUrlContext;
	private static Path								documentStorageDir;

	/**
	 * Chaque type de document est stocké dans un répertoire. Map de correspondance entre le type du document
	 * et le répertoire dans lequel il est stocké. La clé est le code du type de document
	 */
	private static Map<String, String>	dirNamesByType	= new HashMap<>(1);
	static {
		// populate map
		dirNamesByType.put(EnumDocumentType.PHOTO.getCode(), DIR_PHOTO_NAME);
	}

	@PostConstruct
	private void init() {}

	@Value("${document.url.context}")
	public void setDocumentsUrlContext(String urlCtx) {
		documentsUrlContext = urlCtx;
	}

	@Value("${document.storage.dir}")
	public void setPhotoDir(String photoDir) {
		DocumentUtils.documentStorageDir = Paths.get(photoDir);
	}
}
