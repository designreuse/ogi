package fr.jerep6.ogi.search.obj;

import java.util.HashSet;
import java.util.Set;

public class SearchCriteria {
	private static final Integer		PAGE_DEFAUT						= 1;
	private static final Integer		NBRE_RESULTAT_PAR_PAGE_DEFAUT	= 20;
	private static final String			ORDER_DEFAUT					= "ASC";

	/** Filtres sélectionnés par l'internaute */
	private Set<SearchCriteriaFilter>	filtres							= new HashSet<>(0);

	/** Texte saisi par l'internaute */
	private String						keywords;

	/** Page à afficher */
	private Integer						page							= SearchCriteria.PAGE_DEFAUT;

	/** Nbre de résultat a récupérer pour la page */
	private Integer						nbreResultatPage				= SearchCriteria.NBRE_RESULTAT_PAR_PAGE_DEFAUT;

	/** Champ sur lequel trier les produits. Par défaut null donc par de tri. */
	private String						champTri						= null;

	/** Ordre de tri. */
	private String						order							= SearchCriteria.ORDER_DEFAUT;

	public SearchCriteria ajouterFiltre(SearchCriteriaFilter filtre) {
		filtres.add(filtre);
		return this;
	}

	public String getChampTri() {
		return champTri;
	}

	public Set<SearchCriteriaFilter> getFiltres() {
		return filtres;
	}

	public SearchCriteriaFilter getFiltres(SearchEnumFilter enumFiltre) {
		for (SearchCriteriaFilter unFiltre : filtres) {
			if (unFiltre.getFiltre().equals(enumFiltre)) {
				return unFiltre;
			}
		}
		return null;
	}

	public Integer getFrom() {
		return page * nbreResultatPage - nbreResultatPage;
	}

	public String getKeywords() {
		return keywords;
	}

	public Integer getNbreResultatPage() {
		return nbreResultatPage;
	}

	public String getOrder() {
		return order;
	}

	public Integer getPage() {
		return page;
	}

	public SearchCriteria setChampTri(String champTri) {
		this.champTri = champTri;
		return this;
	}

	public SearchCriteria setKeywords(String keywords) {
		this.keywords = keywords;
		return this;
	}

	/**
	 * Positionne le nombre de résultat maximum par page. Règles à respecter sinon positionné à la valeur par défaut :
	 * <code>CritereRecherche.NBRE_RESULTAT_PAR_PAGE_DEFAUT</code>
	 * <ul>
	 * <li>non null</li>
	 * <li>supérieur ou égal à 1</li>
	 *
	 * </ul>
	 *
	 * @param nbreResultatPage
	 * @return
	 */
	public SearchCriteria setNbreResultatPage(Integer nbreResultatPage) {
		if (nbreResultatPage != null && nbreResultatPage >= 1) {
			this.nbreResultatPage = nbreResultatPage;
		} else {
			this.nbreResultatPage = SearchCriteria.NBRE_RESULTAT_PAR_PAGE_DEFAUT;
		}
		return this;
	}

	/**
	 * Positionne l'ordre de tri. Règles à respecter sinon pas positionné:
	 * <ul>
	 * <li>non null</li>
	 * </ul>
	 *
	 * @param order
	 * @return
	 */
	public SearchCriteria setOrder(String order) {
		if (order != null) {
			this.order = order;
		}
		return this;
	}

	/**
	 * Positionne le numéro de la page à afficher. Règles à respecter sinon pas positionné:
	 * <ul>
	 * <li>non null</li>
	 * <li>supérieur ou égal à 1</li>
	 * </ul>
	 *
	 * @param page
	 * @return
	 */
	public SearchCriteria setPage(Integer page) {
		if (page != null && page >= 1) {
			this.page = page;
		}
		return this;
	}

}