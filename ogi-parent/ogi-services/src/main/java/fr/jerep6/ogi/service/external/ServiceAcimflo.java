package fr.jerep6.ogi.service.external;

import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.transfert.WSResult;

public interface ServiceAcimflo extends Service {

	WSResult createOrUpdate(RealProperty prp);

	Boolean exist(String prpReference);

	WSResult delete(String prpReference);
}
