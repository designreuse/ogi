package fr.jerep6.ogi.search.obj;

public abstract class SearchCriteriaFilter {
	protected SearchEnumFilter	filtre;

	private String				orIdentifiant	= "";

	public SearchEnumFilter getFiltre() {
		return filtre;
	}

	public String getMappingField() {
		return filtre.getChamp();
	}

	public String getOrIdentifiant() {
		return orIdentifiant;
	}

	public void setOrIdentifiant(String orIdentifiant) {
		this.orIdentifiant = orIdentifiant;
	}

}