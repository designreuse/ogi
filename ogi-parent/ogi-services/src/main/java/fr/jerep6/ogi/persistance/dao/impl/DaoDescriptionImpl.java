package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.dao.DaoDescription;

@Repository("daoDescription")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoDescriptionImpl extends AbstractDao<Description, Integer> implements DaoDescription {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoDescriptionImpl.class);
}
