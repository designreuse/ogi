package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface ServiceRealProperty extends TransactionalService<RealProperty, Integer> {
	/**
	 * Read a property by this business reference
	 * 
	 * @param reference
	 */
	void readByReference(String reference);
}
