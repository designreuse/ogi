package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class RentTo {
	private Integer		techid;
	private Calendar	freeDate;
	private Boolean		exclusive;
	private Float		price;
	private Float		commission;
	private Float		commissionManagement;
	private Float		deposit;
	private Float		serviceCharge;
	private Boolean		serviceChargeIncluded;

}