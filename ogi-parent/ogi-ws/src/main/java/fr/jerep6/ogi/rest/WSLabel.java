package fr.jerep6.ogi.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.enumeration.EnumLabelType;
import fr.jerep6.ogi.exception.business.EntityAlreadyExist;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.service.ServiceLabel;
import fr.jerep6.ogi.transfert.bean.LabelTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@Controller
@Path("/label")
public class WSLabel extends AbstractJaxRsWS {

	@Autowired
	private ServiceLabel	serviceLabel;

	@Autowired
	private OrikaMapper		mapper;

	@POST
	@Consumes(APPLICATION_JSON_UTF8)
	@Produces(APPLICATION_JSON_UTF8)
	public LabelTo labelAdd(LabelTo label) {
		Label l = null;
		Label bo = mapper.map(label, Label.class);
		try {
			l = serviceLabel.add(bo);
		} catch (EntityAlreadyExist eae) {
			l = serviceLabel.read(bo.getType(), bo.getLabel());
		}

		return mapper.map(l, LabelTo.class);
	}

	/**
	 * @param code
	 *            category code
	 * @return
	 */
	@GET
	@Path("/{type}")
	@Produces(APPLICATION_JSON_UTF8)
	public List<LabelTo> readbyCode(@PathParam("type") String code) {
		List<Label> labels = serviceLabel.readByType(EnumLabelType.valueOfByCode(code));

		List<LabelTo> LabelTo = mapper.mapAsList(labels, LabelTo.class);
		return LabelTo;
	}

}