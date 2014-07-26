package fr.jerep6.ogi.rest.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jerep6.ogi.framework.transfert.ExceptionTo;
import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * Convert exception not catch into JSON
 *
 * @author jerep6
 */
public class JsonExceptionMapper {
	Logger	LOGGER	= LoggerFactory.getLogger(JsonExceptionMapper.class);

	public void toResponse(Exception exception) {
		LOGGER.error("exception", exception);

		// Convert exception into light object
		OrikaMapper mapper = ContextUtils.getBean(OrikaMapper.class);
		ExceptionTo error = mapper.map(exception, ExceptionTo.class);

		// return Response//
		// .status(Response.Status.INTERNAL_SERVER_ERROR)//
		// .entity(JSONUtils.toJson(error))//
		// .type(AbstractJaxRsWS.APPLICATION_JSON_UTF8)//
		// .build();
	}
}