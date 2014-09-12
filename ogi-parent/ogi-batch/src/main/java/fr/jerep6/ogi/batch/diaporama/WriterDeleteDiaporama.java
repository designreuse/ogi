package fr.jerep6.ogi.batch.diaporama;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import fr.jerep6.ogi.service.external.impl.ServiceDiaporamaImpl;

public class WriterDeleteDiaporama implements ItemWriter<RealPropertyDiaporama>, InitializingBean {

	private ServiceDiaporamaImpl	serviceDiaporama;

	@Override
	public void afterPropertiesSet() throws Exception {}

	public void setServiceDiaporama(ServiceDiaporamaImpl serviceDiaporama) {
		this.serviceDiaporama = serviceDiaporama;
	}

	@Override
	public void write(List<? extends RealPropertyDiaporama> items) throws Exception {

		for (RealPropertyDiaporama a : items) {
			serviceDiaporama.delete(a.getTechid(), a.getReference());
		}
	}

}
