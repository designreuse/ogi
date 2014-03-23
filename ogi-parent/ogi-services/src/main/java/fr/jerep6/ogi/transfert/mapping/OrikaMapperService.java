package fr.jerep6.ogi.transfert.mapping;

import javax.annotation.PostConstruct;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

import org.springframework.stereotype.Component;

import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.persistance.bo.Rent;
import fr.jerep6.ogi.persistance.bo.Sale;

@Component("orikaMapperService")
public class OrikaMapperService extends ConfigurableMapper {
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
	private void postConstruct() {
		factory.classMap(Document.class, Document.class).exclude("techid").exclude("property").exclude("absolutePath")
				.byDefault().register();
		factory.classMap(Address.class, Address.class).exclude("techid").byDefault().register();
		factory.classMap(Owner.class, Owner.class).exclude("techid").exclude("properties").exclude("addresses")
				.byDefault().register();
		factory.classMap(Sale.class, Sale.class).exclude("techid").byDefault().register();
		factory.classMap(Rent.class, Rent.class).exclude("techid").byDefault().register();

		factory.classMap(Description.class, Description.class).exclude("techid").exclude("property").byDefault()
				.register();

		// Map only basic type (string, integer ...)
		factory.classMap(RealProperty.class, RealProperty.class)//
				.exclude("techid")//
				.exclude("descriptions")//
				.exclude("modificationDate")//
				.exclude("version")//
				.exclude("category")//
				.exclude("diagnosisProperty")//
				.exclude("equipments")//
				.exclude("sale")//
				.exclude("rent")//
				.exclude("documents")//
				.exclude("type")//
				.exclude("owners")// exclusion des propriétaires car ils sont traités ailleurs
				.byDefault().register();

		factory.classMap(RealPropertyLivable.class, RealPropertyLivable.class)//
				.use(RealProperty.class, RealProperty.class)//
				.exclude("rooms")//
				.exclude("techid")//
				.exclude("descriptions")//
				.exclude("equipments")//
				.exclude("modificationDate")//
				.exclude("version")//
				.exclude("category")//
				.exclude("diagnosisProperty")//
				.exclude("sale")//
				.exclude("rent")//
				.exclude("documents")//
				.exclude("type")//
				.exclude("owners")//
				.exclude("state")//
				.exclude("dpeFile")//
				.byDefault().register();

		factory.classMap(RealPropertyPlot.class, RealPropertyPlot.class)//
				.use(RealProperty.class, RealProperty.class)//
				.exclude("rooms")//
				.exclude("techid")//
				.exclude("descriptions")//
				.exclude("equipments")//
				.exclude("modificationDate")//
				.exclude("version")//
				.exclude("category")//
				.exclude("diagnosisProperty")//
				.exclude("sale")//
				.exclude("rent")//
				.exclude("documents")//
				.exclude("type")//
				.exclude("owners")//
				.exclude("state")//
				.byDefault().register();

	}
}
