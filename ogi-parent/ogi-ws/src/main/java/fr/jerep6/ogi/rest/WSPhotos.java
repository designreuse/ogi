package fr.jerep6.ogi.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import fr.jerep6.ogi.service.ServicePhoto;
import fr.jerep6.ogi.transfert.FileUpload;

@Controller
@Path("/photo")
public class WSPhotos extends AbstractJaxRsWS {
	Logger					LOGGER	= LoggerFactory.getLogger(WSPhotos.class);

	@Autowired
	private ServicePhoto	servicePhoto;

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
	public List<FileUpload> uploadFile( //
			@FormDataParam("file[]") InputStream uploadedInputStream,//
			@FormDataParam("file[]") FormDataContentDisposition fileDetail, //
			@FormDataParam("reference") String reference) throws IOException {

		// Copy uploaded file into photo directory
		FileUpload f = servicePhoto.copyToPhotosDirectory(uploadedInputStream, fileDetail.getFileName(), reference);
		return Arrays.asList(f);
	}
}