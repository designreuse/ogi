package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;

public class ConverterEnumPartnerRequestType extends BidirectionalConverter<EnumPartnerRequestType, String> {

	@Override
	public EnumPartnerRequestType convertFrom(String source, Type<EnumPartnerRequestType> destinationType) {
		return EnumPartnerRequestType.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumPartnerRequestType source, Type<String> destinationType) {
		return source.getCode();
	}

}