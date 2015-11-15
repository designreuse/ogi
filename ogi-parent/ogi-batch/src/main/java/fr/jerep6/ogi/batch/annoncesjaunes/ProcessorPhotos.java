package fr.jerep6.ogi.batch.annoncesjaunes;

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

import fr.jerep6.ogi.utils.DocumentUtils;
import fr.jerep6.ogi.utils.Functions;

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

	private void copyPhoto(String relativePathFile) throws IOException {

		
		// ProcesorTransformToCSV save into cvs photo path with "photos" leading directory. Remove it to access original
		// file into OGI directory
		Path relativePathComputed = Paths.get(photoDirName).relativize(Paths.get(relativePathFile));
		Path relativePathComputedWithoutReference = Paths.get(photoDirName).relativize(Paths.get(Functions.removeReferenceToPhotoName(relativePathFile)));

		// Compute absolute path for this file. Need to delete reference in filename
		Path absPhotoPath = DocumentUtils.absolutize(relativePathComputedWithoutReference);

		// Folder into copy photos
		Path destinationDirectory = absPhotosDirectory.resolve(relativePathComputed.getParent());
		Files.createDirectories(destinationDirectory);
		try {
			Files.copy(absPhotoPath, destinationDirectory.resolve(relativePathComputed.getFileName()),
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
		for (String photo : item.getPhotos()) {
			copyPhoto(photo);
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