package fr.jerep6.ogi.transfert.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = { "kwh", "ges" })
@ToString
public class DPETo {
	private Integer	kwh;
	private String	classificationKWh;
	private Integer	ges;
	private String	classificationGes;
}