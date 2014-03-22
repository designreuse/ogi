package fr.jerep6.ogi.batch.service;

import java.util.List;

import fr.jerep6.ogi.batch.bean.BatchReportJobInstance;
import fr.jerep6.ogi.framework.service.Service;

public interface ServiceBatchReport extends Service {
	/**
	 * Read 5 last job instances of specified job
	 * 
	 * @param jobName
	 *            name of job. If job doesn't exist return an empty list
	 * @return
	 */
	List<BatchReportJobInstance> readLastJobInstance(String jobName);
}
