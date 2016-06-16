package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = { "techid" })
@ToString
public class VisitTo {
	private Integer techid;
	private Calendar date;
	private String	client;
	private String	description;
	private RealPropertyLinkTo property;

	public VisitTo() {
		super();
	}

}