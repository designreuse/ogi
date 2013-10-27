package fr.jerep6.ogi.service;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.State;

public interface ServiceState extends TransactionalService<State, Integer> {}
