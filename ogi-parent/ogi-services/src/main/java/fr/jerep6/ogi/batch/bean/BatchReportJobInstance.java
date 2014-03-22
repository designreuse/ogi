package fr.jerep6.ogi.batch.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;

@Getter
@Setter
public class BatchReportJobInstance {

	private JobInstance			jobInstance;
	private List<JobExecution>	jobExecutions	= new ArrayList<>(0);

	public BatchReportJobInstance(JobInstance jobInstance, List<JobExecution> jobExecutions) {
		super();
		this.jobInstance = jobInstance;
		this.jobExecutions = jobExecutions;
	}

	public Boolean getSuccess() {
		Boolean success = false;
		for (JobExecution ex : jobExecutions) {
			if (!ex.getStatus().isUnsuccessful()) {
				success = true;
				break;
			}
		}

		return success;
	}

}
