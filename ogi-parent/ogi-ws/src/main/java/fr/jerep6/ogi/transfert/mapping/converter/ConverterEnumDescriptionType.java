package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumDescriptionType;

public class ConverterEnumDescriptionType extends BidirectionalConverter<EnumDescriptionType, String> {

	@Override
	public EnumDescriptionType convertFrom(String source, Type<EnumDescriptionType> destinationType) {
		return EnumDescriptionType.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumDescriptionType source, Type<String> destinationType) {
		return source.getCode();
	}

}