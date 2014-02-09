package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import fr.jerep6.ogi.transfert.WSResult;

@Service("serviceSynchronisation")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceSynchronisationImpl extends AbstractService implements ServiceSynchronisation {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceSynchronisationImpl.class);

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private ServiceAcimflo		serviceAcimflo;

	@Override
	public List<WSResult> createOrUpdate(String partner, List<String> prpReferences) {
		Set<RealProperty> properties = serviceRealProperty.readByReference(prpReferences);

		List<WSResult> results = new ArrayList<>(prpReferences.size());
		for (RealProperty prp : properties) {
			WSResult ws = null;
			switch (partner) {
				case "acimflo":
					ws = serviceAcimflo.createOrUpdate(prp);
					break;

				default:
					LOGGER.warn("Unknow partner {}", partner);
					break;
			}
			if (ws != null) {
				results.add(ws);
			}
		}

		return results;
	}

	@Override
	public List<WSResult> delete(String partner, List<String> prpReferences) {
		List<WSResult> results = new ArrayList<>(prpReferences.size());

		for (String aRef : prpReferences) {
			WSResult ws = null;
			switch (partner) {
				case "acimflo":
					ws = serviceAcimflo.delete(aRef);
					break;

				default:
					LOGGER.warn("Unknow partner {}", partner);
					break;
			}
			if (ws != null) {
				results.add(ws);
			}
		}

		return results;
	}

	@Override
	public Boolean exist(String partner, String prpReference) {
		Boolean b = false;
		switch (partner) {
			case "acimflo":
				b = serviceAcimflo.exist(prpReference);
				break;

			default:
				LOGGER.warn("Unknow partner {}", partner);
				break;
		}
		return b;
	}
}