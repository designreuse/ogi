package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import org.springframework.stereotype.Component;

import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.persistance.bo.DocumentType;
import fr.jerep6.ogi.persistance.dao.DaoDocumentType;

/**
 * Quand cette classe est instanciée, le contexte de spring n'est pas encore chargé du fait de la contrainte de orika =>
 * les converter doivent être dans la méthode init
 *
 * @author jerep6
 *
 */
@Component
public class ConverterDocumentType extends BidirectionalConverter<DocumentType, Integer> {

	/**
	 * source is techid from document type
	 */
	@Override
	public DocumentType convertFrom(Integer source, Type<DocumentType> destinationType) {
		return getDaoDocumentType().read(source);
		// DocumentType d = new DocumentType();
		// d.setTechid(1);
		// d.setLabel("Photo du bien");
		// d.setZoneList(EnumDocumentZoneList.NO);
		// return d;
	}

	@Override
	public Integer convertTo(DocumentType source, Type<Integer> destinationType) {
		return source.getTechid();
	}

	private DaoDocumentType getDaoDocumentType() {
		return ContextUtils.getBean(DaoDocumentType.class);
	}

}