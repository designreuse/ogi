package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumGestionMode;

public class ConverterEnumDocumentZoneList extends BidirectionalConverter<EnumGestionMode, String> {

	@Override
	public EnumGestionMode convertFrom(String source, Type<EnumGestionMode> destinationType) {
		return EnumGestionMode.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumGestionMode source, Type<String> destinationType) {
		return source.getCode();
	}

}