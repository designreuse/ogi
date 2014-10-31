package fr.jerep6.ogi.transfert.bean;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "partner")
@ToString
public class PartnerPropertyCountTo {
	private String	partner;
	private Long	totalProperty;
}
