package fr.jerep6.ogi.search.obj;

public enum EnumRechercheFiltre {

	MODE("modes", "modes"), //
	SALE_MIN_PRICE("salePriceMin", "sale.price"), //
	SALE_MAX_PRICE("salePriceMax", "sale.price"), //
	RENT_MIN_PRICE("rentPriceMin", "rent.price"), //
	RENT_MAX_PRICE("RentPriceMax", "rent.price"), //
	CITY("cities", "address.city.raw"), //
	CATEGORIE("categories", "category.raw"), //
	;

	public static EnumRechercheFiltre valueOfByName(String name) {
		for (EnumRechercheFiltre uneEnum : EnumRechercheFiltre.values()) {
			if (uneEnum.getName().equalsIgnoreCase(name)) {
				return uneEnum;
			}
		}
		throw new IllegalArgumentException("Aucune énumération pour la valeur " + name);
	}

	/** Nom du champ dans le mapping */
	private String	mappingField;

	/** Nom de l'aggrégation/facette dans le requête au moteur de recherche */
	private String	name;

	private EnumRechercheFiltre(String nom, String champ) {
		this.name = nom;
		this.mappingField = champ;
	}

	public String getChamp() {
		return mappingField;
	}

	public String getName() {
		return name;
	}

}