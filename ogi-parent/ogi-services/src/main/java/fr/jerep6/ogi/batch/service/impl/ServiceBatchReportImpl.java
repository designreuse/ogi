package fr.jerep6.ogi.batch.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.jerep6.ogi.batch.bean.BatchReportJobInstance;
import fr.jerep6.ogi.batch.service.ServiceBatchReport;
import fr.jerep6.ogi.framework.service.impl.AbstractService;

@Service("serviceBatchSummerize")
public class ServiceBatchReportImpl extends AbstractService implements ServiceBatchReport {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceBatchReportImpl.class);

	@Autowired
	private JobExplorer		jobExplorer;

	@Override
	public List<BatchReportJobInstance> readLastJobInstance(String jobName) {
		List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 5);

		List<BatchReportJobInstance> ret = new ArrayList<>();
		for (JobInstance jobInstance : jobInstances) {
			List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
			ret.add(new BatchReportJobInstance(jobInstance, jobExecutions));
		}

		return ret;
	}
}