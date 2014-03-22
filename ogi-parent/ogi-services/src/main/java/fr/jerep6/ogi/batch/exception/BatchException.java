package fr.jerep6.ogi.batch.exception;

import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.persistance.bo.Label;

public class BatchException extends BusinessException {
	private static final long	serialVersionUID	= -2932063299390151842L;

	private Label				existantLabel;

	public BatchException(Label existantLabel, Object... args) {
		super("EAE01", "Le label existe déjà", args);
		this.existantLabel = existantLabel;
	}

	public Label getExistantLabel() {
		return existantLabel;
	}

}
