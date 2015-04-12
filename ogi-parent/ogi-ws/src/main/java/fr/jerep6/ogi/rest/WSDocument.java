package fr.jerep6.ogi.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;

import fr.jerep6.ogi.service.ServiceDocument;
import fr.jerep6.ogi.transfert.FileUpload;
import fr.jerep6.ogi.transfert.bean.FileUploadTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;
import fr.jerep6.ogi.utils.OSUtils;

@RestController
@RequestMapping(value = "/document", produces = "application/json;charset=UTF-8")
public class WSDocument extends AbtractWS {
	Logger					LOGGER	= LoggerFactory.getLogger(WSDocument.class);

	@Autowired
	private ServiceDocument	serviceDocument;

	@Autowired
	private OrikaMapper		mapper;

	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	/**
	 * Note that the response should always be a JSON object containing a files array even if only one file is uploaded.
	 *
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param reference
	 *            property reference
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public Map<String, List<FileUploadTo>> uploadFile( //
			@RequestParam("file[]") Part part, //
			@RequestParam("reference") String reference,//
			@RequestParam("type") String type) throws IOException {

		Preconditions.checkNotNull(reference);
		Preconditions.checkNotNull(type);

		// Copy uploaded file into photo directory
		String fileName = getFileName(part);
		if(OSUtils.isWindows()) {
			// Convert iso filename in utf8. Je ne suis pas arrivé à envoyer le nom du fichier en utf8 sous windows
			fileName = new String(fileName.getBytes("iso-8859-1"), "UTF-8");
		}
		FileUpload f = serviceDocument.copyToDirectory(part.getInputStream(), fileName, reference, type);

		Map<String, List<FileUploadTo>> m = new HashMap<>();
		m.put("files", Arrays.asList(mapper.map(f, FileUploadTo.class)));
		return m;
	}
}