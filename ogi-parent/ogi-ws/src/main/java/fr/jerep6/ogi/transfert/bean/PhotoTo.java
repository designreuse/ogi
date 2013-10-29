package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

@Getter
@Setter
@EqualsAndHashCode(of = { "techid" })
public class PhotoTo {
	private Integer	techid;
	private Integer	order;
	private String	name;
	private String	url;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).//
				add("techid", techid).//
				add("order", order).//
				add("name", name).//
				add("url", url).toString();
	}
}