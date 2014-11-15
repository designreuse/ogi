package fr.jerep6.ogi.search.obj;

public abstract class SearchCriteriaFilter {
	protected SearchEnumFilter	filtre;

	public SearchEnumFilter getFiltre() {
		return filtre;
	}

	public String getMappingField() {
		return filtre.getChamp();

	}

}