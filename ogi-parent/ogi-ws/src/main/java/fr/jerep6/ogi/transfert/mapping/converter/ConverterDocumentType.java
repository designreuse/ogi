package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;
import fr.jerep6.ogi.persistance.bo.DocumentType;
import fr.jerep6.ogi.persistance.dao.DaoDocumentType;

public class ConverterDocumentType extends BidirectionalConverter<DocumentType, Integer> {

	private DaoDocumentType	daoDocumentType;

	public ConverterDocumentType(DaoDocumentType daoDocumentType) {
		super();
		this.daoDocumentType = daoDocumentType;
	}

	@Override
	public DocumentType convertFrom(Integer source, Type<DocumentType> destinationType) {
		// TODO
		// return daoDocumentType.read(source);
		DocumentType d = new DocumentType();
		d.setTechid(1);
		d.setLabel("Photo du bien");
		d.setZoneList(EnumDocumentZoneList.NO);
		return d;
	}

	@Override
	public Integer convertTo(DocumentType source, Type<Integer> destinationType) {
		return source.getTechid();
	}

}