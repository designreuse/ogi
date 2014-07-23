package fr.jerep6.ogi.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.service.ServiceOwner;
import fr.jerep6.ogi.transfert.ListResult;
import fr.jerep6.ogi.transfert.bean.OwnerTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/owner", produces = "application/json;charset=UTF-8")
public class WSOwner extends AbtractWS {

	@Autowired
	private ServiceOwner	serviceOwner;

	@Autowired
	private OrikaMapper		mapper;

	@RequestMapping(value = "/{techid}/property/{prpRef}", method = RequestMethod.PUT)
	public void addLink(@PathVariable("techid") Integer ownerTechid, @PathVariable("prpRef") String prpRef) {
		Preconditions.checkNotNull(ownerTechid);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpRef));

		serviceOwner.addProperty(ownerTechid, prpRef);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	public OwnerTo create(@RequestBody OwnerTo owner) {
		Preconditions.checkNotNull(owner);
		Owner ownersBo = mapper.map(owner, Owner.class);
		// Pas d'update directement avec l'objet recu de la requete car sinon hibernate supprime l'adresse
		// puis la recrée.
		Owner ownerInsert = serviceOwner.createOrUpdate(ownersBo);
		return mapper.map(ownerInsert, OwnerTo.class);
	}

	@RequestMapping(value = "/{techid}/property/{prpRef}", method = RequestMethod.DELETE)
	public void deleteLink(@PathVariable("techid") Integer ownerTechid, @PathVariable("prpRef") String prpRef) {
		Preconditions.checkNotNull(ownerTechid);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpRef));

		serviceOwner.deleteProperty(ownerTechid, prpRef);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void deleteOwners(@RequestParam("ref") List<Integer> techids) {
		Preconditions.checkNotNull(techids);

		serviceOwner.removeByPrimaryKey(techids);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ListResult<OwnerTo> list(@RequestParam(value = "pageNumber", required = false) Integer pageNumber, //
			@RequestParam(value = "itemNumberPerPage", required = false) Integer itemNumberPerPage, //
			@RequestParam(value = "sortBy", required = false) String sortBy, //
			@RequestParam(value = "sortDir", required = false) String sortDir) {

		// Read owner
		ListResult<Owner> result = serviceOwner.list(pageNumber, itemNumberPerPage, sortBy, sortDir);

		return mapper.map(result, ListResult.class);
	}

	@RequestMapping(value = "/{techid}", method = RequestMethod.GET)
	public OwnerTo read(@PathVariable("techid") Integer techid) {
		Owner owner = serviceOwner.read(techid);
		OwnerTo ownerTo = mapper.map(owner, OwnerTo.class);
		return ownerTo;
	}

	@RequestMapping(value = "/property/{prpRef}", method = RequestMethod.GET)
	public List<OwnerTo> readByProperty(@PathVariable("prpRef") String prpRef) {
		List<Owner> owners = serviceOwner.readByProperty(prpRef);

		List<OwnerTo> ownersTo = mapper.mapAsList(owners, OwnerTo.class);
		return ownersTo;
	}

	@RequestMapping(value = "/{techid}", method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
	public OwnerTo update(@PathVariable("techid") Integer techid, @RequestBody OwnerTo owner) {
		Preconditions.checkNotNull(owner);
		Preconditions.checkArgument(techid.equals(owner.getTechid()));

		Owner ownersBo = mapper.map(owner, Owner.class);
		// Pas d'update directement avec l'objet recu de la requete car sinon hibernate supprime l'adresse
		// puis la recrée.
		serviceOwner.createOrUpdate(ownersBo);
		return owner;
	}
}