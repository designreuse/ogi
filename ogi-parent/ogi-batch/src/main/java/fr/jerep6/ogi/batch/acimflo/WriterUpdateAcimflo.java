package fr.jerep6.ogi.batch.acimflo;

import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.external.impl.ServiceAcimfloImpl;

public class WriterUpdateAcimflo implements ItemWriter<String>, InitializingBean {

	private ServiceAcimfloImpl	serviceAcimflo;
	private ServiceRealProperty	serviceRealProperty;

	@Override
	public void afterPropertiesSet() throws Exception {}

	public void setServiceAcimflo(ServiceAcimfloImpl serviceAcimflo) {
		this.serviceAcimflo = serviceAcimflo;
	}

	public void setServiceRealProperty(ServiceRealProperty serviceRealProperty) {
		this.serviceRealProperty = serviceRealProperty;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {
		Set<RealProperty> prp = serviceRealProperty.readByReference((List<String>) items);

		for (RealProperty realProperty : prp) {
			serviceAcimflo.createOrUpdate(realProperty);
		}

	}

}
