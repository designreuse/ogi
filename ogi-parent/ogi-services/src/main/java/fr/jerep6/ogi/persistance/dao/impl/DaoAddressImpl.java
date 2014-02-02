package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.dao.DaoAddress;

@Repository("daoAddress")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoAddressImpl extends AbstractDao<Address, Integer> implements DaoAddress {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoAddressImpl.class);
}
