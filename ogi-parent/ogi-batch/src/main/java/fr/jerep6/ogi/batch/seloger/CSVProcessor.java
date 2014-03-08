package fr.jerep6.ogi.batch.seloger;

import org.springframework.batch.item.ItemProcessor;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceUrl;

public class CSVProcessor implements ItemProcessor<RealProperty, RealPropertyCSV> {

	private ServiceUrl	serviceUrl;

	public RealPropertyCSV process(RealProperty item) throws Exception {
		RealPropertyCSV r = new RealPropertyCSV();
		r.setTechid(item.getTechid());
		r.setReference(item.getReference());
		return r;
	}

	public void setServiceUrl(ServiceUrl serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}