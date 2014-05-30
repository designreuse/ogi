package fr.jerep6.ogi.batch.seloger;

import java.util.Optional;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * Indique si des biens ont été écrit dans le fichier csv.<br />
 * If no data write in step <code>stepWriteCSVRent</code> AND no data write in step <code>stepWriteCSVSale</code> =>
 * exitStatus is <b>NO_DATA</b><br />
 * else exist statut is <b>COMPLETED</b>
 * 
 * @author jerep6 29 mai 2014
 */
public class DeciderHasPrpProcessed implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

		// Get step "stepWriteCSVRent"
		Optional<StepExecution> stepRent = stepExecution.getJobExecution().getStepExecutions().stream()
				.filter(s -> "stepWriteCSVRent".equals(s.getStepName())).findFirst();

		// Get step "stepWriteCSVSale"
		Optional<StepExecution> stepSale = stepExecution.getJobExecution().getStepExecutions().stream()
				.filter(s -> "stepWriteCSVSale".equals(s.getStepName())).findFirst();

		// Indicate if step write 0
		boolean zeroRent = stepRent.isPresent() && stepRent.get().getWriteCount() == 0;
		boolean zeroSale = stepSale.isPresent() && stepSale.get().getWriteCount() == 0;

		return zeroRent && zeroSale ? new FlowExecutionStatus("NO_DATA") : FlowExecutionStatus.COMPLETED;
	}

}
