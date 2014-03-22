package fr.jerep6.ogi.rest.batch.transfert;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchReportStatusTo {
	private String	code;
	private Boolean	isRunning;
	private Boolean	isUnsuccessful;
}
