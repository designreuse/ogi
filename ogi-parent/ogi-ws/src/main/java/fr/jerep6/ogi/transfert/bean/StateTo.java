package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "order" })
public class StateTo {
	private Integer	order;
	private String	label;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("ordre", order).add("label", label).toString();
	}
}