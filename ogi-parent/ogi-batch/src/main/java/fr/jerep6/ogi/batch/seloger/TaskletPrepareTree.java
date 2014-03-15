package fr.jerep6.ogi.batch.seloger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

/**
 * Prepare tree files for seloger.com
 * 
 * @author jerep6 15 mars 2014
 */
public class TaskletPrepareTree implements Tasklet {
	private final Logger		LOGGER			= LoggerFactory.getLogger(TaskletPrepareTree.class);

	private static final String	SELOGER_VERSION	= "SELOGER_VERSION";
	private static final String	OGI_VERSION		= "OGI_VERSION";

	private Resource			rootDirectory;

	private String				seLogerVersion;
	private Resource			config;
	private Resource			photosConfig;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		// Read file and replace token
		writeFileAndReplaceToken(config.getFile().toPath());
		writeFileAndReplaceToken(photosConfig.getFile().toPath());

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}

	public Resource getRootDirectory() {
		return rootDirectory;
	}

	public void setConfig(Resource config) {
		this.config = config;
	}

	public void setPhotosConfig(Resource photosConfig) {
		this.photosConfig = photosConfig;
	}

	public void setRootDirectory(Resource rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public void setSeLogerVersion(String seLogerVersion) {
		this.seLogerVersion = seLogerVersion;
	}

	private void writeFileAndReplaceToken(Path p) throws IOException {
		LOGGER.debug("Replace token from file {}", p);

		byte[] readAllBytes = Files.readAllBytes(p);
		String s = new String(readAllBytes, Charset.forName("UTF-8"));

		s = s.replaceAll(SELOGER_VERSION, seLogerVersion);
		s = s.replaceAll(OGI_VERSION, getClass().getPackage().getImplementationVersion());

		// Write content to root directory
		Path root = rootDirectory.getFile().toPath();
		Files.write(root.resolve(p.getFileName()), s.getBytes());
	}
}
