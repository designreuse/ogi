package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.dao.DaoDPE;

@Repository("daoDPE")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoDPEImpl extends AbstractDao<DPE, Integer> implements DaoDPE {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoDPEImpl.class);
}
