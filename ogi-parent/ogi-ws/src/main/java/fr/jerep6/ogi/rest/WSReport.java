package fr.jerep6.ogi.rest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.jerep6.ogi.service.ServiceReport;

/**
 * @author jerep6
 */
@Controller
@Path("/report")
public class WSReport extends AbstractJaxRsWS {

	@Getter
	@AllArgsConstructor
	private static class FormatConfiguration {
		private String	mime;
		private String	extension;

	}

	/**
	 * Configuration for format
	 */
	private static Map<String, FormatConfiguration>	mimeType	= new HashMap<>();
	static {
		mimeType.put(ServiceReport.FORMAT_PDF, new FormatConfiguration("application/pdf", "pdf"));
		mimeType.put(ServiceReport.FORMAT_WORD, new FormatConfiguration(
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"));
	}

	@Autowired
	private ServiceReport							serviceReport;

	@GET
	@Path("/{prpRef}")
	public Response generateShopFront(@PathParam("prpRef") String prpReference, @QueryParam("format") String format)
			throws Exception {
		ByteArrayOutputStream generateShopFront = serviceReport.generateShopFront(prpReference, format);
		return Response
				.ok(generateShopFront.toByteArray())
				.type(mimeType.get(format).getMime())
				.header("Content-Disposition",
						"attachment; filename=" + prpReference + "." + mimeType.get(format).getExtension())//
				.build();
	}
}