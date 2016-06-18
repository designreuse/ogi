package fr.jerep6.ogi.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorVisit;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Visit;
import fr.jerep6.ogi.persistance.dao.DaoVisit;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceVisit;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceVisit")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceVisitImpl extends AbstractTransactionalService<Visit, Integer> implements ServiceVisit {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceVisitImpl.class);

	@Autowired
	private DaoVisit			daoVisit;
	
	@Autowired
	private ServiceRealProperty serviceProperty;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	public Visit createOrUpdate(String prpRef, Visit visit) {
		validate(visit);

		if (visit.getTechid() != null) {
			Visit visitBD = read(visit.getTechid());
			// Mapper don't map addresses and properties association
			mapper.map(visit, visitBD);
			update(visitBD);
		} else {
			visit.setProperty(serviceProperty.readByReference(prpRef).get());
			save(visit);
		}
		return visit;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoVisit);
	}


	@Override
	public List<Visit> readByProperty(String prpRef) {
		return daoVisit.readByProperty(prpRef);
	}

	@Override
	public void validate(Visit bo) throws BusinessException {
		if (bo == null) {
			return;
		}
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (Strings.isNullOrEmpty(bo.getDescription())) {
			mbe.add(EnumBusinessErrorVisit.NO_DESCRIPTION);
		}
		if(bo.getDate() == null) {
			mbe.add(EnumBusinessErrorVisit.NO_DATE);
		}

		mbe.checkErrors();
	}

	@Override
	public void delete(Integer techid) {
		daoVisit.remove(techid);
	}

}
