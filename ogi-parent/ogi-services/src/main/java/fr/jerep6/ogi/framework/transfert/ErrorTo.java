package fr.jerep6.ogi.framework.transfert;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorTo {
	private String	code;
	private String	message;

	public ErrorTo() {
		super();
	}

	public ErrorTo(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}
