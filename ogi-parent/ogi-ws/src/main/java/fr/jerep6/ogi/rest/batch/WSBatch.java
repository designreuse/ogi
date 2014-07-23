package fr.jerep6.ogi.rest.batch;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.batch.bean.BatchReportJobInstance;
import fr.jerep6.ogi.batch.service.ServiceBatchReport;
import fr.jerep6.ogi.rest.AbtractWS;
import fr.jerep6.ogi.rest.batch.transfert.BatchReportInstanceTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@RestController
@RequestMapping(value = "/batch", produces = "application/json;charset=UTF-8")
public class WSBatch extends AbtractWS {

	@Autowired
	private ServiceBatchReport	serviceBatchSummerize;

	@Autowired
	private OrikaMapper			mapper;

	@RequestMapping(method = RequestMethod.GET)
	public Collection<BatchReportInstanceTo> readLast(@RequestParam("jobName") String jobName) {
		List<BatchReportJobInstance> instances = serviceBatchSummerize.readLastJobInstance(jobName);

		Collection<BatchReportInstanceTo> batchsReports = mapper.mapAsList(instances, BatchReportInstanceTo.class);
		return batchsReports;
	}

}