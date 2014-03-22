package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import org.springframework.batch.core.ExitStatus;

import fr.jerep6.ogi.rest.batch.transfert.BatchReportExitStatusTo;

public class ConverterEnumBatchExitStatus extends BidirectionalConverter<ExitStatus, BatchReportExitStatusTo> {

	@Override
	public ExitStatus convertFrom(BatchReportExitStatusTo source, Type<ExitStatus> destinationType) {
		return null;
	}

	@Override
	public BatchReportExitStatusTo convertTo(ExitStatus source, Type<BatchReportExitStatusTo> destinationType) {
		BatchReportExitStatusTo bres = new BatchReportExitStatusTo();
		bres.setCode(source.getExitCode());
		bres.setDescription(source.getExitDescription());
		return bres;
	}

}