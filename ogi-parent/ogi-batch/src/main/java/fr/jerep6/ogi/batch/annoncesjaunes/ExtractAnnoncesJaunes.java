package fr.jerep6.ogi.batch.annoncesjaunes;

import fr.jerep6.ogi.persistance.bo.RealProperty;

/**
 * A real property can be in sale and in rent at the same time.
 * I use two jpql to extract properties in sale and properties in rent
 * 
 * Resultat des requêtes spql de lecture des biens à envoyer à seloger
 * 
 * @author jerep6 23 mars 2014
 */
public class ExtractAnnoncesJaunes {
	private RealProperty	property;

	/** Must be SALE or RENT */
	private String			mode;

	public ExtractAnnoncesJaunes(RealProperty property, String mode) {
		super();
		this.property = property;
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public RealProperty getProperty() {
		return property;
	}
}
