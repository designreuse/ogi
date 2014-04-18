package fr.jerep6.ogi.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.common.base.Preconditions;

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

	@DELETE
	@Produces(APPLICATION_JSON_UTF8)
	public void delete(@QueryParam("ref") List<String> reference) {
		Preconditions.checkNotNull(reference);

		serviceRealProperty.delete(reference);
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public Collection<RealPropertyTo> listAll() {
		Collection<RealProperty> properties = serviceRealProperty.listAll();

		Collection<RealPropertyTo> rpt = mapper.mapAsList(properties, RealPropertyTo.class);
		return rpt;
	}

	@GET
	@Path("/{reference}")
	@Produces(APPLICATION_JSON_UTF8)
	public RealPropertyTo read(@PathParam("reference") String reference) {
		RealProperty realProperty = serviceRealProperty.readByReference(reference);

		RealPropertyTo rpt = mapper.map(realProperty, RealPropertyTo.class);
		return rpt;
	}

	@PUT
	@Path("/{reference}")
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public RealPropertyTo update(RealPropertyTo rp, @PathParam("reference") String reference) {
		// Map into business object. Fulfill only business field. I.E technical field will be retrieve on database
		// before record. I should have proceed with a another dto but afterwards
		RealProperty property = mapper.map(rp, RealProperty.class);
		property = serviceRealProperty.updateFromBusinessFields(reference, property);

		return mapper.map(property, RealPropertyTo.class);
	}
}