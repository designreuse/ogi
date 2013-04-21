package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = { "number", "street", "postalCode", "city", "additional" })
@ToString
public class AddressTo {
	private String	number;
	private String	street;
	private String	additional;
	private String	postalCode;
	private String	city;
	private String	cedex;

	private String	latitude;
	private String	longitude;
}
