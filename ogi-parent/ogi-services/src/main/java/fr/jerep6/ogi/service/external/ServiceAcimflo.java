package fr.jerep6.ogi.service.external;

import java.io.IOException;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface ServiceAcimflo extends Service {

	void createOrUpdate(Set<RealProperty> properties) throws ClientProtocolException, IOException;

	Boolean exist(String prpReference);
}
