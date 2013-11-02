package fr.jerep6.ogi.transfert.mapping.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Used to serialize Java.util.Calendar. Format : yyyy-MM-dd
 * 
 * @author jerep6
 */
public class JsonCalendarSerializer extends JsonSerializer<Calendar> {
	private static final SimpleDateFormat	dateFormat	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

	@Override
	public void serialize(Calendar cal, JsonGenerator gen, SerializerProvider provider) throws IOException,
			JsonProcessingException {

		String formattedDate = dateFormat.format(cal.getTime());
		gen.writeString(formattedDate);
	}

}