package fr.jerep6.ogi.rest.external;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.rest.AbstractJaxRsWS;
import fr.jerep6.ogi.service.ServiceSynchronisation;

@Controller
@Path("/synchronisation")
public class WSSynchronisation extends AbstractJaxRsWS {

	@Autowired
	private ServiceSynchronisation	serviceSynchronisation;

	@POST
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public void synchronise(List<String> prpReferences) {

		serviceSynchronisation.createOrUpdate(prpReferences);
	}
}