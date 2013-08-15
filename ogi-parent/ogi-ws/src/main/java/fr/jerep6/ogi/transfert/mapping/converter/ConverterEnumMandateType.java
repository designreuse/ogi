package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumMandateType;

public class ConverterEnumMandateType extends BidirectionalConverter<EnumMandateType, String> {

	@Override
	public EnumMandateType convertFrom(String source, Type<EnumMandateType> destinationType) {
		return EnumMandateType.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumMandateType source, Type<String> destinationType) {
		return source.getCode();
	}

}