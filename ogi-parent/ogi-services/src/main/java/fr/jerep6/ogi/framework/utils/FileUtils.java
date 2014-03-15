package fr.jerep6.ogi.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(FileUtils.class);
	private static byte[]		buffer	= new byte[1024];

	/**
	 * Delete path. If folder is not empty delete all this content before delete it
	 * 
	 * @param p
	 *            path to delete. Folder or file
	 * @throws IOException
	 */
	public static void delete(Path p) throws IOException {
		if (p == null) {
			return;
		}

		// For directory walk into it to delete all files
		if (Files.isDirectory(p)) {
			LOGGER.debug("Deleting {}", p);

			Files.walkFileTree(p, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					if (exc == null) {
						LOGGER.debug("Deleting {}", dir);
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					} else {
						throw exc;
					}
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

			});

		} else {
			LOGGER.debug("Deleting {}", p);
			Files.delete(p);
		}
	}

	public static void write(InputStream is, OutputStream os) throws IOException {

		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}

		os.flush();
		os.close();
	}

	private FileUtils() {}

}
