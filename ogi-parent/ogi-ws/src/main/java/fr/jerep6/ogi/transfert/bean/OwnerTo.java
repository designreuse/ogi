package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class OwnerTo {
	private Integer						techid;
	private String						firstname;
	private String						gender;
	private String						surname;
	private Calendar					birthDate;
	private String						phoneHome;
	private String						phoneWork;
	private String						phoneMobile;
	private String						phoneConjoint;
	private String						fax;
	private String						mail;
	private List<AddressTo>				addresses;
	private List<RealPropertyLinkTo>	properties;
}
