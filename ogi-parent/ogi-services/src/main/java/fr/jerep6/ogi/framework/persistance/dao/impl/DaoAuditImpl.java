package fr.jerep6.ogi.framework.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.bo.Audit;
import fr.jerep6.ogi.framework.persistance.dao.DaoAudit;

@Repository("daoAudit")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoAuditImpl extends AbstractDao<Audit, Integer> implements DaoAudit {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoAuditImpl.class);

}
