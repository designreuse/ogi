package fr.jerep6.ogi.transfert.mapping;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyGarage;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.transfert.bean.RealPropertyLivableTo;

/**
 * Instancie le BO correspondant à la classe de bien. Nécessaire du fait de l'heritage
 * 
 * @author jerep6
 * 
 */
public class FactoryRealProperty implements ObjectFactory<RealProperty> {

	@Override
	public RealProperty create(Object source, MappingContext mappingContext) {
		Class c = source.getClass();

		RealProperty rpt = null;
		if (RealPropertyLivableTo.class.equals(c)) {
			rpt = new RealPropertyLivable();
		}
		// TODO
		else if (RealPropertyGarage.class.equals(c)) {
			rpt = null;
		}

		return rpt;
	}
}