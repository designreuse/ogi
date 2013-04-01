package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.dao.DaoCategory;
import fr.jerep6.ogi.service.ServiceCategory;

@Service("serviceCategory")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceCategoryImpl extends AbstractTransactionalService<Category, Integer> implements ServiceCategory {

	@Autowired
	private DaoCategory	daoCategory;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoCategory);
	}

	@Override
	public Category readByCode(EnumCategory enumCategory) {
		Preconditions.checkNotNull(enumCategory);

		return daoCategory.readByCode(enumCategory);
	}
}
