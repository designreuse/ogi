package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.DocumentType;

public interface DaoDocumentType extends DaoCRUD<DocumentType, Integer> {

	List<DocumentType> listDocumentType(EnumDocumentZoneList zone);

}
