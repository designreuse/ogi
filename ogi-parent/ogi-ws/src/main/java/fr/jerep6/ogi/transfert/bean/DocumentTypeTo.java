package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Getter
@Setter
@EqualsAndHashCode(of = { "code" })
public class DocumentTypeTo {
	private String	code;
	private String	label;
	private String	zoneList;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).//
				add("label", label).//
				add("code", code).//
				add("zoneList", zoneList).toString();
	}
}