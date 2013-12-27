package fr.jerep6.ogi.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoOwner;
import fr.jerep6.ogi.service.ServiceOwner;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceOwner")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceOwnerImpl extends AbstractTransactionalService<Owner, Integer> implements ServiceOwner {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceOwnerImpl.class);

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private DaoOwner			daoOwner;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	public void associate(String prpRef, List<Owner> ownersBo) {
		RealProperty prp = serviceRealProperty.readByReference(prpRef);

		prp.setOwners(new HashSet<>(ownersBo));
	}

	@Override
	public List<Owner> createOrUpdate(List<Owner> ownersBo) {
		for (Owner owner : ownersBo) {
			if (owner.getTechid() != null) {
				Owner ownerBD = read(owner.getTechid());
				mapper.map(owner, ownerBD);
				update(ownerBD);
			} else {
				save(owner);
			}
		}

		return ownersBo;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoOwner);
	}

	@Override
	public List<Owner> readByProperty(String prpRef) {
		return daoOwner.readByProperty(prpRef);
	}

}
