package fr.jerep6.ogi.rest.external;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.service.ServicePartnerRequest;
import fr.jerep6.ogi.service.ServiceSynchronisation;
import fr.jerep6.ogi.transfert.PartnerPropertyCount;
import fr.jerep6.ogi.transfert.bean.PartnerPropertyCountTo;
import fr.jerep6.ogi.transfert.bean.PartnerRequestTo;
import fr.jerep6.ogi.transfert.bean.SynchronisationListAll;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@RestController
@RequestMapping(value = "/synchronisation", produces = "application/json;charset=UTF-8")
public class WSSynchronisation {

	@Autowired
	private ServicePartnerRequest	servicePartnerRequest;

	@Autowired
	private ServiceSynchronisation	serviceSynchronisation;

	@Autowired
	private OrikaMapper				mapper;

	@RequestMapping(value = "/{partner}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("partner") String partner, @RequestParam("ref") List<String> prpReferences) {
		serviceSynchronisation.delete(partner, prpReferences);
	}

	@RequestMapping(value = "/{partner}/{ref}", method = RequestMethod.GET)
	public Map<String, Object> get(@PathVariable("partner") String partner, @PathVariable("ref") String prpReference) {

		PartnerRequest lastRequest = servicePartnerRequest
				.lastRequest(EnumPartner.valueOfByCode(partner), prpReference);
		PartnerRequestTo lastRequestTo = mapper.map(lastRequest, PartnerRequestTo.class);

		HashMap<String, Object> result = new HashMap<>(1);
		result.put("exist", lastRequestTo != null ? lastRequestTo.isPresentOnPartner() : false);
		result.put("lastRequest", lastRequestTo);
		return result;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SynchronisationListAll listAll() {
		Map<String, List<PartnerRequest>> lastRequests = servicePartnerRequest.lastRequests();

		// Map<String, List<PartnerRequestTo>> convertMap = mapper.mapAsMap(lastRequests,
		// new TypeBuilder<Map<String, List<PartnerRequest>>>() {}.build(),
		// new TypeBuilder<Map<String, List<PartnerRequestTo>>>() {}.build());

		// Count property on partners
		Set<PartnerPropertyCount> count = servicePartnerRequest.countPropertyOnPartners();
		List<PartnerPropertyCountTo> countTo = mapper.mapAsList(count, PartnerPropertyCountTo.class);

		Map<String, List<PartnerRequestTo>> m = new HashMap<>(lastRequests.size());
		for (Entry<String, List<PartnerRequest>> e : lastRequests.entrySet()) {
			m.put(e.getKey(), mapper.mapAsList(e.getValue(), PartnerRequestTo.class));
		}
		return new SynchronisationListAll(m, countTo);
	}

	@RequestMapping(value = "/{partner}", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	public void synchronise(@PathVariable("partner") String partner, @RequestBody List<String> prpReferences) {
		serviceSynchronisation.createOrUpdate(partner, prpReferences);
	}
}