package fr.jerep6.ogi.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.dao.DaoType;
import fr.jerep6.ogi.service.ServiceType;

@Service("serviceType")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceTypeImpl extends AbstractTransactionalService<Type, Integer> implements ServiceType {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceTypeImpl.class);

	@Autowired
	private DaoType			daoType;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoType);
	}

	@Override
	public List<Type> readByCategory(EnumCategory category) {
		Preconditions.checkNotNull(category);
		return daoType.readByCategory(category);
	}

	@Override
	public Type readByLabel(String label) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(label));

		return daoType.readByLabel(label);
	}

	@Override
	public Type readOrInsert(String label, Category category) {
		Type type = readByLabel(label);

		// Create if not exist
		if (type == null) {
			type = new Type(label, category);
			save(type);
		}
		return type;
	}
}
