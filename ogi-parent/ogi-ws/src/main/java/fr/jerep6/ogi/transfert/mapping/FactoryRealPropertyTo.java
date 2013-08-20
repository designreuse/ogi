package fr.jerep6.ogi.transfert.mapping;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.transfert.bean.RealPropertyLivableTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyPlotTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;

/**
 * Instancie le DTO correspondant à la classe de bien. Nécessaire du fait de l'heritage
 * 
 * @author jerep6
 * 
 */
public class FactoryRealPropertyTo implements ObjectFactory<RealPropertyTo> {

	@Override
	public RealPropertyTo create(Object source, MappingContext mappingContext) {
		Class c = source.getClass();

		RealPropertyTo rpt = null;
		if (RealPropertyLivable.class.equals(c)) {
			rpt = new RealPropertyLivableTo();
		}
		else if (RealPropertyPlot.class.equals(c)) {
			rpt = new RealPropertyPlotTo();
		}
		// TODO : others types

		return rpt;
	}
}