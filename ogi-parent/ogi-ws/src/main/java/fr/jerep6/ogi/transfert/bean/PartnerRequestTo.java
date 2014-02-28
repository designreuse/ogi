package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PartnerRequestTo {
	private Integer		techid;
	private Integer		property;
	private String		partner;
	private String		requestType;
	private Calendar	modificationDate;
	private String		label;

	public PartnerRequestTo() {
		super();
	}

}