package fr.jerep6.ogi.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.business.FileAlreadyExist;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Photo;
import fr.jerep6.ogi.persistance.dao.DaoPhoto;
import fr.jerep6.ogi.service.ServicePhoto;
import fr.jerep6.ogi.transfert.FileUpload;

@Service("servicePhoto")
@Transactional(propagation = Propagation.REQUIRED)
public class ServicePhotoImpl extends AbstractTransactionalService<Photo, Integer> implements ServicePhoto {
	Logger				LOGGER	= LoggerFactory.getLogger(ServicePhotoImpl.class);

	@Value("${photos.dir}")
	private String		photosDir;

	@Autowired
	private DaoPhoto	daoPhoto;

	@Override
	public FileUpload copyToPhotosDirectory(InputStream is, String fileName, String reference) throws IOException {
		Preconditions.checkNotNull(is);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(fileName));
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));

		// Temp directory for photos
		Path root = Paths.get(photosDir, "temp", reference);

		// Create temps directory if not exist
		if (!Files.isDirectory(root)) {
			LOGGER.info("Create directory {}", root.resolve(reference));
			Files.createDirectories(root);
		}

		Path photo = root.resolve(fileName);

		// If file exist => exception
		if (Files.exists(photo)) { throw new FileAlreadyExist(photo.getFileName().toString()); }

		// Copy file into photo temp directory
		Files.copy(is, photo);

		FileUpload f = new FileUpload.Builder() //
				.name(fileName) //
				.size(Files.size(photo)) //
				.url("http://example.org/files/picture1.jpg") //
				.thumbnailUrl("http://example.org/files/picture1.jpg") //
				.deleteUrl("http://example.org/files/picture1.jpg") //
				.deleteType("DELETE") //
				.build();

		return f;

	}

	@Override
	public List<Photo> getPhotos(String reference) {
		List<Photo> photos = daoPhoto.getPhotos(reference);
		return photos;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoPhoto);
	}

}
