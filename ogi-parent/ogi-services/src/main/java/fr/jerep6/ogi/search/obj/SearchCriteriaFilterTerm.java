package fr.jerep6.ogi.search.obj;

public class SearchCriteriaFilterTerm extends SearchCriteriaFilter {
	private Object[]	valeurs;

	public SearchCriteriaFilterTerm(SearchEnumFilter filtre, Object... valeurs) {
		super();
		this.filtre = filtre;
		this.valeurs = valeurs;
	}

	public boolean contains(Object o) {
		if (valeurs != null) {
			for (Object unObject : valeurs) {
				if (unObject.equals(o)) {
					return true;
				}
			}
		}

		return false;
	}

	public Object[] getValeur() {
		return valeurs;
	}
}