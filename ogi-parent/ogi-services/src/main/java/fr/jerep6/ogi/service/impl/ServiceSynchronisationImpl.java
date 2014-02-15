package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

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
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.transfert.WSResult;

@Service("serviceSynchronisation")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceSynchronisationImpl extends AbstractService implements ServiceSynchronisation {
	private final Logger				LOGGER	= LoggerFactory.getLogger(ServiceSynchronisationImpl.class);

	@Autowired
	private ServiceRealProperty			serviceRealProperty;

	@Autowired
	private ServicePartner				serviceAcimflo;

	// Map is defined in xml. Contains all instances of Service to interact with partners
	@Resource(name = "mapPartners")
	private Map<String, ServicePartner>	partners;

	@Override
	public List<WSResult> createOrUpdate(String partner, List<String> prpReferences) {
		List<WSResult> results = new ArrayList<>(prpReferences.size());

		// Get servicepartner according to name
		ServicePartner servicePartner = partners.get(partner);

		if (servicePartner != null) {
			Set<RealProperty> properties = serviceRealProperty.readByReference(prpReferences);

			for (RealProperty prp : properties) {
				WSResult ws = servicePartner.createOrUpdate(prp);
				results.add(ws);
			}
		} else {
			LOGGER.warn("Unknow partner {}", partner);
		}

		return results;
	}

	@Override
	public List<WSResult> delete(String partner, List<String> prpReferences) {
		List<WSResult> results = new ArrayList<>(prpReferences.size());

		// Get servicepartner according to name
		ServicePartner servicePartner = partners.get(partner);

		if (servicePartner != null) {
			for (String aRef : prpReferences) {
				WSResult ws = servicePartner.delete(aRef);
				results.add(ws);
			}
		} else {
			LOGGER.warn("Unknow partner {}", partner);
		}

		return results;
	}

	@Override
	public Boolean exist(String partner, String prpReference) {
		Boolean result = false;
		// Get servicepartner according to name
		ServicePartner servicePartner = partners.get(partner);

		if (servicePartner != null) {
			result = servicePartner.exist(prpReference);
		} else {
			LOGGER.warn("Unknow partner {}", partner);
		}
		return result;
	}
}