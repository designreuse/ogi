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

import fr.jerep6.ogi.rest.AbstractJaxRsWS;
import fr.jerep6.ogi.service.ServiceSynchronisation;
import fr.jerep6.ogi.transfert.WSResult;

@Controller
@Path("/synchronisation")
public class WSSynchronisation extends AbstractJaxRsWS {

	@Autowired
	private ServiceSynchronisation	serviceSynchronisation;

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
	public Map<String, Boolean> get(@PathParam("partner") String partner, @PathParam("ref") String prpReference) {
		Boolean b = serviceSynchronisation.exist(partner, prpReference);

		HashMap<String, Boolean> result = new HashMap<>(1);
		result.put("exist", b);
		return result;
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