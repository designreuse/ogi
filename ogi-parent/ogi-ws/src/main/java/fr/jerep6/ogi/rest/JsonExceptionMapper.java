package fr.jerep6.ogi.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Convert exception not catch into JSON
 * 
 * @author jerep6
 */
@Provider
public class JsonExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		String msg = exception.getMessage() == null ? "" : exception.getMessage();
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"exception\":\"" + msg + "\"}")
				.type(AbstractJaxRsWS.APPLICATION_JSON_UTF8).build();
	}
}