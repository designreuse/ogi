package fr.jerep6.ogi.batch.common.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * http://stackoverflow.com/questions/20633005/accessing-spring-batch-job-step-details-via-the-generic-query-provider
 * 
 * @author jerep6 20 mars 2014
 */
@Component("PromoteStepStatToJobContextListener")
public class PromoteStepStatToJobContextListener implements StepListener {
	public static String	STATS_PREFIXE	= "StatStepExecution.";

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExecutionContext exe = stepExecution.getJobExecution().getExecutionContext();

		exe.put(STATS_PREFIXE + stepExecution.getStepName(), stepExecution);
		return stepExecution.getExitStatus();
	}
}