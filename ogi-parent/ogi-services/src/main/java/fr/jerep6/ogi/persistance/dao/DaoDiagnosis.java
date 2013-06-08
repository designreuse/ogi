package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Diagnosis;

public interface DaoDiagnosis extends DaoCRUD<Diagnosis, Integer> {

	Diagnosis readByLabel(String label);
}
