package fr.jerep6.ogi.batch.common.writer;

import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.util.ObjectUtils;

public class AdvancedDelimitedLineAggregator<T> extends ExtractorLineAggregator<T> {

	private String	delimiter		= ",";
	private String	quotedCharacter	= "";

	@Override
	public String doAggregate(Object[] fields) {
		if (ObjectUtils.isEmpty(fields)) {
			return "";
		}
		if (fields.length == 1) {
			return ObjectUtils.nullSafeToString(fields[0]);
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			if (i > 0) {
				sb.append(delimiter);
			}
			sb.append(quotedCharacter);
			sb.append(fields[i]);
			sb.append(quotedCharacter);
		}
		return sb.toString();
	}

	/**
	 * Public setter for the delimiter.
	 * 
	 * @param delimiter
	 *            the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void setQuotedCharacter(String quotedCharacter) {
		this.quotedCharacter = quotedCharacter;
	}

}