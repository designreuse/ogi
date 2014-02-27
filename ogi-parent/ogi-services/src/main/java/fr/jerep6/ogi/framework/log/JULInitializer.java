package fr.jerep6.ogi.framework.log;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import fr.jerep6.ogi.framework.spring.CryptablePropertyPlaceholderConfigurer;

/**
 * Init java.util.logging to slf4j
 * 
 * http://blog.cn-consult.dk/2009/03/bridging-javautillogging-to-slf4j.html
 * 
 * @author jerep6 27 févr. 2014
 */
public class JULInitializer {
	private static Logger	LOGGER	= LoggerFactory.getLogger(CryptablePropertyPlaceholderConfigurer.class);

	@PostConstruct
	private void init() {
		LOGGER.info("INIT JULL to SLF4J logger");
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

}
