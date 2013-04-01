package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;

@Service("sp")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceRealPropertyImpl extends AbstractTransactionalService<RealProperty, Integer> implements
		ServiceRealProperty {

	@Autowired
	private DaoProperty	daoProperty;

	@Value("${search.result.max}")
	private Integer		searchMaxResult;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoProperty);
	}

	@Override
	public void readByReference(String reference) {
		daoProperty.readByReference(reference);
	}

}
