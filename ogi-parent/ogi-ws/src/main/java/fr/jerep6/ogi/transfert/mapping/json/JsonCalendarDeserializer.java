package fr.jerep6.ogi.transfert.mapping.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Used to deserialize Java.util.Calendar. Format : yyyy-MM-dd
 *
 * @author jerep6
 */
public class JsonCalendarDeserializer extends JsonDeserializer<Calendar> {
	private static final String	PATTERN_ISO8601	= "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String	PATTERN_DAY		= "yyyy-MM-dd";

	@Override
	public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date d = null;
		try {
			SimpleDateFormat dateFormatIso8601 = new SimpleDateFormat(PATTERN_ISO8601);
			String date = jp.getText();
			if (date.endsWith("Z")) {
				date = date.substring(0, date.length() - 1) + "+0000";
			}
			d = dateFormatIso8601.parse(date);
		} catch (ParseException pe1) {
			// Fallback do second date format
			try {
				SimpleDateFormat dateFormatDay = new SimpleDateFormat(PATTERN_DAY);
				d = dateFormatDay.parse(jp.getText());
			} catch (ParseException pe2) {
				throw new RuntimeException(pe1);

			}
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}
}