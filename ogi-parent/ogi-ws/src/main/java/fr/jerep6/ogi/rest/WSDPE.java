package fr.jerep6.ogi.rest;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.service.ServiceDPE;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@Controller
@Path("/dpe")
public class WSDPE extends AbstractJaxRsWS {

	@Autowired
	private ServiceDPE			serviceDPE;

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private OrikaMapper			mapper;

	/**
	 * @param code
	 *            category code
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/kwh/{ref}")
	@Produces("image/png")
	public Response readbyCode(@PathParam("ref") String prpReference, @QueryParam("witdh") Integer width)
			throws IOException {
		serviceRealProperty.readByReference(prpReference);

		DPE d = new DPE();
		d.setKWh(267);
		BufferedImage generateDPEkWhImage = serviceDPE.generateDPEkWhImage(d, width);

		return Response.ok(toByte(generateDPEkWhImage, "png")).build();
	}

}