package fr.jerep6.ogi.persistance.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.dao.DaoDocument;

@Repository("daoDocument")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoDocumentImpl extends AbstractDao<Document, Integer> implements DaoDocument {
	Logger	LOGGER	= LoggerFactory.getLogger(DaoDocumentImpl.class);

}
