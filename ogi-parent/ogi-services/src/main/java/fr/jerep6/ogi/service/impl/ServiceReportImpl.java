package fr.jerep6.ogi.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.exception.technical.JasperException;
import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceReport;

@Service("serviceReport")
public class ServiceReportImpl extends AbstractService implements ServiceReport {
	private final Logger		LOGGER	= LoggerFactory.getLogger(ServiceReportImpl.class);

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

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
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
		exporter.exportReport();
		return bos;
	}

	@Override
	public ByteArrayOutputStream generateShopFront(String prpReference, String format) throws TechnicalException {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(prpReference));

		try {
			JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("/report1.jasper"));

			RealProperty readByReference = serviceRealProperty.readByReference(prpReference);
			List<RealProperty> l = new ArrayList<>();
			l.add(readByReference);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("criteres", "test de critere");

			JRDataSource dataSource = new JRBeanCollectionDataSource(l);
			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

			return generate(print, format);
		} catch (Exception e) {
			LOGGER.error("Error generating report for property" + prpReference, e);
			throw new JasperException(e.getMessage(), e);
		}
	}

}
