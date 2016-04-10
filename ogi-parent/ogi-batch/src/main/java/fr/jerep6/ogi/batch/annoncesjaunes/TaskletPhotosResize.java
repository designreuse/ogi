package fr.jerep6.ogi.batch.annoncesjaunes;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import fr.jerep6.ogi.framework.utils.FileUtils;
import fr.jerep6.ogi.utils.ImageUtils;

public class TaskletPhotosResize implements Tasklet {
	private final Logger	LOGGER	= LoggerFactory.getLogger(TaskletPhotosResize.class);
	private Resource		photosDirectory;
	private Integer			photoSize;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		final Map<String, Integer> m = new HashMap<String, Integer>(1);
		m.put("nbPhotosResize", 0);

		Files.walkFileTree(photosDirectory.getFile().toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				// Nothing to do for directory.
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				LOGGER.debug("Resize {}", file);

				if (FileUtils.isImage(file)) {
					ImageUtils.resize(file, photoSize, file.toFile());
					m.put("nbPhotosResize", m.get("nbPhotosResize") + 1);
				}
				

				return FileVisitResult.CONTINUE;
			}

		});

		// Write step execution context the number of resized photos
		chunkContext.getStepContext().getStepExecution().setWriteCount(m.get("nbPhotosResize"));
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
