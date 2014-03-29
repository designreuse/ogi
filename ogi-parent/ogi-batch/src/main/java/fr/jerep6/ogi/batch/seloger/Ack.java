package fr.jerep6.ogi.batch.seloger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ack {
	private String	a;
	private String	bienTechid;

	public Ack() {
		super();
	}

	/** Constructor for JPA */
	public Ack(Integer techid) {
		super();
		bienTechid = techid.toString();
	}

}
