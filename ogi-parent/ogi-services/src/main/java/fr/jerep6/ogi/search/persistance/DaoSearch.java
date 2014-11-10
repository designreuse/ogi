package fr.jerep6.ogi.search.persistance;

import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface DaoSearch {
	void search();

	void index(RealProperty r);
}
