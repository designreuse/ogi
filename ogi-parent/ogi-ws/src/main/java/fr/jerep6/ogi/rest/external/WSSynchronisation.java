package fr.jerep6.ogi.rest.external;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.rest.AbstractJaxRsWS;
import fr.jerep6.ogi.service.ServicePartnerRequest;
import fr.jerep6.ogi.service.ServiceSynchronisation;
import fr.jerep6.ogi.transfert.WSResult;
import fr.jerep6.ogi.transfert.bean.PartnerRequestTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@Controller
@Path("/synchronisation")
public class WSSynchronisation extends AbstractJaxRsWS {

	@Autowired
	private ServiceSynchronisation	serviceSynchronisation;

	@Autowired
	private ServicePartnerRequest	servicePartnerRequest;

	@Autowired
	private OrikaMapper				mapper;

	@DELETE
	@Path("/{partner}")
	@Produces(APPLICATION_JSON_UTF8)
	public List<WSResult> delete(@PathParam("partner") String partner, @QueryParam("ref") List<String> prpReferences) {
		List<WSResult> results = serviceSynchronisation.delete(partner, prpReferences);
		return results;
	}

	@GET
	@Path("/{partner}/{ref}")
	@Produces(APPLICATION_JSON_UTF8)
	public Map<String, Object> get(@PathParam("partner") String partner, @PathParam("ref") String prpReference) {
		Boolean b = serviceSynchronisation.exist(partner, prpReference);

		PartnerRequest lastRequest = servicePartnerRequest
				.lastRequest(EnumPartner.valueOfByCode(partner), prpReference);
		PartnerRequestTo lastRequestTo = mapper.map(lastRequest, PartnerRequestTo.class);

		HashMap<String, Object> result = new HashMap<>(1);
		result.put("exist", b);
		result.put("lastRequest", lastRequestTo);
		return result;
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public Map<String, List<PartnerRequest>> listAll() {
		Map<String, List<PartnerRequest>> lastRequests = servicePartnerRequest.lastRequests();

		// Map<String, List<PartnerRequestTo>> convertMap = mapper.mapAsMap(lastRequests,
		// new TypeBuilder<Map<String, List<PartnerRequest>>>() {}.build(),
		// new TypeBuilder<Map<String, List<PartnerRequestTo>>>() {}.build());

		return lastRequests;
	}

	@POST
	@Path("/{partner}")
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public List<WSResult> synchronise(@PathParam("partner") String partner, List<String> prpReferences) {
		List<WSResult> results = serviceSynchronisation.createOrUpdate(partner, prpReferences);
		return results;
	}
}