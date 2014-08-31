package fr.jerep6.ogi.batch.acimflo;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.service.ServiceSynchronisation;

public class WriterUpdateAcimflo implements ItemWriter<String>, InitializingBean {

	private ServiceSynchronisation	serviceSynchronisation;

	@Override
	public void afterPropertiesSet() throws Exception {}

	public void setServiceSynchronisation(ServiceSynchronisation serviceSynchronisation) {
		this.serviceSynchronisation = serviceSynchronisation;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {
		serviceSynchronisation.createOrUpdate(EnumPartner.ACIMFLO.getCode(), (List<String>) items);

	}

}
