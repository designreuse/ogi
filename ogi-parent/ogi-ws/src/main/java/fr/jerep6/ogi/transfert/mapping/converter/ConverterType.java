package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class ConverterType extends BidirectionalConverter<fr.jerep6.ogi.persistance.bo.Type, String> {

	@Override
	public fr.jerep6.ogi.persistance.bo.Type convertFrom(String source,
			Type<fr.jerep6.ogi.persistance.bo.Type> destinationType) {
		return null;
	}

	@Override
	public String convertTo(fr.jerep6.ogi.persistance.bo.Type source, Type<String> destinationType) {
		return source.getLabel();
	}

}