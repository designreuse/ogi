package fr.jerep6.ogi.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.obj.PhotoDimension;

@Component
public class UrlPhotoUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(UrlPhotoUtils.class);

	private static String		urlBasePhoto;
	private static String		photoDir;
	private static Path			pathDir;

	public static String buildUrl(Path absolutePath) {
		return buildUrl(absolutePath, null);
	}

	public static String buildUrl(Path absolutePath, PhotoDimension dimension) {
		String size = "";
		if (dimension != null) {
			size = "?size=" + dimension.getName();
		}
		return urlBasePhoto + pathDir.relativize(absolutePath).toString().replace("\\", "/") + size;
	}

	@PostConstruct
	private void init() {
		pathDir = Paths.get(photoDir);
	}

	@Value("${photos.dir}")
	public void setPhotoDir(String photoDir) {
		UrlPhotoUtils.photoDir = photoDir;
	}

	@Value("${photos.url}")
	public void setUrlBasePhoto(String urlBasePhoto) {
		this.urlBasePhoto = urlBasePhoto;
	}
}
