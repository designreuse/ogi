package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Getter
@Setter
@EqualsAndHashCode(of = { "type", "label" })
public class LabelTo {
	private Integer	techid;
	private String	type;
	private String	label;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("type", type).add("label", label).toString();
	}

}