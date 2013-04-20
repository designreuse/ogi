package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = { "type" })
@ToString
public class DescriptionTo {
	private String	type;
	private String	label;

	public DescriptionTo() {
		super();
	}

}