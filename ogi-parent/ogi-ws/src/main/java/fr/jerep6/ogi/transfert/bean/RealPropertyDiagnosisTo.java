package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

import fr.jerep6.ogi.transfert.mapping.json.JsonCalendarDeserializer;
import fr.jerep6.ogi.transfert.mapping.json.JsonCalendarSerializer;

// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "diagnosis", "date" })
public class RealPropertyDiagnosisTo {
	private String		diagnosis;
	@JsonSerialize(using = JsonCalendarSerializer.class)
	@JsonDeserialize(using = JsonCalendarDeserializer.class)
	private Calendar	date;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("diagnosis", diagnosis).add("date", date).toString();
	}
}
