package fr.jerep6.ogi.rest.provider;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.transfert.ExceptionTo;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	private Logger		LOGGER	= LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private OrikaMapper	mapper;

	@ExceptionHandler({ Exception.class, MultipleBusinessException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionTo defaultErrorHandler(HttpServletRequest req, Exception e) {
		LOGGER.error("exception", e);

		// Convert exception into light object
		ExceptionTo error = mapper.map(e, ExceptionTo.class);
		return error;
	}

}