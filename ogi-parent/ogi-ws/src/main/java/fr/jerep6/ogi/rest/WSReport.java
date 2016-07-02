package fr.jerep6.ogi.rest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumGestionMode;
import fr.jerep6.ogi.enumeration.EnumPageSize;
import fr.jerep6.ogi.enumeration.EnumReport;
import fr.jerep6.ogi.service.ServiceReport;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/report", produces = "application/json;charset=UTF-8")
public class WSReport extends AbtractWS {

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

	@RequestMapping(value = "/{prpRef}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> generateShopFront(//
			@PathVariable("prpRef") String prpReference, //
			@RequestParam("type") String type, //
			@RequestParam("format") String format, //
			@RequestParam("pageSize") String pageSize,
			@RequestParam("gestionMode") String gestionMode
			) throws Exception {

		EnumPageSize enumPageFormat = Strings.isNullOrEmpty(pageSize) ? null : EnumPageSize.valueOfByCode(pageSize);
		ByteArrayOutputStream generateShopFront = serviceReport.generate(prpReference, EnumReport.valueOfByName(type),
				format, enumPageFormat, EnumGestionMode.valueOfByCode(gestionMode));

		String fileName = prpReference + "." + mimeType.get(format).getExtension();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(mimeType.get(format).getMime()));
		headers.setContentDispositionFormData(fileName, fileName);
		return new ResponseEntity<byte[]>(generateShopFront.toByteArray(), headers, HttpStatus.OK);

	}
}