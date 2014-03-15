package fr.jerep6.ogi.batch.common.tasklet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import fr.jerep6.ogi.framework.utils.ZipUtils;

public class TaskletZip implements Tasklet {
	private final Logger		LOGGER	= LoggerFactory.getLogger(TaskletZip.class);
	private static final String	DATE	= "#DATE";

	/** Ressource to zip. Must be a directory */
	private Resource			resource;

	/** Zip name pattern. {@link #DATE} will be replace with current date */
	private String				zipNamePattern;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Path p = resource.getFile().toPath();

		// COmpute zipName
		SimpleDateFormat sp = new SimpleDateFormat("dd-MM-yyyy");
		String zipName = zipNamePattern.replaceAll(DATE, sp.format(new Date()));

		ZipUtils.zipDirectory(p, p.resolve(Paths.get(zipName)));

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setZipNamePattern(String zipNamePattern) {
		this.zipNamePattern = zipNamePattern;
	}

}
