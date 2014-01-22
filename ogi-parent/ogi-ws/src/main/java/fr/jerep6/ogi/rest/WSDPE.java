package fr.jerep6.ogi.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

	@GET
	@Path("/ges")
	@Produces("image/png")
	public Response generateImgGes(@QueryParam("dpe") Integer dpeValue, @QueryParam("width") Integer width)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serviceDPE.generateDPEGesImage(baos, dpeValue, width);
		return Response.ok(baos.toByteArray()).build();
	}

	@GET
	@Path("/kwh")
	@Produces("image/png")
	public Response generateImgKwh(@QueryParam("dpe") Integer dpeValue, @QueryParam("width") Integer width)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serviceDPE.generateDPEkWhImage(baos, dpeValue, width);
		return Response.ok(baos.toByteArray()).build();
	}

}