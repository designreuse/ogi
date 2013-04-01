package fr.jerep6.ogi.transfert.converter;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

import org.springframework.stereotype.Component;

import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.transfert.bean.CategoryTo;

@Component
public class OrikaMapper extends ConfigurableMapper {

	@Override
	protected void configure(MapperFactory factory) {
		super.configure(factory);

		ConverterFactory converterFactory = factory.getConverterFactory();

		// Specific converter
		converterFactory.registerConverter(new ConverterEnumCategory());

		// Mapping definition
		factory.classMap(Category.class, CategoryTo.class).byDefault().register();
	}

}
