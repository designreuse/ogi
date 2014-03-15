package fr.jerep6.ogi.framework.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(FileUtils.class);

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

	private FileUtils() {}

}
