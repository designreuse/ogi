package fr.jerep6.ogi.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.service.ServiceSale;
import fr.jerep6.ogi.transfert.ExpiredMandate;

/**
 * @author jerep6
 */
@Controller
@Path("/mandate")
public class WSMandate extends AbstractJaxRsWS {

	@Autowired
	private ServiceSale	serviceSale;

	@GET
	@Path("/expired")
	@Produces(APPLICATION_JSON_UTF8)
	public Map<String, List<ExpiredMandate>> listExpired() {
		return serviceSale.listExpiredMandates();

	}
}