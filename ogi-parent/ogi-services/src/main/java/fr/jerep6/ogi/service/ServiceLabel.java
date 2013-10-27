package fr.jerep6.ogi.service;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumLabelType;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.Label;

public interface ServiceLabel extends TransactionalService<Label, Integer> {
	List<Label> readByType(EnumLabelType type);

	/**
	 * Add label. Remove value of techid
	 * 
	 * @param label
	 *            label to add
	 * @return
	 */
	Label add(Label label);

	Label read(EnumLabelType type, String label);
}
