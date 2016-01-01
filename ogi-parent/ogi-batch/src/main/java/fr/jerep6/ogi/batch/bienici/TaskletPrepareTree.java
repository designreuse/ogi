package fr.jerep6.ogi.batch.bienici;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import fr.jerep6.ogi.framework.utils.FileUtils;

/**
 * Prepare tree files for seloger.com
 * 
 * @author jerep6 15 mars 2014
 */
public class TaskletPrepareTree implements Tasklet {
	private final Logger		LOGGER					= LoggerFactory.getLogger(TaskletPrepareTree.class);

	private static final String	SELOGER_VERSION			= "#SELOGER_VERSION";
	private static final String	OGI_VERSION				= "#OGI_VERSION";

	private Resource			rootDirectory;

	private String				seLogerVersion;
	private Resource			config;
	private Resource			photosConfig;

	/** List of directory to create. Path relative to rootDirectory */
	private List<Resource>		subDirectoriesToCreate	= new ArrayList<>(0);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		// chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
		// .put("POUET", "VALEUR POUET");

		// Create root
		Path root = rootDirectory.getFile().toPath();
		Files.createDirectories(root);

		// Create sub directory
		for (Resource r : subDirectoriesToCreate) {
			if (!r.exists()) {
				Files.createDirectories(root.resolve(r.getFile().toPath()));
			}
		}

		// Read file and replace token
		writeFileAndReplaceToken(config);
		writeFileAndReplaceToken(photosConfig);

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

	public void setSubDirectoriesToCreate(List<Resource> subDirectoriesToCreate) {
		this.subDirectoriesToCreate = subDirectoriesToCreate;
	}

	private void writeFileAndReplaceToken(Resource r) throws IOException {
		// The resource is in the jar so, I can't use Path or file
		StringWriter w = new StringWriter();
		FileUtils.write(r.getInputStream(), w);

		// Replace token
		String s = w.toString();
		s = s.replaceAll(SELOGER_VERSION, seLogerVersion);
		s = s.replaceAll(OGI_VERSION, getClass().getPackage().getImplementationVersion() == null ? "unknow"
				: getClass().getPackage().getImplementationVersion());

		// Write content to root directory
		Path root = rootDirectory.getFile().toPath();
		Files.write(root.resolve(Paths.get(r.getFilename())), s.getBytes());
	}
}
