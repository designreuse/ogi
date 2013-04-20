package fr.jerep6.ogi.transfert.mapping;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import fr.jerep6.ogi.persistance.bo.RealPropertyGarage;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.transfert.bean.RealPropertyLivableTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;

/**
 * Instancie le DTO correspondant à la classe de bien. Nécessaire du fait de l'heritage
 * 
 * @author jerep6
 * 
 */
public class FactoryRealProperty implements ObjectFactory<RealPropertyTo> {

	@Override
	public RealPropertyTo create(Object source, MappingContext mappingContext) {
		Class c = source.getClass();

		RealPropertyTo rpt = null;
		if (RealPropertyLivable.class.equals(c)) {
			rpt = new RealPropertyLivableTo();
		}
		// TODO
		else if (RealPropertyGarage.class.equals(c)) {
			rpt = null;
		}

		return rpt;
	}
}