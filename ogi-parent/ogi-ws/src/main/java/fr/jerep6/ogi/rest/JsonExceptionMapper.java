package fr.jerep6.ogi.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jerep6.ogi.framework.utils.ExceptionUtils;

/**
 * Convert exception not catch into JSON
 * 
 * @author jerep6
 */
@Provider
public class JsonExceptionMapper implements ExceptionMapper<Exception> {
	Logger	LOGGER	= LoggerFactory.getLogger(JsonExceptionMapper.class);

	@Override
	public Response toResponse(Exception exception) {
		LOGGER.error("exception", exception);
		String msg = exception.getMessage() == null ? "" : ExceptionUtils.i18n(exception);
		return Response//
				.status(Response.Status.INTERNAL_SERVER_ERROR)//
				.entity("{\"exception\":\"" + msg + "\"}")//
				.type(AbstractJaxRsWS.APPLICATION_JSON_UTF8)//
				.build();
	}
}