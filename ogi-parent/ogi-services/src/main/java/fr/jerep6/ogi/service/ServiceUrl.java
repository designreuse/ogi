package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.Service;

public interface ServiceUrl extends Service {

	String urlProperty(String reference);

	String urlDocument(String p);
}
