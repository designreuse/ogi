package fr.jerep6.ogi.service;

import java.util.Optional;

import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.DocumentType;

public interface ServiceDocumentType extends TransactionalService<DocumentType, Integer> {

	Optional<DocumentType> readByCode(String code);
}
