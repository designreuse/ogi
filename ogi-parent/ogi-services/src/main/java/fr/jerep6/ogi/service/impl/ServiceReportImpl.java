package fr.jerep6.ogi.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.enumeration.EnumCategory;
import fr.jerep6.ogi.enumeration.EnumPageSize;
import fr.jerep6.ogi.enumeration.EnumReport;
import fr.jerep6.ogi.exception.technical.JasperTechnicalException;
import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceReport;

@Service("serviceReport")
public class ServiceReportImpl extends AbstractService implements ServiceReport {
	private final Logger					LOGGER			= LoggerFactory.getLogger(ServiceReportImpl.class);

	@Autowired
	private DataSource						dataSource;

	/** Folder which are stored documents from properties (photos, dpe ...) */
	@Value("${document.storage.dir}")
	private String							jasperDocumentDirectory;

	@Autowired
	private ServiceRealProperty				serviceRealProperty;

	private static Map<EnumReport, String>	reportsConfig	= new HashMap<>(2);
	static {
		reportsConfig.put(EnumReport.CLIENT, "fiche_client_vente$SUFFIXE.jasper");
		reportsConfig.put(EnumReport.VITRINE, "fiche_vitrine_$PAGESIZE$SUFFIXE.jasper");
	}

	private String computeReportName(String prpReference, EnumReport reportType, EnumPageSize pageSize) {
		// If no format => A4
		pageSize = pageSize == null ? EnumPageSize.A4 : pageSize;

		String reportName = reportsConfig.get(reportType);

		Optional<RealProperty> prp = serviceRealProperty.readByReference(prpReference);

		// Replace format
		reportName = reportName.replace("$PAGESIZE", pageSize.getCode());

		switch (reportType) {
			case VITRINE:
				// Suffixe only with A4
				if (pageSize == EnumPageSize.A4) {
					reportName = replaceSuffix(reportName, prp.get().getCategory());
				} else {
					reportName = reportName.replace("$SUFFIXE", "");
				}
				break;
			case CLIENT:
				reportName = replaceSuffix(reportName, prp.get().getCategory());
				break;
		}

		return reportName;
	}

	private ByteArrayOutputStream generate(JasperPrint print, String format) throws JRException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(format));

		JRAbstractExporter exporter = null;
		switch (format) {
			case FORMAT_PDF:
				exporter = new JRPdfExporter();
				break;

			case FORMAT_WORD:
				exporter = new JRDocxExporter();
				break;

			case FORMAT_ODT:
				exporter = new JROdtExporter();
				break;
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
		exporter.exportReport();
		return bos;
	}

	@Override
	public ByteArrayOutputStream generate(String prpReference, EnumReport reportType, String format,
			EnumPageSize pageSize) throws TechnicalException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpReference));

		// Get file name from type
		String reportName = computeReportName(prpReference, reportType, pageSize);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reportName));

		try {
			// Create report
			JasperReport report = (JasperReport) JRLoader.loadObject(this.getClass().getClassLoader()
					.getResourceAsStream("jasper/" + reportName));

			// Create parameters's report
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("DOC_DIR", jasperDocumentDirectory);
			parameters.put("reference", prpReference);
			parameters.put(JRParameter.REPORT_LOCALE, Locale.FRANCE);

			Connection connection = dataSource.getConnection();
			JasperPrint print = JasperFillManager.fillReport(report, parameters, connection);

			// Generate report
			ByteArrayOutputStream data = generate(print, format);
			connection.close();
			return data;
		} catch (Exception e) {
			LOGGER.error("Error generating report for property " + prpReference, e);
			throw new JasperTechnicalException(e.getMessage(), e);
		}
	}

	private String replaceSuffix(String reportName, Category categ) {
		return reportName.replace("$SUFFIXE", EnumCategory.PLOT == categ.getCode() ? "_T" : "");
	}
}
