package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.dao.DaoRent;
import fr.jerep6.ogi.service.ServiceRent;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceRent")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceRentImpl extends AbstractTransactionalService<Rent, Integer> implements ServiceRent {

	@Autowired
	private DaoRent				daoRent;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoRent);
	}

	@Override
	public Rent merge(Rent rentOriginalBD, Rent rentModif) {
		// control the consistency of the object
		validate(rentModif);

		Rent rentMapping = null;

		// IHM send a sale
		if (rentModif != null) {
			// Map field only if sale found and saleModif (ihm) is not null
			if (rentOriginalBD != null) {
				rentMapping = rentOriginalBD;
				mapper.map(rentModif, rentMapping);
			}
			// no sale in database and ihm is not null
			else if (rentOriginalBD == null) {
				rentMapping = rentModif;
			}

		} else {
			// ihm null and database contain sale => remove sale in database
			if (rentOriginalBD != null) {
				remove(rentOriginalBD);
			}
		}

		return rentMapping;
	}

}