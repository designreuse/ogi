package fr.jerep6.ogi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessError;
import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.dao.DaoAddress;
import fr.jerep6.ogi.service.ServiceAddress;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceAddress")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceAddressImpl extends AbstractTransactionalService<Address, Integer> implements ServiceAddress {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceAddressImpl.class);

	@Autowired
	private DaoAddress			daoAddress;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoAddress);
	}

	@Override
	public Set<Address> merge(Set<Address> addressesBD, Set<Address> addressesModif) {

		List<Address> addressToDelete = new ArrayList<>(0);

		for (Address aBD : addressesBD) {
			boolean find = false;
			for (Address aModif : addressesModif) {
				if (aBD.equals(aModif)) {
					find = true;
					break;
				}
			}
			if (!find) {
				addressToDelete.add(aBD);
			}
		}

		// Keep addresses to reuse it (avoid insert)
		List<Address> addrBDBackup = new ArrayList<>(addressesBD);

		// descriptions in database
		addressesBD.clear();

		// Add or update addresses to manage object
		for (Address addrModif : addressesModif) {
			Address d = addrModif;

			int indexDesc = addrBDBackup.indexOf(d);
			if (indexDesc != -1) {
				// Get existant address to avoid sql insert
				d = addrBDBackup.get(indexDesc);
				mapper.map(addrModif, d);
			}
			addressesBD.add(d);
		}

		addrBDBackup.removeAll(addressesBD);
		remove(addrBDBackup);

		return addressesBD;
	}

	@Override
	public void validate(Address bo) throws BusinessException {
		MultipleBusinessException mbe = new MultipleBusinessException();
		if (Strings.isNullOrEmpty(bo.getCity())) {
			mbe.add(EnumBusinessError.NO_ADDRESS_CITY);
		}
		if (Strings.isNullOrEmpty(bo.getPostalCode())) {
			mbe.add(EnumBusinessError.NO_ADDRESS_POSTAL_CODE);
		}
		mbe.checkErrors();
	}

}