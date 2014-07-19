package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import fr.jerep6.ogi.transfert.mapping.json.JsonCalendarDeserializer;
import fr.jerep6.ogi.transfert.mapping.json.JsonCalendarSerializer;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "techid" })
public class RentTo {
	private Integer		techid;
	private String		mandateReference;
	@JsonSerialize(using = JsonCalendarSerializer.class)
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	private Calendar	freeDate;
	private String		mandateType;
	private Float		price;
	private Float		commission;
	private Float		commissionManagement;
	private Float		deposit;
	private Float		serviceCharge;
	private Boolean		serviceChargeIncluded;
	private Boolean		furnished;
	private Float		priceFinal;

}