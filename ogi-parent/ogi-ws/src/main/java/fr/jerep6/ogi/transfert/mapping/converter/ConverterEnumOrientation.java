package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumOrientation;

public class ConverterEnumOrientation extends BidirectionalConverter<EnumOrientation, String> {

	@Override
	public EnumOrientation convertFrom(String source, Type<EnumOrientation> destinationType) {
		return EnumOrientation.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumOrientation source, Type<String> destinationType) {
		return source.getCode();
	}

}