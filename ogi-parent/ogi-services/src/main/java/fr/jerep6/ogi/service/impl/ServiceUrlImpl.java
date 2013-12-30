package fr.jerep6.ogi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.service.ServiceUrl;
import fr.jerep6.ogi.utils.MyUrlUtils;

@Service("serviceUrl")
public class ServiceUrlImpl extends AbstractService implements ServiceUrl {
	private final Logger		LOGGER			= LoggerFactory.getLogger(ServiceUrlImpl.class);

	private static final String	PATH_PROPERTY	= "property";

	@Value("${rest.url}")
	private String				restUrl;

	@Value("${photos.url}")
	private String				urlBasePhoto;

	@Override
	public String urlDocument(String p) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(p));

		StringBuilder sb = new StringBuilder();
		sb.append(urlBasePhoto).append("/");
		sb.append(MyUrlUtils.replace(p.toString()));
		return sb.toString();
	}

	@Override
	public String urlProperty(String reference) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));

		StringBuilder sb = new StringBuilder();
		sb.append(restUrl).append("/");
		sb.append(PATH_PROPERTY).append("/");
		sb.append(reference);

		return sb.toString();
	}

}
