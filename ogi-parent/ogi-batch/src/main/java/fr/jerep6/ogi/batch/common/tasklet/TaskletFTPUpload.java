package fr.jerep6.ogi.batch.common.tasklet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class TaskletFTPUpload implements Tasklet {
	private final Logger	LOGGER	= LoggerFactory.getLogger(TaskletFTPUpload.class);

	/** Resources to upload */
	private List<Resource>	resources;

	/** FTP dir into upload. Must be ended with trailing / */
	private String			destinationDirectory;

	private String			ftpHost;
	private String			ftpLogin;
	private String			ftpPwd;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOGGER.debug("Directories to upload : {}", resources);

		// Establish connection
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		int reply;
		ftp.connect(ftpHost);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new Exception("Exception in connecting to FTP Server");
		}
		ftp.login(ftpLogin, ftpPwd);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();

		// Upload resources to ftp
		for (Resource res : resources) {
			ftp.storeFile(destinationDirectory + res.getFile().getName(), res.getInputStream());
		}

		// Disconnect
		try {
			ftp.logout();
			ftp.disconnect();
		} catch (IOException ioe) {
			LOGGER.error("Error to disconnect", ioe);
		}

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}

	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public void setFtpLogin(String ftpLogin) {
		this.ftpLogin = ftpLogin;
	}

	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}

	public void setResources(Resource[] resources) {
		this.resources = Arrays.asList(resources);
	}

}
