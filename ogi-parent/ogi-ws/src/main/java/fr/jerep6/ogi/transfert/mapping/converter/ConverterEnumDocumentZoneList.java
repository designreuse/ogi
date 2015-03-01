package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumDocumentZoneList;

public class ConverterEnumDocumentZoneList extends BidirectionalConverter<EnumDocumentZoneList, String> {

	@Override
	public EnumDocumentZoneList convertFrom(String source, Type<EnumDocumentZoneList> destinationType) {
		return EnumDocumentZoneList.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumDocumentZoneList source, Type<String> destinationType) {
		return source.getCode();
	}

}