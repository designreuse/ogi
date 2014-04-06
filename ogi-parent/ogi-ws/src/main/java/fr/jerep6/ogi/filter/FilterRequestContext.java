package fr.jerep6.ogi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jerep6.ogi.framework.utils.ContextUtils;

/**
 * Put into ContextUtils.threadLocalRequestURI
 * Exemple : http://localhost:8080
 * 
 * @author jerep6 6 avr. 2014
 * 
 */
public class FilterRequestContext implements Filter {
	private final Logger	LOGGER	= LoggerFactory.getLogger(FilterRequestContext.class);

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		// Craft server uri
		StringBuilder domainName = new StringBuilder();
		domainName.append("http://");
		domainName.append(request.getServerName());
		domainName.append(":");
		domainName.append(request.getServerPort());

		LOGGER.debug("Domain name from request = {}", domainName.toString());

		ContextUtils.threadLocalRequestURI.set(domainName.toString());

		// Pass request back down the filter chain
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}