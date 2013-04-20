package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import fr.jerep6.ogi.persistance.bo.Equipment;

public class ConverterEquipment extends BidirectionalConverter<Equipment, String> {

	@Override
	public Equipment convertFrom(String source, Type<Equipment> destinationType) {
		return null;
	}

	@Override
	public String convertTo(Equipment source, Type<String> destinationType) {
		return source.getLabel();
	}

}