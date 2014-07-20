package fr.jerep6.ogi.transfert;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ExpiredMandate {
	private String		mandatType;
	private String		propertyReference;
	private String		mandateReference;
	private Calendar	endDate;

	/**
	 * Format endDate to iso8601 format. If endDate is null return empty string
	 *
	 * @return
	 */
	public String getIso8601EndDate() {
		if (endDate != null) {
			return DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault()).format(endDate.toInstant());
		} else {
			return "";
		}
	}
}
