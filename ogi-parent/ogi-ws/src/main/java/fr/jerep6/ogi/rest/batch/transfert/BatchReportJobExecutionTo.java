package fr.jerep6.ogi.rest.batch.transfert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchReportJobExecutionTo {

	private Date								startTime;
	private Date								endTime;

	private BatchReportStatusTo					status;
	private BatchReportExitStatusTo				exitStatus;

	private List<BatchReportStepExecutionTo>	steps	= new ArrayList<>();
}
