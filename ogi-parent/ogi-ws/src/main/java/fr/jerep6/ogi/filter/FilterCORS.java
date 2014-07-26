package fr.jerep6.ogi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class FilterCORS implements Filter {

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
	ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		response.setHeader("Access-Control-Max-Age", "3600");
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) {}

}

/**
 * All CORS related headers are prefixed with "Access-Control-". Here’s some more details about each header.
 *
 * Access-Control-Allow-Origin (required) - This header must be included in all valid CORS responses; omitting the
 * header will cause the CORS request to fail. The value of the header can either echo the Origin request header (as
 * in the example above), or be a '*' to allow requests from any origin. If you’d like any site to be able to access
 * your data, using '*' is fine. But if you’d like finer control over who can access your data, use an actual value
 * in the header.
 *
 * Access-Control-Allow-Credentials (optional) - By default, cookies are not included in CORS requests. Use this
 * header to indicate that cookies should be included in CORS requests. The only valid value for this header is true
 * (all lowercase). If you don't need cookies, don't include this header (rather than setting its value to false).
 *
 * The Access-Control-Allow-Credentials header works in conjunction with the withCredentials property on the
 * XMLHttpRequest 2 object. Both these properties must be set to true in order for the CORS request to succeed. If
 * .withCredentials is true, but there is no Access-Control-Allow-Credentials header, the request will fail (and
 * vice versa).
 *
 * Its recommended that you don’t set this header unless you are sure you want cookies to be included in CORS
 * requests.
 *
 * Access-Control-Expose-Headers (optional) - The XMLHttpRequest 2 object has a getResponseHeader() method that
 * returns the value of a particular response header. During a CORS request, the getResponseHeader() method can only
 * access simple response headers. Simple response headers are defined as follows:
 *
 * Cache-Control
 * Content-Language
 * Content-Type
 * Expires
 * Last-Modified
 * Pragma
 */
