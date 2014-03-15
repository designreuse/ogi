package fr.jerep6.ogi.batch.common.tasklet;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import fr.jerep6.ogi.framework.utils.FileUtils;

public class TaskletDeleteDirectories implements Tasklet {
	private final Logger	LOGGER	= LoggerFactory.getLogger(TaskletDeleteDirectories.class);

	private List<Resource>	resources;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOGGER.debug("Directories to delete : {}", resources);

		for (Resource r : resources) {
			FileUtils.delete(r.getFile().toPath());
		}

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}

	public void setResources(Resource[] resources) {
		this.resources = Arrays.asList(resources);
	}

}
