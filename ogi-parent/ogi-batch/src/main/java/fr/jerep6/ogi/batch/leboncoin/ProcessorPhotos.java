package fr.jerep6.ogi.batch.leboncoin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.Resource;

import fr.jerep6.ogi.persistance.bo.Document;

/**
 * Collect photos of property and place them into destination directory
 *
 * Photos are selected according to RealPropertyCSV photos fields
 *
 * @author jerep6 15 mars 2014
 */
public class ProcessorPhotos implements ItemProcessor<RealPropertyCSV, RealPropertyCSV> {
	private static Logger	LOGGER	= LoggerFactory.getLogger(ProcessorPhotos.class);

	private Resource		rootDirectory;
	private String			photoDirName;

	private Path			absPhotosDirectory;

	private void copyPhoto(Document photo, String photoName) throws IOException {

		// LeBonCoin require photo on archive root. Have to rename photo to avoid collision file into OGI directory
		// Folder into copy photos
		try {
			Files.copy(photo.getAbsolutePath(), absPhotosDirectory.resolve(photoName),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (NoSuchFileException nsfe) {
			LOGGER.warn("Error coping file", nsfe);
		}
	}

	// Spring call this method
	private void init() throws IOException {
		absPhotosDirectory = rootDirectory.getFile().toPath().resolve(Paths.get(photoDirName));
	}

	@Override
	public RealPropertyCSV process(RealPropertyCSV item) throws Exception {

		// Copy all available photos in RealPropertyCSV into output directory
		for (int i = 0; i < item.getPhotos().size(); i++) {
			copyPhoto(item.getPhotos().get(i), item.getPhotosName().get(i));
		}

		return item;
	}

	public void setPhotoDirName(String photoDirName) {
		this.photoDirName = photoDirName;
	}

	public void setRootDirectory(Resource rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

}