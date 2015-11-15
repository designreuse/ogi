package fr.jerep6.ogi.batch.annoncesjaunes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

/**
 * Prepare tree files for annonces jaunes
 * 
 * @author jerep6 11 novembre 2015
 */
public class TaskletPrepareTree implements Tasklet {
	private final Logger		LOGGER					= LoggerFactory.getLogger(TaskletPrepareTree.class);

	private Resource			rootDirectory;


	/** List of directory to create. Path relative to rootDirectory */
	private List<Resource>		subDirectoriesToCreate	= new ArrayList<>(0);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// Create root
		Path root = rootDirectory.getFile().toPath();
		Files.createDirectories(root);

		// Create sub directory
		for (Resource r : subDirectoriesToCreate) {
			if (!r.exists()) {
				Files.createDirectories(root.resolve(r.getFile().toPath()));
			}
		}

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}

	public Resource getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(Resource rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public void setSubDirectoriesToCreate(List<Resource> subDirectoriesToCreate) {
		this.subDirectoriesToCreate = subDirectoriesToCreate;
	}
}
