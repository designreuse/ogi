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
@EqualsAndHashCode(of = { "mandateReference" })
@ToString
public class SaleTo {
	private String		mandateReference;
	@JsonSerialize(using = JsonCalendarSerializer.class)
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	private Calendar	mandateStartDate;
	@JsonSerialize(using = JsonCalendarSerializer.class)
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	private Calendar	mandateEndDate;
	private String		mandateType;
	private Float		price;
	private Float		priceFinal;
	private Float		commission;
	private Float		commissionPercent;
	private Float		estimationPrice;
	@JsonSerialize(using = JsonCalendarSerializer.class)
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	private Calendar	estimationDate;
	private Float		propertyTax;

}