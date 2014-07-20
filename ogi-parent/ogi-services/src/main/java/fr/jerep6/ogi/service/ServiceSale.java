package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Map;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.transfert.ExpiredMandate;

public interface ServiceSale extends TransactionalService<Sale, Integer> {

	Sale merge(Sale saleOriginalBD, Sale saleModif);

	/**
	 * List mandates whose endDate is over or soon over
	 *
	 *
	 * @return key from map => EXPIRED or SOON_EXPIRED
	 */
	Map<String, List<ExpiredMandate>> listExpiredMandates();

}
