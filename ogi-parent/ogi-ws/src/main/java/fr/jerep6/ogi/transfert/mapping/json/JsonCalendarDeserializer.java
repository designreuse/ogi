package fr.jerep6.ogi.transfert.mapping.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	private static final SimpleDateFormat	dateFormat	= new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(dateFormat.parse(jp.getText()));
			return c;
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}