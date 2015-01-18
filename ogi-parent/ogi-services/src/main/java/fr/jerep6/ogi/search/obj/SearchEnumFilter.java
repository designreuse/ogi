package fr.jerep6.ogi.search.obj;

public enum SearchEnumFilter {

	MODE("modes", "modes"), //
	SALE_PRICE("salePrice", "sale.price"), //
	RENT_PRICE("rentPrice", "rent.price"), //
	CITY("cities", "address.city.raw"), //
	CATEGORIE("categories", "category.raw"), //
	AREA("area", "area"), //
	LAND_AREA("landArea", "landArea"), //
	SOLD("sold", "sale.sold"), //
	RENTED("sold", "rent.rented"), //
	;

	public static SearchEnumFilter valueOfByName(String name) {
		for (SearchEnumFilter uneEnum : SearchEnumFilter.values()) {
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

	private SearchEnumFilter(String nom, String champ) {
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