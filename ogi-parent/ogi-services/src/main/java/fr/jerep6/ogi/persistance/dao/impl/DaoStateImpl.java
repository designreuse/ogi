package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.persistance.dao.DaoState;

@Repository("daoState")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoStateImpl extends AbstractDao<State, Integer> implements DaoState {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoStateImpl.class);
}
