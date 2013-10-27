package fr.jerep6.ogi.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.service.ServiceState;
import fr.jerep6.ogi.transfert.bean.StateTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@Controller
@Path("/state")
public class WSState extends AbstractJaxRsWS {

	@Autowired
	private ServiceState	serviceState;

	@Autowired
	private OrikaMapper		mapper;

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	public List<StateTo> readbyCode(@PathParam("type") String code) {
		Collection<State> states = serviceState.listAll();

		List<StateTo> statesTo = mapper.mapAsList(states, StateTo.class);
		return statesTo;
	}
}