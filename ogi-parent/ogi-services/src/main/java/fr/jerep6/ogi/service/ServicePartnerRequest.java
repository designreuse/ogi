package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.transfert.PartnerPropertyCount;

public interface ServicePartnerRequest extends TransactionalService<PartnerRequest, Integer> {

	Set<PartnerPropertyCount> countPropertyOnPartners();

	PartnerRequest lastRequest(EnumPartner partner, String prpReference);

	/**
	 * Return lasts requests for each property
	 *
	 * @return
	 */
	Map<String, List<PartnerRequest>> lastRequests();

	void addRequest(EnumPartner partner, Integer prpTechid, EnumPartnerRequestType requestType);

}
