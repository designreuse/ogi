package fr.jerep6.ogi.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceSynchronisation;
import fr.jerep6.ogi.service.external.ServiceAcimflo;

@Service("serviceSynchronisation")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceSynchronisationImpl extends AbstractService implements ServiceSynchronisation {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceSynchronisationImpl.class);

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private ServiceAcimflo		serviceAcimflo;

	@Override
	public void createOrUpdate(List<String> prpReferences) {
		Set<RealProperty> properties = serviceRealProperty.readByReference(prpReferences);

		try {
			serviceAcimflo.createOrUpdate(properties);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}