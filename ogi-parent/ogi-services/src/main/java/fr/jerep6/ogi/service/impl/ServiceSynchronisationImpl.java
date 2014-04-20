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

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServicePartnerRequest;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceSynchronisation;
import fr.jerep6.ogi.service.external.ServicePartner;
import fr.jerep6.ogi.transfert.WSResult;

@Service("serviceSynchronisation")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceSynchronisationImpl extends AbstractService implements ServiceSynchronisation {
	private final Logger						LOGGER	= LoggerFactory.getLogger(ServiceSynchronisationImpl.class);

	@Autowired
	private ServiceRealProperty					serviceRealProperty;

	@Autowired
	private ServicePartnerRequest				servicePartnerRequest;

	// Map is defined in xml. Contains all instances of Service to interact with partners
	@Resource(name = "mapPartners")
	private Map<EnumPartner, ServicePartner>	partners;

	@Override
	public List<WSResult> createOrUpdate(String partner, List<String> prpReferences) {
		List<WSResult> results = new ArrayList<>(prpReferences.size());

		try {
			EnumPartner prt = EnumPartner.valueOfByCode(partner);
			// Predicate : if enumPartner exist, servicePartner have to exist
			ServicePartner servicePartner = partners.get(prt);

			// Read properties
			Set<RealProperty> properties = serviceRealProperty.readByReference(prpReferences);

			for (RealProperty prp : properties) {
				// Validate if property is valid for partner
				servicePartner.validate(prp);

				// insert in database add/update request. It will be ack when property will be really updat on partner
				servicePartnerRequest.addRequest(prt, prp.getTechid(), EnumPartnerRequestType.ADD_UPDATE);

				// Create or update property on external website
				WSResult ws = servicePartner.createOrUpdate(prp);

				results.add(ws);
			}
		} catch (IllegalArgumentException iae) {
			LOGGER.warn("Unknow partner {}. Exception = ", partner, iae.getMessage());

		}

		return results;
	}

	@Override
	public List<WSResult> delete(String partner, List<String> prpReferences) {
		List<WSResult> results = new ArrayList<>(prpReferences.size());

		try {
			EnumPartner prt = EnumPartner.valueOfByCode(partner);
			// Predicate : if enumPartner exist, servicePartner have to exist
			ServicePartner servicePartner = partners.get(prt);

			for (String aRef : prpReferences) {
				Integer prpTechid = serviceRealProperty.readTechid(aRef);
				// insert in database add/update request. It will be ack when property will be really updat on partner
				servicePartnerRequest.addRequest(prt, prpTechid, EnumPartnerRequestType.DELETE);

				WSResult ws = servicePartner.delete(serviceRealProperty.readByReference(aRef));
				results.add(ws);
			}
		} catch (IllegalArgumentException iae) {
			LOGGER.warn("Unknow partner {}. Exception = ", partner, iae.getMessage());
		}
		return results;
	}

	@Override
	public Boolean exist(String partner, String prpReference) {
		Boolean result = false;
		try {
			EnumPartner prt = EnumPartner.valueOfByCode(partner);
			// Predicate : if enumPartner exist, servicePartner have to exist
			ServicePartner servicePartner = partners.get(prt);

			result = servicePartner.exist(prpReference);
		} catch (IllegalArgumentException iae) {
			LOGGER.warn("Unknow partner {}. Exception = ", partner, iae.getMessage());

		}
		return result;
	}
}