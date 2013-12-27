package fr.jerep6.ogi.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.service.ServiceOwner;
import fr.jerep6.ogi.transfert.bean.OwnerTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@Controller
@Path("/owner")
public class WSOwner extends AbstractJaxRsWS {

	@Autowired
	private ServiceOwner	serviceOwner;

	@Autowired
	private OrikaMapper		mapper;

	@PUT
	@Path("/property/{prpRef}")
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public List<OwnerTo> associate(@PathParam("prpRef") String prpRef, List<OwnerTo> owners) {
		List<Owner> ownersBo = mapper.mapAsList(owners, Owner.class);
		serviceOwner.createOrUpdate(ownersBo);
		serviceOwner.associate(prpRef, ownersBo);
		return owners;
	}

	@GET
	@Path("/property/{prpRef}")
	@Produces(APPLICATION_JSON_UTF8)
	public List<OwnerTo> readByProperty(@PathParam("prpRef") String prpRef) {
		List<Owner> owners = serviceOwner.readByProperty(prpRef);

		List<OwnerTo> ownersTo = mapper.mapAsList(owners, OwnerTo.class);
		return ownersTo;
	}

}