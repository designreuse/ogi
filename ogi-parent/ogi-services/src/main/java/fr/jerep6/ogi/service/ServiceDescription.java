package fr.jerep6.ogi.service;

import java.util.Set;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Description;

public interface ServiceDescription extends TransactionalService<Description, Integer> {

	Set<Description> merge(Set<Description> descriptionsBD, Set<Description> descriptionsModif);
}
