package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Map;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;

public interface ServicePartnerRequest extends TransactionalService<PartnerRequest, Integer> {

	/**
	 * Indicate if the last request for external web site is a type ...
	 *
	 * @param partner
	 *            external web site
	 * @param requestType
	 * @return
	 */
	boolean lastRequestIs(EnumPartner partner, Integer prpTechid, EnumPartnerRequestType... addUpdateAck);

	PartnerRequest lastRequest(EnumPartner partner, String prpReference);

	/**
	 * Return lasts requests for each property
	 *
	 * @return
	 */
	Map<String, List<PartnerRequest>> lastRequests();

	void addRequest(EnumPartner partner, Integer prpTechid, EnumPartnerRequestType requestType);

}
