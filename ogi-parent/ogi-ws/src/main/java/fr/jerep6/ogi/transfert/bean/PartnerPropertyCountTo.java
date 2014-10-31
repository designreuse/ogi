package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = "partner")
@ToString
public class PartnerPropertyCountTo {
	private String	partner;
	private Long	totalProperty;
	private Long	maxAllowed;

}
