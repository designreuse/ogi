package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.dao.DaoEquipment;
import fr.jerep6.ogi.service.ServiceEquipment;

@Service("serviceEquipment")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceEquipmentImpl extends AbstractTransactionalService<Equipment, Integer> implements ServiceEquipment {

	@Autowired
	private DaoEquipment	daoEquipment;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoEquipment);
	}

	@Override
	public Equipment readByLabel(String label, EnumCategory category) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(label));
		Preconditions.checkNotNull(category);

		return daoEquipment.readByLabel(label, category);
	}

}
