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
import fr.jerep6.ogi.persistance.bo.Visit;
import fr.jerep6.ogi.service.ServiceOwner;
import fr.jerep6.ogi.service.ServiceVisit;
import fr.jerep6.ogi.transfert.ListResult;
import fr.jerep6.ogi.transfert.bean.OwnerTo;
import fr.jerep6.ogi.transfert.bean.VisitTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/visit", produces = "application/json;charset=UTF-8")
public class WSVisit extends AbtractWS {

	@Autowired
	private ServiceVisit	serviceVisit;

	@Autowired
	private OrikaMapper		mapper;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	public VisitTo create(@RequestBody VisitTo visit) {
		Preconditions.checkNotNull(visit);
		Visit visitBo = mapper.map(visit, Visit.class);
		Visit visiInsert = serviceVisit.createOrUpdate(visitBo);
		return mapper.map(visiInsert, VisitTo.class);
	}

	@RequestMapping(value = "/{prpRef}", method = RequestMethod.GET)
	public List<VisitTo> readByProperty(@PathVariable("prpRef") String prpRef) {
		List<Visit> visits = serviceVisit.readByProperty(prpRef);

		List<VisitTo> visitsTo = mapper.mapAsList(visits, VisitTo.class);
		return visitsTo;
	}

	@RequestMapping(value = "/{techid}", method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
	public VisitTo update(@PathVariable("techid") Integer techid, @RequestBody VisitTo visit) {
		Preconditions.checkNotNull(visit);
		Preconditions.checkArgument(techid.equals(visit.getTechid()));

		Visit visitBo = mapper.map(visit, Visit.class);
		serviceVisit.createOrUpdate(visitBo);
		return visit;
	}
}