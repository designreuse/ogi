package fr.jerep6.ogi.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@Controller
@Path("/property")
public class WSRealProperty extends AbstractJaxRsWS {

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private OrikaMapper			mapper;

	@POST
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public RealPropertyTo create(RealPropertyTo rp) {
		// Map into business object. Fulfill only business field. I.E technical field will be retrieve on database
		// before record. I should have proceed with a another dto but afterwards
		RealProperty property = mapper.map(rp, RealProperty.class);
		property = serviceRealProperty.createFromBusinessFields(property);

		return mapper.map(property, RealPropertyTo.class);
	}

	@GET
	@Path("/{reference}")
	@Produces(APPLICATION_JSON_UTF8)
	public RealPropertyTo read(@PathParam("reference") String reference) {
		RealProperty realProperty = serviceRealProperty.readByReference(reference);

		RealPropertyTo rpt = mapper.map(realProperty, RealPropertyTo.class);
		return rpt;
	}
}