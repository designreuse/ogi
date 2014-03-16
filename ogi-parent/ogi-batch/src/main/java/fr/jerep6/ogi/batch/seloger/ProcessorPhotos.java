package fr.jerep6.ogi.batch.seloger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.Resource;

import fr.jerep6.ogi.utils.DocumentUtils;

/**
 * Collect photos of property and place them into destination directory
 * 
 * Photos are selected according to RealPropertyCSV photos fields
 * 
 * @author jerep6 15 mars 2014
 */
public class ProcessorPhotos implements ItemProcessor<RealPropertyCSV, RealPropertyCSV> {
	private static Logger	LOGGER	= LoggerFactory.getLogger(ProcessorPhotos.class);

	private Resource		photosDirectory;

	private void copyPhoto(String relativePathFile) throws IOException {

		// Compute absolute path for this file
		Path absPhotoPath = DocumentUtils.absolutize(Paths.get(relativePathFile));

		// Folder into copy photos
		Path destinationDirectory = photosDirectory.getFile().toPath().resolve(Paths.get(relativePathFile).getParent());
		Files.createDirectories(destinationDirectory);
		Files.copy(absPhotoPath, destinationDirectory.resolve(Paths.get(relativePathFile).getFileName()),
				StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public RealPropertyCSV process(RealPropertyCSV item) throws Exception {

		// Copy all available photos in RealPropertyCSV into output directory
		for (String photo : item.getPhotos()) {
			copyPhoto(photo);
		}

		return item;
	}

	public void setPhotosDirectory(Resource photosDirectory) {
		this.photosDirectory = photosDirectory;
	}

}