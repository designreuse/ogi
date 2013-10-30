package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumDocumentType;

public class ConverterEnumDocumentType extends BidirectionalConverter<EnumDocumentType, String> {

	@Override
	public EnumDocumentType convertFrom(String source, Type<EnumDocumentType> destinationType) {
		return EnumDocumentType.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumDocumentType source, Type<String> destinationType) {
		return source.getCode();
	}

}