package fr.jerep6.ogi.transfert.bean;

import java.util.Calendar;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "diagnosis", "date" })
public class DiagnosisRealPropertyTo {
	private String		diagnosis;
	private Calendar	date;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("diagnosis", diagnosis).add("date", date).toString();
	}
}
