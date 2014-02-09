package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.transfert.WSResult;

public interface ServiceSynchronisation extends Service {

	List<WSResult> createOrUpdate(String partner, List<String> prpReferences);

	Boolean exist(String partner, String prpReference);

	List<WSResult> delete(String partner, List<String> prpReferences);
}
