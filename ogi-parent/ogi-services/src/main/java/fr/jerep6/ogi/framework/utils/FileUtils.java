package fr.jerep6.ogi.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {
	private static final Logger	LOGGER				= LoggerFactory.getLogger(FileUtils.class);
	private static Integer		DEFAULT_BUFFER_SIZE	= 1024;

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
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}

		os.flush();
		os.close();
	}

	/**
	 * Copy bytes from an InputStream to chars on a Writer using UTF-8 charset
	 * This method buffers the input internally, so there is no need to use a BufferedInputStream.
	 * This method uses java.io.InputStreamReader.
	 * 
	 * @param input
	 *            input the InputStream to read from
	 * @param output
	 *            the Writer to write to
	 * @throws NullPointerException
	 *             if the input or output is nul
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void write(InputStream input, Writer output) throws IOException {
		InputStreamReader in = new InputStreamReader(input, Charset.forName("UTF-8"));
		FileUtils.write(in, output);
	}

	public static long write(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}

		return count;
	}

	private FileUtils() {}

}
