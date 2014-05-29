package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumSortByDirection;
import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorOwner;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.dao.DaoOwner;
import fr.jerep6.ogi.service.ServiceAddress;
import fr.jerep6.ogi.service.ServiceOwner;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.ListResult;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceOwner")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceOwnerImpl extends AbstractTransactionalService<Owner, Integer> implements ServiceOwner {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceOwnerImpl.class);

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private ServiceAddress		serviceAddress;

	@Autowired
	private DaoOwner			daoOwner;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	public void addProperty(Integer techid, String prpRef) {
		RealProperty prp = serviceRealProperty.readByReference(prpRef);

		Owner owner = read(techid);
		owner.getProperties().add(prp);
	}

	@Override
	public Owner createOrUpdate(Owner owner) {
		validate(owner);

		if (owner.getTechid() != null) {
			Owner ownerBD = read(owner.getTechid());
			// Mapper don't map addresses and properties association
			mapper.map(owner, ownerBD);
			Set<Address> addr = serviceAddress.merge(ownerBD.getAddresses(), owner.getAddresses());
			ownerBD.setAddresses(addr);
			update(ownerBD);
		} else {
			save(owner);
		}

		return owner;
	}

	@Override
	public void deleteProperty(Integer ownerTechid, String prpRef) {
		RealProperty prp = serviceRealProperty.readByReference(prpRef);

		Owner owner = read(ownerTechid);
		owner.getProperties().remove(prp);
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoOwner);
	}

	@Override
	public ListResult<Owner> list(Integer pageNumber, Integer itemNumberPerPage, String sortBy, String sortDir) {
		pageNumber = Objects.firstNonNull(pageNumber, 1);
		itemNumberPerPage = Objects.firstNonNull(itemNumberPerPage, 30);
		sortDir = Objects.firstNonNull(sortDir, "asc");

		List<Owner> owners = daoOwner.list(pageNumber, itemNumberPerPage, sortBy,
				EnumSortByDirection.valueOfByCode(sortDir));

		return new ListResult<Owner>(owners, daoOwner.count().intValue());
	}

	@Override
	public Set<Owner> merge(Set<Owner> ownersBD, Set<Owner> ownersModify) {
		// Keep owners to reuse it (avoid insert)
		List<Owner> ownersBDBackup = new ArrayList<>(ownersBD);

		// clear owners in database
		ownersBD.clear();

		// Populate set of owners.
		ownersModify.forEach(ownerModif -> {
			// Find owner corresponding to techid
			int index = ownersBDBackup.indexOf(ownerModif);
			// Optional<Owner> roomInBd = ownersBDBackup.stream().filter(o -> techid.equals(o.getTechid()))
			// .findFirst();

				// If owner exist => reuse it
			if (index != -1) {
					Owner o = ownersBDBackup.get(index);
					ownersBD.add(o);
				}
				// Owner doesn't exist in prp => read it into database
				else {
					Owner ownerToAdd = read(ownerModif.getTechid());
					if (ownerToAdd != null) {
						ownersBD.add(ownerToAdd);
					}
				}
			});

		return ownersBD;

	}

	@Override
	public Set<Owner> read(Set<Integer> techids) {
		return new HashSet<>(daoOwner.read(techids));
	}

	@Override
	public List<Owner> readByProperty(String prpRef) {
		return daoOwner.readByProperty(prpRef);
	}

	@Override
	public void validate(Owner bo) throws BusinessException {
		if (bo == null) {
			return;
		}
		MultipleBusinessException mbe = new MultipleBusinessException();

		if (Strings.isNullOrEmpty(bo.getSurname())) {
			mbe.add(EnumBusinessErrorOwner.NO_SURNAME);
		}

		mbe.checkErrors();
	}

}
