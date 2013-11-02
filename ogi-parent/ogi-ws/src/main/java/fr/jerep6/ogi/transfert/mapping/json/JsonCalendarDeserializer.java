package fr.jerep6.ogi.transfert.mapping.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * Used to deserialize Java.util.Calendar. Format : yyyy-MM-dd
 * 
 * @author jerep6
 */
public class JsonCalendarDeserializer extends JsonDeserializer<Calendar> {
	private static final SimpleDateFormat	dateFormatIso8601	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
	private static final SimpleDateFormat	dateFormatDay		= new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date d = null;
		try {
			d = dateFormatIso8601.parse(jp.getText());
		} catch (ParseException pe1) {
			// Fallback do second date format
			try {
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