package fr.jerep6.ogi.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author http://examples.javacodegeeks.com/core-java/io/fileinputstream/decompress-a-gzip-file-in-java-example/
 * @author jerep6 http://www.avajava.com/tutorials/lessons/how-do-i-zip-a-directory-and-all-its-contents.html
 * */
public class ZipUtils {
	private static final Logger	LOGGER	= LoggerFactory.getLogger(ZipUtils.class);

	/**
	 * Directory into file to zip is store
	 * 
	 * @param directoryToZip
	 *            root directory of zip. File must be within this directory
	 * @param file
	 *            file to add into zip
	 * @param zos
	 *            zip output stream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void addToZip(Path directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
			IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath;
		// If current file to add in zip is root file (ie)
		if (Files.isSameFile(directoryToZip, file.toPath())) {
			zipFilePath = "";
		} else {
			// replace backslash o slash because unzip fail under linux system
			zipFilePath = file
					.getCanonicalPath()
					.substring(directoryToZip.toFile().getCanonicalPath().length() + 1,
							file.getCanonicalPath().length()).replace("\\", "/");
		}
		LOGGER.debug("Writing '{}' to zip file", zipFilePath);

		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	private static List<Path> getAllFiles(Path dir) throws IOException {
		final List<Path> fileList = new ArrayList<>();

		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				// Nothing to do for directory.
				// fileList.add(dir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				fileList.add(file);
				return FileVisitResult.CONTINUE;
			}

		});
		return fileList;
	}

	public static void gzipFile(String source_filepath, String destinaton_zip_filepath) throws IOException {
		byte[] buffer = new byte[1024];
		FileOutputStream fileOutputStream = new FileOutputStream(destinaton_zip_filepath);
		GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);
		FileInputStream fileInput = new FileInputStream(source_filepath);

		int bytes_read;
		while ((bytes_read = fileInput.read(buffer)) > 0) {
			gzipOuputStream.write(buffer, 0, bytes_read);
		}

		fileInput.close();
		gzipOuputStream.finish();
		gzipOuputStream.close();

	}

	public static String unGunzipFile(String compressedFile) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		unGunzipFile(compressedFile, os);
		String s = new String(os.toByteArray(), "UTF-8");
		return s;
	}

	public static void unGunzipFile(String compressedFile, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[1024];

		FileInputStream fileIn = new FileInputStream(compressedFile);
		GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);

		int bytes_read;
		while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, bytes_read);
		}

		gZIPInputStream.close();
		outputStream.close();
	}

	public static void unGunzipFile(String compressedFile, String decompressedFile) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
		unGunzipFile(compressedFile, fileOutputStream);
	}

	public static void unZip(Path zipFile, Path directoryToStoreContent) throws IOException {
		// create output directory is not exists
		if (!Files.exists(directoryToStoreContent)) {
			Files.createDirectories(directoryToStoreContent);
		}

		// get the zip file content
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toFile()));

		// get the zipped file list entry
		ZipEntry ze = zis.getNextEntry();
		while (ze != null) {
			String fileName = ze.getName();
			Path newFile = directoryToStoreContent.resolve(Paths.get(fileName));

			LOGGER.debug("File to unzip {}", newFile);

			// create all non exists folders
			// else you will hit FileNotFoundException for compressed folder
			Files.createDirectories(newFile.getParent());

			FileOutputStream fos = new FileOutputStream(newFile.toFile());
			FileUtils.write(zis, fos);

			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
	}

	public static void zipDirectory(Path directoryToZip, Path zipFile) throws IOException {
		// List all files into directory to zip
		List<Path> fileList = getAllFiles(directoryToZip);

		// If file to zip => zip
		if (!fileList.isEmpty()) {
			// Create zip file
			FileOutputStream fos = new FileOutputStream(zipFile.toFile());
			ZipOutputStream zos = new ZipOutputStream(fos);

			// Add each file to zip
			for (Path file : fileList) {
				File f = file.toFile();
				addToZip(directoryToZip, f, zos);
			}

			zos.close();
			fos.close();
		}

	}

}