package fr.jerep6.ogi.transfert.mapping;

import javax.annotation.PostConstruct;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.TypeFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyDiagnosis;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.transfert.bean.AddressTo;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.transfert.bean.DescriptionTo;
import fr.jerep6.ogi.transfert.bean.LabelTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyDiagnosisTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;
import fr.jerep6.ogi.transfert.bean.RoomTo;
import fr.jerep6.ogi.transfert.bean.SaleTo;
import fr.jerep6.ogi.transfert.bean.StateTo;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumCategory;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumDescriptionType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumLabelType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumMandateType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumOrientation;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEquipment;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterPhoto;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterType;

@Component("orikaMapper")
public class OrikaMapper extends ConfigurableMapper {
	@Value("${photos.url}")
	private String			urlBasePhoto;

	private MapperFactory	factory;

	@Override
	protected void configure(MapperFactory factory) {
		super.configure(factory);
		this.factory = factory;
	}

	/**
	 * La méthode configure est appelée lors de la construction de l'objet et les injections spring ne sont pas encore
	 * réalisées
	 */
	@PostConstruct
	private void init() {
		ConverterFactory converterFactory = factory.getConverterFactory();

		// Specifics converter (instantiate and copy properties)
		converterFactory.registerConverter(new ConverterEnumCategory());
		converterFactory.registerConverter(new ConverterEnumDescriptionType());
		converterFactory.registerConverter(new ConverterEnumOrientation());
		converterFactory.registerConverter(new ConverterEnumMandateType());
		converterFactory.registerConverter(new ConverterEnumLabelType());
		converterFactory.registerConverter(new ConverterPhoto(urlBasePhoto));
		converterFactory.registerConverter(new ConverterEquipment());
		converterFactory.registerConverter(new ConverterType());

		// Specifics factory (create object)
		factory.registerObjectFactory(new FactoryRealPropertyTo(), TypeFactory.valueOf(RealPropertyTo.class));
		factory.registerObjectFactory(new FactoryRealProperty(), TypeFactory.valueOf(RealProperty.class));

		// Mapping definition
		factory.classMap(Category.class, CategoryTo.class).byDefault().register();
		factory.classMap(Address.class, AddressTo.class).byDefault().register();
		factory.classMap(Description.class, DescriptionTo.class).byDefault().register();
		factory.classMap(RealPropertyDiagnosis.class, RealPropertyDiagnosisTo.class)
				.field("diagnosis.label", "diagnosis").byDefault().register();
		factory.classMap(Room.class, RoomTo.class).byDefault().register();
		factory.classMap(Sale.class, SaleTo.class).byDefault().register();
		factory.classMap(Label.class, LabelTo.class).byDefault().register();
		factory.classMap(State.class, StateTo.class).byDefault().register();

		factory.classMap(RealProperty.class, RealPropertyTo.class).field("equipments{label}", "equipments{}")//
				.field("type.label", "type") //
				.field("diagnosisProperty", "diagnosis")//
				// must exclude the field else classcast exception. It doesn't detected that description is mapped as
				// map bellow
				.exclude("descriptions").field("descriptions{type}", "descriptions{key}")//
				.field("descriptions{}", "descriptions{value}")//
				.byDefault().register();
	}
}
