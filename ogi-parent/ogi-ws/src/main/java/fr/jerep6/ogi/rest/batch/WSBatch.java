package fr.jerep6.ogi.rest.batch;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.batch.bean.BatchReportJobInstance;
import fr.jerep6.ogi.batch.service.ServiceBatchReport;
import fr.jerep6.ogi.rest.AbstractJaxRsWS;
import fr.jerep6.ogi.rest.batch.transfert.BatchReportInstanceTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@Controller
@Path("/batch")
public class WSBatch extends AbstractJaxRsWS {

	@Autowired
	private ServiceBatchReport	serviceBatchSummerize;

	@Autowired
	private OrikaMapper			mapper;

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public Collection<BatchReportInstanceTo> readLast(@QueryParam("jobName") String jobName) {
		List<BatchReportJobInstance> instances = serviceBatchSummerize.readLastJobInstance(jobName);

		Collection<BatchReportInstanceTo> batchsReports = mapper.mapAsList(instances, BatchReportInstanceTo.class);
		return batchsReports;
	}

}