package fr.jerep6.ogi.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;

public class AbstractJaxRsWS {
	public final static String	APPLICATION_JSON_UTF8	= MediaType.APPLICATION_JSON + "; charset=UTF-8";

	protected byte[] toByte(BufferedImage img, String formatImg) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, formatImg, baos);
		return baos.toByteArray();
	}
}