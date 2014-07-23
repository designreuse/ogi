package fr.jerep6.ogi.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.service.ServiceState;
import fr.jerep6.ogi.transfert.bean.StateTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/state", produces = "application/json;charset=UTF-8")
public class WSState extends AbtractWS {

	@Autowired
	private ServiceState	serviceState;

	@Autowired
	private OrikaMapper		mapper;

	@RequestMapping(method = RequestMethod.GET)
	public List<StateTo> list() {
		Collection<State> states = serviceState.listAll();

		List<StateTo> statesTo = mapper.mapAsList(states, StateTo.class);
		return statesTo;
	}
}