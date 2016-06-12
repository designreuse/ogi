package fr.jerep6.ogi.service;

import java.util.List;
import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.Visit;
import fr.jerep6.ogi.transfert.ListResult;

public interface ServiceVisit extends TransactionalService<Visit, Integer> {
	List<Visit> readByProperty(String prpRef);
	Visit createOrUpdate(Visit owner);
}
