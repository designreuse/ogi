package fr.jerep6.ogi.rest.batch.transfert;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchReportStepExecutionTo {
	private String					stepName;
	private BatchReportStatusTo		status;
	private BatchReportExitStatusTo	exitStatus;
	private Integer					readCount			= 0;
	private Integer					writeCount			= 0;
	private Integer					commitCount			= 0;
	private Integer					rollbackCount		= 0;
	private Integer					readSkipCount		= 0;
	private Integer					processSkipCount	= 0;
	private Integer					writeSkipCount		= 0;
}
