package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.dao.DaoDescription;
import fr.jerep6.ogi.service.ServiceDescription;

@Service("serviceDescription")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDescriptionImpl extends AbstractTransactionalService<Description, Integer> implements
		ServiceDescription {
	private static Logger	LOGGER	= LoggerFactory.getLogger(ServiceDescriptionImpl.class);

	@Autowired
	private DaoDescription	daoDescription;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDescription);
	}

}