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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumDocumentType;
import fr.jerep6.ogi.obj.PhotoDimension;

@Component
public class DocumentUtils {
	private static final Logger						LOGGER			= LoggerFactory.getLogger(DocumentUtils.class);

	// public variables
	public static String							DIR_PHOTO_NAME	= "photos";
	public static String							DIR_MISC_NAME	= "divers";
	public static String							DIR_TMP			= "temp";

	private static String							urlBaseDocuments;
	private static Path								documentStorageDir;

	private static Map<EnumDocumentType, String>	dirNamesByType	= new HashMap<>(2);
	static {
		// populate map
		dirNamesByType.put(EnumDocumentType.PHOTO, DIR_PHOTO_NAME);
		dirNamesByType.put(EnumDocumentType.MISC, DIR_MISC_NAME);
	}

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

	public static String buildUrl(Path absolutePath, PhotoDimension dimension) {
		String size = "";
		if (dimension != null) {
			size = "?size=" + dimension.getName();
		}
		return urlBaseDocuments + documentStorageDir.relativize(absolutePath).toString().replace("\\", "/") + size;
	}

	/**
	 * Return absolute path to property's directory
	 * 
	 * @param reference
	 *            property's reference
	 * @return
	 */
	public static Path getDirectory(String reference) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));
		return documentStorageDir.resolve(Paths.get(reference));
	}

	/**
	 * Return directory only name of a document type
	 * 
	 * @param type
	 *            type of document
	 * @return
	 */
	public static String getDirectoryName(EnumDocumentType type) {
		return dirNamesByType.get(type);
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
	public static Path getTempDirectory(String reference, EnumDocumentType type) {
		return documentStorageDir.resolve(Paths.get(DIR_TMP, reference, DocumentUtils.getDirectoryName(type)));
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
		String relativePathUrl = url.replace(urlBaseDocuments, "");
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

	@PostConstruct
	private void init() {}

	@Value("${document.storage.dir}")
	public void setPhotoDir(String photoDir) {
		DocumentUtils.documentStorageDir = Paths.get(photoDir);
	}

	@Value("${photos.url}")
	public void setUrlBasePhoto(String urlBasePhoto) {
		urlBaseDocuments = urlBasePhoto;
	}
}
