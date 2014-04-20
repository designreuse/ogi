package fr.jerep6.ogi.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.common.base.Preconditions;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import fr.jerep6.ogi.enumeration.EnumDocumentType;
import fr.jerep6.ogi.service.ServiceDocument;
import fr.jerep6.ogi.transfert.FileUpload;
import fr.jerep6.ogi.transfert.bean.FileUploadTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@Controller
@Path("/document")
public class WSDocument extends AbstractJaxRsWS {
	Logger					LOGGER	= LoggerFactory.getLogger(WSDocument.class);

	@Autowired
	private ServiceDocument	serviceDocument;

	@Autowired
	private OrikaMapper		mapper;

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
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Map<String, List<FileUploadTo>> uploadFile( //
			@FormDataParam("file[]") InputStream uploadedInputStream,//
			@FormDataParam("file[]") FormDataContentDisposition fileDetail, //
			@FormDataParam("reference") String reference,//
			@FormDataParam("type") String type) throws IOException {
		Preconditions.checkNotNull(reference);
		Preconditions.checkNotNull(type);

		EnumDocumentType enumType = EnumDocumentType.valueOfByCode(type);

		// Copy uploaded file into photo directory
		// Convert iso filename in utf8. Je ne suis pas arrivé à envoyer le nom du fichier en utf8
		String fileName = new String(fileDetail.getFileName().getBytes("iso-8859-1"), "UTF-8");
		FileUpload f = serviceDocument.copyToDirectory(uploadedInputStream, fileName, reference, enumType);

		Map<String, List<FileUploadTo>> m = new HashMap<>();
		m.put("files", Arrays.asList(mapper.map(f, FileUploadTo.class)));
		return m;
	}
}