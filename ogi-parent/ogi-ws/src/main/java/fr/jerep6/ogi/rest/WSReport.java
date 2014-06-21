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

import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumPageSize;
import fr.jerep6.ogi.enumeration.EnumReport;
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
	 * Configuration for formats.
	 * Given a name of format return mime type and file extension
	 */
	private static Map<String, FormatConfiguration>	mimeType	= new HashMap<>();
	static {
		mimeType.put(ServiceReport.FORMAT_PDF, new FormatConfiguration("application/pdf", "pdf"));
		mimeType.put(ServiceReport.FORMAT_WORD, new FormatConfiguration(
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"));
		mimeType.put(ServiceReport.FORMAT_ODT,
				new FormatConfiguration("application/vnd.oasis.opendocument.text", "odt"));
	}

	@Autowired
	private ServiceReport							serviceReport;

	@GET
	@Path("/{prpRef}")
	public Response generateShopFront(//
			@PathParam("prpRef") String prpReference, //
			@QueryParam("type") String type, //
			@QueryParam("format") String format, //
			@QueryParam("pageSize") String pageSize) throws Exception {

		EnumPageSize enumPageFormat = Strings.isNullOrEmpty(pageSize) ? null : EnumPageSize.valueOfByCode(pageSize);
		ByteArrayOutputStream generateShopFront = serviceReport.generate(prpReference, EnumReport.valueOfByName(type),
				format, enumPageFormat);

		return Response
				.ok(generateShopFront.toByteArray())
				.type(mimeType.get(format).getMime())
				.header("Content-Disposition",
						"attachment; filename=" + prpReference + "." + mimeType.get(format).getExtension())//
						.build();
	}
}