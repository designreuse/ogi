package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.framework.service.Service;

public interface ServiceSynchronisation extends Service {

	void createOrUpdate(String partner, List<String> prpReferences);

	void delete(String partner, List<String> prpReferences);
}
