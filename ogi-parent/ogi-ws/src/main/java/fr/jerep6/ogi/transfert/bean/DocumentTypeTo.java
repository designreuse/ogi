package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Getter
@Setter
@EqualsAndHashCode(of = { "techid" })
public class DocumentTypeTo {
	private Integer	techid;
	private String	label;
	private String	zoneList;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).//
				add("techid", techid).//
				add("label", label).//
				add("zoneList", zoneList).toString();
	}
}