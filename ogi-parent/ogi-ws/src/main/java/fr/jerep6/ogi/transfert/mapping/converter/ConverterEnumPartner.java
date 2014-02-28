package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumPartner;

public class ConverterEnumPartner extends BidirectionalConverter<EnumPartner, String> {

	@Override
	public EnumPartner convertFrom(String source, Type<EnumPartner> destinationType) {
		return EnumPartner.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumPartner source, Type<String> destinationType) {
		return source.getCode();
	}

}