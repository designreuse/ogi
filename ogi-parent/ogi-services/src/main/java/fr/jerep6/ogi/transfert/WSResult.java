package fr.jerep6.ogi.transfert;

import lombok.Getter;
import lombok.ToString;

import com.google.common.base.Preconditions;

@Getter
@ToString
public class WSResult {
	public static final String	RESULT_OK	= "OK";
	public static final String	RESULT_KO	= "KO";
	public static final String	RESULT_WAIT	= "WAIT";

	private String				reference;
	private String				code;
	private String				message;

	public WSResult(String reference, String code, String message) {
		super();
		Preconditions.checkNotNull(reference);
		Preconditions.checkNotNull(code);

		this.reference = reference;
		this.code = code;
		this.message = message;
	}

	public WSResult combine(WSResult other) {
		String c = "";
		if (code.equals(other.code) && code.equals(RESULT_OK)) {
			c = RESULT_OK;
		} else {
			c = RESULT_KO;
		}

		StringBuilder msg = new StringBuilder(message);
		msg.append(" -- ");
		msg.append(other.message);

		return new WSResult(reference, c, msg.toString());
	}

	public boolean isSuccess() {
		return RESULT_OK.equals(code) || RESULT_WAIT.equals(code);
	}
}
