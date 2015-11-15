package fr.jerep6.ogi.batch.annoncesjaunes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ack {
	private String	a;
	private String	bienReference;

	public Ack() {
		super();
	}

	/** Constructor for JPA */
	public Ack(String ref) {
		super();
		bienReference = ref.toString();
	}

}
