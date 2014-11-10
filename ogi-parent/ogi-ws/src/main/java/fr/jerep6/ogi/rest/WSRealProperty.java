package fr.jerep6.ogi.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;

import fr.jerep6.ogi.event.EventCreateRealProperty;
import fr.jerep6.ogi.event.EventUpdateRealProperty;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.ListResult;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@RestController
@RequestMapping(value = "/property", produces = "application/json;charset=UTF-8")
public class WSRealProperty extends AbtractWS {

	@Autowired
	private ApplicationEventPublisher	eventPublisher;

	@Autowired
	private ServiceRealProperty			serviceRealProperty;

	@Autowired
	private OrikaMapper					mapper;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	public RealPropertyTo create(@RequestBody RealPropertyTo rp) {
		// Map into business object. Fulfill only business field. I.E technical field will be retrieve on database
		// before record. I should have proceed with a another dto but afterwards
		RealProperty property = mapper.map(rp, RealProperty.class);
		property = serviceRealProperty.createFromBusinessFields(property);

		// publish event here and not in service due to transactions
		eventPublisher.publishEvent(new EventCreateRealProperty(this, property));

		return mapper.map(property, RealPropertyTo.class);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam("ref") List<String> reference) {
		Preconditions.checkNotNull(reference);

		serviceRealProperty.delete(reference);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ListResult<RealPropertyTo> list(//
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber, //
			@RequestParam(value = "itemNumberPerPage", required = false) Integer itemNumberPerPage, //
			@RequestParam(value = "sortBy", required = false) String sortBy, //
			@RequestParam(value = "sortDir", required = false) String sortDir) {

		// List properties according to criteria
		ListResult<RealProperty> result = serviceRealProperty.list(pageNumber, itemNumberPerPage, sortBy, sortDir);

		return mapper.map(result, ListResult.class);
	}

	@RequestMapping(value = "/{reference}", method = RequestMethod.GET)
	public RealPropertyTo read(@PathVariable("reference") String reference) {
		RealProperty realProperty = serviceRealProperty.readByReference(reference).get();

		RealPropertyTo rpt = mapper.map(realProperty, RealPropertyTo.class);
		return rpt;
	}

	@RequestMapping(value = "/{reference}", method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
	public RealPropertyTo update(@RequestBody RealPropertyTo rp, @PathVariable("reference") String reference) {
		// Map into business object. Fulfill only business field. I.E technical field will be retrieve on database
		// before record. I should have proceed with a another dto but afterwards
		RealProperty property = mapper.map(rp, RealProperty.class);
		property = serviceRealProperty.updateFromBusinessFields(reference, property);

		// publish event here and not in service due to transactions
		eventPublisher.publishEvent(new EventUpdateRealProperty(this, property));

		return mapper.map(property, RealPropertyTo.class);
	}
}