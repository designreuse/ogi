package fr.jerep6.ogi.batch.diaporama;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.external.impl.ServiceDiaporamaImpl;

public class WriterUpdateDiaporama implements ItemWriter<RealPropertyDiaporama>, InitializingBean {

	private ServiceDiaporamaImpl	serviceDiaporama;
	private ServiceRealProperty		serviceRealProperty;

	@Override
	public void afterPropertiesSet() throws Exception {}

	public void setServiceDiaporama(ServiceDiaporamaImpl serviceDiaporama) {
		this.serviceDiaporama = serviceDiaporama;
	}

	public void setServiceRealProperty(ServiceRealProperty serviceRealProperty) {
		this.serviceRealProperty = serviceRealProperty;
	}

	@Override
	public void write(List<? extends RealPropertyDiaporama> items) throws Exception {
		Set<RealProperty> prp = serviceRealProperty.readByReference(items.stream().map(a -> a.getReference())
				.collect(Collectors.toList()));

		for (RealProperty realProperty : prp) {
			serviceDiaporama.createOrUpdate(realProperty);
		}

	}

}
