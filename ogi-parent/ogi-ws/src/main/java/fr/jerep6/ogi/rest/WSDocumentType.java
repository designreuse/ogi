package fr.jerep6.ogi.rest;

import java.util.List;

import org.apache.logging.log4j.core.helpers.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;
import fr.jerep6.ogi.persistance.bo.DocumentType;
import fr.jerep6.ogi.service.ServiceDocument;
import fr.jerep6.ogi.transfert.bean.DocumentTypeTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@RestController
@RequestMapping(value = "/document/type", produces = "application/json;charset=UTF-8")
public class WSDocumentType extends AbtractWS {
	Logger					LOGGER	= LoggerFactory.getLogger(WSDocumentType.class);

	@Autowired
	private ServiceDocument	serviceDocument;

	@Autowired
	private OrikaMapper		mapper;

	@RequestMapping(method = RequestMethod.GET)
	public List<DocumentTypeTo> listAllDocumentType() {
		return listDocumentType(null);
	}

	/**
	 * List document type to display in an IHM zone
	 *
	 * @param zone
	 * @return
	 */
	@RequestMapping(value = "{zone}", method = RequestMethod.GET)
	public List<DocumentTypeTo> listDocumentType(@PathVariable("zone") String zone) {
		EnumDocumentZoneList enumZone = Strings.isNotEmpty(zone) ? EnumDocumentZoneList.valueOfByCode(zone) : null;
		List<DocumentType> documentsTypes = serviceDocument.listDocumentType(enumZone);
		return mapper.mapAsList(documentsTypes, DocumentTypeTo.class);
	}

	@RequestMapping(value = "zone", method = RequestMethod.GET)
	public List<String> listZone() {
		return mapper.mapAsList(EnumDocumentZoneList.values(), String.class);
	}
}