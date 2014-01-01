package fr.jerep6.ogi.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

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
	@Path("{techid}/property/{prpRef}")
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public void addLink(@PathParam("techid") Integer ownerTechid, @PathParam("prpRef") String prpRef) {
		Preconditions.checkNotNull(ownerTechid);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpRef));

		serviceOwner.addProperty(ownerTechid, prpRef);
	}

	@DELETE
	@Path("{techid}/property/{prpRef}")
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public void deleteLink(@PathParam("techid") Integer ownerTechid, @PathParam("prpRef") String prpRef) {
		Preconditions.checkNotNull(ownerTechid);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpRef));

		serviceOwner.deleteProperty(ownerTechid, prpRef);
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public Collection<OwnerTo> listAll() {
		Collection<Owner> owners = serviceOwner.listAll();

		Collection<OwnerTo> ownersTo = mapper.mapAsList(owners, OwnerTo.class);
		return ownersTo;
	}

	@GET
	@Path("{techid}")
	@Produces(APPLICATION_JSON_UTF8)
	public OwnerTo read(@PathParam("techid") Integer techid) {
		Owner owner = serviceOwner.read(techid);
		OwnerTo ownerTo = mapper.map(owner, OwnerTo.class);
		return ownerTo;
	}

	@GET
	@Path("/property/{prpRef}")
	@Produces(APPLICATION_JSON_UTF8)
	public List<OwnerTo> readByProperty(@PathParam("prpRef") String prpRef) {
		List<Owner> owners = serviceOwner.readByProperty(prpRef);

		List<OwnerTo> ownersTo = mapper.mapAsList(owners, OwnerTo.class);
		return ownersTo;
	}

	@PUT
	@Path("/{techid}")
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public OwnerTo update(@PathParam("techid") Integer techid, OwnerTo owner) {
		Preconditions.checkNotNull(owner);
		Preconditions.checkArgument(techid.equals(owner.getTechid()));

		Owner ownersBo = mapper.map(owner, Owner.class);
		serviceOwner.update(ownersBo);
		return owner;
	}
}