package fr.jerep6.ogi.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.exception.business.FileIsNotAnImage;
import fr.jerep6.ogi.framework.utils.FileUtils;

@Component
public class ImageUtils {
	
	public static void resize(Path imgToResize, Integer targetSize, OutputStream outputToWriteImage) throws IOException {
		// Resize only image
		if (FileUtils.isImage(imgToResize)) {
			// Resize image
			BufferedImage img = ImageIO.read(imgToResize.toFile());
			BufferedImage imgResize = Scalr.resize(img, targetSize);
			ImageIO.write(imgResize, "jpeg", outputToWriteImage);
		}
	}
	
	public static void resize(Path imgToResize, Integer targetSize, File fileToWriteImage) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageUtils.resize(imgToResize, targetSize, bos);
		FileOutputStream fos = new FileOutputStream(fileToWriteImage);
		fos.write(bos.toByteArray());
		fos.flush();
		fos.close();
	}
	
	public static InputStream resize(Path imgToResize, Integer targetSize) throws FileIsNotAnImage, IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageUtils.resize(imgToResize, targetSize, os);
		
		if (os.size() > 0) {
			return new ByteArrayInputStream(os.toByteArray());
		} else {
			throw new FileIsNotAnImage(imgToResize);
		}
	}
}
