package fr.jerep6.ogi.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.technical.JasperTechnicalException;
import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.service.ServiceReport;

@Service("serviceReport")
public class ServiceReportImpl extends AbstractService implements ServiceReport {
	private final Logger				LOGGER			= LoggerFactory.getLogger(ServiceReportImpl.class);

	@Autowired
	private DataSource					dataSource;

	/** Folder which are stored jasper reports */
	@Value("${jasper.directory.root}")
	private String						jasperRootDirectory;

	/** Folder which are stored documents from properties (photos, dpe ...) */
	@Value("${document.storage.dir}")
	private String						jasperDocumentDirectory;

	private static Map<String, String>	reportsConfig	= new HashMap<>();
	static {
		reportsConfig.put("classeur", "fiche_classeur.jasper");
		reportsConfig.put("vitrine", "fiche_vitrine.jasper");
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
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
		exporter.exportReport();
		return bos;
	}

	@Override
	public ByteArrayOutputStream generate(String prpReference, String reportType, String format)
			throws TechnicalException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpReference));

		// Get file name from type
		String reportName = reportsConfig.get(reportType);
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reportName));

		try {
			// Create report
			JasperReport report = (JasperReport) JRLoader.loadObject(new FileInputStream(jasperRootDirectory + "/"
					+ reportName));

			// Create parameters's report
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ROOT_DIR", jasperRootDirectory);
			parameters.put("DOC_DIR", jasperDocumentDirectory);
			parameters.put("reference", prpReference);

			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());

			// Generate report
			return generate(print, format);
		} catch (Exception e) {
			LOGGER.error("Error generating report for property " + prpReference, e);
			throw new JasperTechnicalException(e.getMessage(), e);
		}
	}
}
