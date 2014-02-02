package fr.jerep6.ogi.service;

import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Address;

public interface ServiceAddress extends TransactionalService<Address, Integer> {

	Set<Address> merge(Set<Address> addressesBD, Set<Address> addressesModif);
}
