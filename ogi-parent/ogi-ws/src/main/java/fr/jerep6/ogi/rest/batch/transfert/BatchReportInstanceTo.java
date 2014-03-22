package fr.jerep6.ogi.rest.batch.transfert;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchReportInstanceTo {

	private String							jobName;
	private List<BatchReportJobExecutionTo>	jobExecutions	= new ArrayList<>(0);
	private Boolean							isSuccess;
}
