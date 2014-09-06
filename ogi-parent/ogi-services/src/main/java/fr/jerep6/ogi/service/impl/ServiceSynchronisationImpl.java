package fr.jerep6.ogi.service.impl;

import java.util.List;
import java.util.Map;

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
	public void createOrUpdate(String partner, List<String> prpReferences) {

		try {
			EnumPartner prt = EnumPartner.valueOfByCode(partner);
			// Predicate : if enumPartner exist, servicePartner have to exist
			ServicePartner servicePartner = partners.get(prt);

			for (String aRef : prpReferences) {

				// Validate real property according to partner specification
				RealProperty prp = serviceRealProperty.readByReference(aRef).get();
				servicePartner.validate(prp);

				// Write demand on database
				servicePartnerRequest.addRequest(EnumPartner.valueOfByCode(partner), prp.getTechid(),
						EnumPartnerRequestType.ADD_UPDATE);
			}
		} catch (IllegalArgumentException iae) {
			LOGGER.warn("Unknow partner {}. Exception = ", partner, iae.getMessage());

		}

	}

	@Override
	public void delete(String partner, List<String> prpReferences) {
		try {
			for (String aRef : prpReferences) {
				Integer prpTechid = serviceRealProperty.readTechid(aRef);
				servicePartnerRequest.addRequest(EnumPartner.valueOfByCode(partner), prpTechid,
						EnumPartnerRequestType.DELETE);
			}
		} catch (IllegalArgumentException iae) {
			LOGGER.warn("Unknow partner {}. Exception = ", partner, iae.getMessage());
		}
	}

	@Override
	public Boolean exist(String partner, String prpReference) {
		Boolean result = false;
		try {
			Integer prpTechid = serviceRealProperty.readTechid(prpReference);
			result = servicePartnerRequest.lastRequestIs(EnumPartner.valueOfByCode(partner), prpTechid,
					EnumPartnerRequestType.ADD_UPDATE, EnumPartnerRequestType.ADD_UPDATE_ACK);
		} catch (IllegalArgumentException iae) {
			LOGGER.warn("Unknow partner {}. Exception = ", partner, iae.getMessage());
		}
		return result;
	}

}