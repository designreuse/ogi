package fr.jerep6.ogi.framework.transfert;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExceptionTo {

	private List<ErrorTo>	errors	= new ArrayList<>(1);

	public void add(ErrorTo error) {
		errors.add(error);
	}

	public boolean isMultiple() {
		return errors.size() > 1;
	}

}
