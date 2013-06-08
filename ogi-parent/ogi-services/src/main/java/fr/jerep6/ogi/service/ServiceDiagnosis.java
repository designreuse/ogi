package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Diagnosis;

public interface ServiceDiagnosis extends TransactionalService<Diagnosis, Integer> {
	/**
	 * Read a diagnosis by this label
	 * 
	 * @param code
	 * @return null if nothing match label
	 */
	Diagnosis readByLabel(String label);
}
