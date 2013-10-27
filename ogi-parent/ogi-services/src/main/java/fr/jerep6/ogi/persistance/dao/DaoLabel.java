package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumLabelType;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Label;

public interface DaoLabel extends DaoCRUD<Label, Integer> {

	List<Label> readByType(EnumLabelType type);

	Label read(EnumLabelType type, String label);

}
