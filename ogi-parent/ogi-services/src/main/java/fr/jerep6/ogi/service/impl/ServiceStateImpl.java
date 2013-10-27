package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.persistance.dao.DaoState;
import fr.jerep6.ogi.service.ServiceState;

@Service("serviceState")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceStateImpl extends AbstractTransactionalService<State, Integer> implements ServiceState {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceStateImpl.class);

	@Autowired
	private DaoState		daoState;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoState);
	}

}