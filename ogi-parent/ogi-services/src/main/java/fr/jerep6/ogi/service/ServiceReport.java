package fr.jerep6.ogi.service;

import java.io.ByteArrayOutputStream;

import fr.jerep6.ogi.enumeration.EnumPageSize;
import fr.jerep6.ogi.enumeration.EnumReport;
import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.Service;
import fr.jerep6.ogi.enumeration.EnumGestionMode;

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
	 * @param pageSize
	 *            size of page (A4, A3)
	 * @param gestionMode
	 *            generate report for rent or sale (optional. default sale)            
	 * @return bytes of generates reports
	 * @throws TechnicalException
	 *             if error during generation
	 */
	ByteArrayOutputStream generate(String prpReference, EnumReport reportType, String format, EnumPageSize pageSize, EnumGestionMode gestionMode)
			throws TechnicalException;

}
