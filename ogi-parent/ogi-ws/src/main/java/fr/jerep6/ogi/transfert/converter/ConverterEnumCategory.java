package fr.jerep6.ogi.transfert.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.enumeration.EnumCategory;

public class ConverterEnumCategory extends BidirectionalConverter<EnumCategory, String> {

	@Override
	public EnumCategory convertFrom(String source, Type<EnumCategory> destinationType) {
		return EnumCategory.valueOfByCode(source);
	}

	@Override
	public String convertTo(EnumCategory source, Type<String> destinationType) {
		return source.getCode();
	}

}