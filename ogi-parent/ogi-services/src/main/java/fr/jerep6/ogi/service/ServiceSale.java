package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Sale;

public interface ServiceSale extends TransactionalService<Sale, Integer> {

	Sale merge(Sale saleOriginalBD, Sale saleModif);

}
