package fr.jerep6.ogi.service;

import java.io.ByteArrayOutputStream;

import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.Service;

public interface ServiceReport extends Service {
	static String	FORMAT_WORD	= "docx";
	static String	FORMAT_ODT	= "odt";
	static String	FORMAT_PDF	= "pdf";

	/**
	 * Generate a report from a property
	 * 
	 * @param prpReference
	 *            property reference. Must be NOT NULL
	 * @param reportType
	 *            type of report (classeur, vitrine)
	 * @param format
	 *            format name (pdf or docx)
	 * @return bytes of generates reports
	 * @throws TechnicalException
	 *             if error during generation
	 */
	ByteArrayOutputStream generate(String prpReference, String reportType, String format) throws TechnicalException;

}
