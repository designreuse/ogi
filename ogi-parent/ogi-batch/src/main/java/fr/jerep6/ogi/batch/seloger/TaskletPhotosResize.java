package fr.jerep6.ogi.batch.seloger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class TaskletPhotosResize implements Tasklet {
	private final Logger	LOGGER	= LoggerFactory.getLogger(TaskletPhotosResize.class);
	private Resource		photosDirectory;
	private Integer			photoSize;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Files.walkFileTree(photosDirectory.getFile().toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				// Nothing to do for directory.
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				LOGGER.debug("Resize {}", file);

				// Resize image
				BufferedImage img = ImageIO.read(file.toFile());
				BufferedImage imgResize = Scalr.resize(img, photoSize);
				ImageIO.write(imgResize, "jpeg", file.toFile());

				return FileVisitResult.CONTINUE;
			}

		});

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}

	public void setPhotosDirectory(Resource photosDirectory) {
		this.photosDirectory = photosDirectory;
	}

	public void setPhotoSize(Integer photoSize) {
		this.photoSize = photoSize;
	}

}
