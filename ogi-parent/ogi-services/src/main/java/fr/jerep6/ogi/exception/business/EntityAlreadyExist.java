package fr.jerep6.ogi.exception.business;

import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.persistance.bo.Label;

public class EntityAlreadyExist extends BusinessException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	private Label				existantLabel;

	public EntityAlreadyExist(Label existantLabel, Object... args) {
		super("EAE01", "Le label existe déjà", args);
		this.existantLabel = existantLabel;
	}

	public Label getExistantLabel() {
		return existantLabel;
	}

}
