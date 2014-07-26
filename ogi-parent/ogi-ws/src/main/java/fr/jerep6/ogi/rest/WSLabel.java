package fr.jerep6.ogi.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.enumeration.EnumLabelType;
import fr.jerep6.ogi.exception.business.EntityAlreadyExist;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.service.ServiceLabel;
import fr.jerep6.ogi.transfert.bean.LabelTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/label", produces = "application/json;charset=UTF-8")
public class WSLabel extends AbtractWS {

	@Autowired
	private ServiceLabel	serviceLabel;

	@Autowired
	private OrikaMapper		mapper;

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
	public LabelTo labelAdd(@RequestBody LabelTo label) {
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
	@RequestMapping(value = "/{type}", method = RequestMethod.GET)
	public List<LabelTo> readbyCode(@PathVariable("type") String code) {
		List<Label> labels = serviceLabel.readByType(EnumLabelType.valueOfByCode(code));

		List<LabelTo> LabelTo = mapper.mapAsList(labels, LabelTo.class);
		return LabelTo;
	}

}