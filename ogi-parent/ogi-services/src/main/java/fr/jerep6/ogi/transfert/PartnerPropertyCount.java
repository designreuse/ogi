package fr.jerep6.ogi.transfert;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import fr.jerep6.ogi.enumeration.EnumPartner;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "partner")
@ToString
public class PartnerPropertyCount {
	private EnumPartner	partner;
	private Long		totalProperty;
}
