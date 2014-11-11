package fr.jerep6.ogi.search.obj;

public abstract class SearchCriteriaFilter {
	protected EnumRechercheFiltre	filtre;

	public EnumRechercheFiltre getFiltre() {
		return filtre;
	}

	public String getMappingField() {
		return filtre.getChamp();

	}

}