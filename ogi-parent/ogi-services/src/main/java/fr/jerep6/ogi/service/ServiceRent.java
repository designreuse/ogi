package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Rent;

public interface ServiceRent extends TransactionalService<Rent, Integer> {
	Rent merge(Rent rentOriginalBD, Rent rentModif);
}
