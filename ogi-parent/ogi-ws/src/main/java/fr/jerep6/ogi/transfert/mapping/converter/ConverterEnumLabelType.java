package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumLabelType;

public class ConverterEnumLabelType extends BidirectionalConverter<EnumLabelType, String> {

	@Override
	public EnumLabelType convertFrom(String source, Type<EnumLabelType> destinationType) {
		return EnumLabelType.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumLabelType source, Type<String> destinationType) {
		return source.getCode();
	}

}