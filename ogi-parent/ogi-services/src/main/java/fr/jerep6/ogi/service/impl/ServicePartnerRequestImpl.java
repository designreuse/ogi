package fr.jerep6.ogi.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoPartnerRequest;
import fr.jerep6.ogi.service.ServicePartnerRequest;
import fr.jerep6.ogi.service.ServiceRealProperty;

@Service("servicePartnerExistence")
@Transactional(propagation = Propagation.REQUIRED)
public class ServicePartnerRequestImpl extends AbstractTransactionalService<PartnerRequest, Integer> implements
		ServicePartnerRequest {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServicePartnerRequestImpl.class);

	@Autowired
	private DaoPartnerRequest	daoPartnerRequest;

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Override
	public Map<String, List<PartnerRequest>> lastRequests() {
		List<Object[]> lastRequests = daoPartnerRequest.lastRequests();

		Map<String, List<PartnerRequest>> collect2 = lastRequests.stream().collect(//
				// Grouping by prp reference and extract only partner request
				Collectors.groupingBy(o -> ((RealProperty) o[0]).getReference(), //
						Collectors.mapping(o -> (PartnerRequest) o[1], Collectors.toList())));

		return collect2;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addRequest(EnumPartner partner, Integer prpTechid, EnumPartnerRequestType requestType) {
		PartnerRequest r = new PartnerRequest(prpTechid, partner, requestType);
		daoPartnerRequest.save(r);
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoPartnerRequest);
	}

	@Override
	public PartnerRequest lastRequest(EnumPartner partner, String prpReference) {
		Integer prpTechid = serviceRealProperty.readTechid(prpReference);
		return daoPartnerRequest.lastRequests(partner, prpTechid);
	}

	@Override
	public boolean lastRequestIs(EnumPartner partner, Integer prpTechid, EnumPartnerRequestType... requestType) {
		List<EnumPartnerRequestType> l = Arrays.asList(requestType);
		return daoPartnerRequest.lastRequestIs(partner, prpTechid, l);
	}
}
