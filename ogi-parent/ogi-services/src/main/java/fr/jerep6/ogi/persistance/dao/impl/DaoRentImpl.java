package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.dao.DaoRent;

@Repository("daoRent")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoRentImpl extends AbstractDao<Rent, Integer> implements DaoRent {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoRentImpl.class);
}
