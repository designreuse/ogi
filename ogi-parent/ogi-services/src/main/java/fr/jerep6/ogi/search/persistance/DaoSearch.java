package fr.jerep6.ogi.search.persistance;

import java.util.List;

import fr.jerep6.ogi.persistance.bo.RealProperty;

public interface DaoSearch {
	void search();

	void index(List<RealProperty> realProperty);

	void delete(List<String> references);
}
