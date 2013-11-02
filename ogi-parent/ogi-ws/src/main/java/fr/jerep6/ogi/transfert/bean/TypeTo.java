package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "category", "label" })
public class TypeTo {
	private String		label;
	private CategoryTo	category;

	public TypeTo() {
		super();
	}

}