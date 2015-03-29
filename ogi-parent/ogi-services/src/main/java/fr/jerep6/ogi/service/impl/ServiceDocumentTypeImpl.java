package fr.jerep6.ogi.service.impl;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.DocumentType;
import fr.jerep6.ogi.persistance.dao.DaoDocumentType;
import fr.jerep6.ogi.service.ServiceDocumentType;

@Service("serviceDocumentType")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDocumentTypeImpl extends AbstractTransactionalService<DocumentType, Integer> implements
ServiceDocumentType {
	private final Logger	LOGGER	= LoggerFactory.getLogger(ServiceDocumentTypeImpl.class);

	@Autowired
	private DaoDocumentType	daoDocumentType;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDocumentType);
	}

	@Override
	public Optional<DocumentType> readByCode(String code) {
		return daoDocumentType.readByCode(code);
	}

}
