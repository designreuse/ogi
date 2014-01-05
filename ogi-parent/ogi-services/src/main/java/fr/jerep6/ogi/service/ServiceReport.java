package fr.jerep6.ogi.service;

import java.io.ByteArrayOutputStream;

import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.Service;

public interface ServiceReport extends Service {
	static String	FORMAT_WORD	= "docx";
	static String	FORMAT_PDF	= "pdf";

	ByteArrayOutputStream generateShopFront(String prpReference, String format) throws TechnicalException;

}
