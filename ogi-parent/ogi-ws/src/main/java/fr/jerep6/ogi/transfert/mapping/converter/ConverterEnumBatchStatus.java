package fr.jerep6.ogi.transfert.mapping.converter;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import org.springframework.batch.core.BatchStatus;

import fr.jerep6.ogi.rest.batch.transfert.BatchReportStatusTo;

public class ConverterEnumBatchStatus extends BidirectionalConverter<BatchStatus, BatchReportStatusTo> {

	@Override
	public BatchStatus convertFrom(BatchReportStatusTo source, Type<BatchStatus> destinationType) {
		return null;
	}

	@Override
	public BatchReportStatusTo convertTo(BatchStatus source, Type<BatchReportStatusTo> destinationType) {
		BatchReportStatusTo brs = new BatchReportStatusTo();
		brs.setCode(source.toString());
		brs.setIsRunning(source.isRunning());
		brs.setIsUnsuccessful(source.isUnsuccessful());
		return brs;
	}

}