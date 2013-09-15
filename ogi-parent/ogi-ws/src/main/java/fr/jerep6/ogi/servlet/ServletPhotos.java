package fr.jerep6.ogi.servlet;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.obj.PhotoDimension;

public class ServletPhotos extends HttpServlet {
	private static final long	serialVersionUID	= -8348693134869320794L;
	private final Logger		LOGGER				= LoggerFactory.getLogger(ServletPhotos.class);

	/** War context path */
	private String				contextPath;

	private String				photosStorageDir;
	private String				photosStorageUrl;

	private String				photosProtocol;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-Type", "image/jpeg");

		try {
			// Read photo
			InputStream is = getInputStream(request);

			// If dimension defined => resize else serve original photo
			if (!Strings.isNullOrEmpty(request.getParameter("size"))) {
				serveResizedImg(request, response, is);
			}
			else { // Serve original image
				response.getOutputStream().write(toByteArray(is));
			}
		}
		catch (Exception e) {
			LOGGER.warn("Error for serving image " + request.getRequestURI(), e);

			// Serve error image
			serveResizedImg(request, response, Files.newInputStream(Paths.get(photosStorageDir, "error.jpg")));
		}

	}

	/**
	 * If parameter size is not set return will be null
	 * 
	 * @param parameter
	 * @return
	 */
	private PhotoDimension getDimension(String parameter) {
		PhotoDimension d = null;

		if (PhotoDimension.THUMB.getName().equals(parameter)) {
			d = PhotoDimension.THUMB;
		}
		else if (PhotoDimension.MEDIUM.getName().equals(parameter)) {
			d = PhotoDimension.MEDIUM;
		}
		else if (PhotoDimension.BIG.getName().equals(parameter)) {
			d = PhotoDimension.BIG;
		}
		return d;
	}

	private InputStream getInputStream(HttpServletRequest request) throws IOException {
		InputStream is = null;

		switch (photosProtocol) {
			case "file":
				is = Files.newInputStream(Paths.get(photosStorageDir).resolve(getRelativePhotoPath(request)));
				break;

			case "http":
				URL u = new URL(photosStorageUrl + getRelativePhotoPath(request).toString().replace("\\", "/"));
				is = u.openStream();
				break;

			default:
				LOGGER.info("Photo protocol unknow {}", photosProtocol);
				break;
		}
		return is;
	}

	/**
	 * Get photo path into url <br />
	 * Exemple : http://localhost:8080/ogi-ws/photos/ref1/a?size=thumb return ref1/a
	 * 
	 * @param request
	 * @return
	 */
	private Path getRelativePhotoPath(HttpServletRequest request) {
		String begin = contextPath + request.getServletPath() + "/";
		String relativePath = request.getRequestURI().replaceFirst(begin, "");

		return Paths.get(relativePath);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextPath = config.getServletContext().getContextPath();
		photosStorageDir = ContextUtils.getProperty("photos.storage.dir");
		photosStorageUrl = ContextUtils.getProperty("photos.storage.url");
		photosProtocol = ContextUtils.getProperty("photos.protocol");
	}

	private BufferedImage resizeImage(BufferedImage originalImage, PhotoDimension d) {
		// Original
		Integer ow = originalImage.getWidth();
		Integer oh = originalImage.getHeight();

		// Destination
		Integer dw = d.getWidth();
		Integer dh = d.getHeight();

		// Original rapport
		Float or = ow.floatValue() / oh.floatValue();

		Integer width, height;
		// Width is base
		if (ow >= oh) {
			width = dw;
			height = ((Float) (dw / or)).intValue();
		}
		// Height is base
		else {
			width = ((Float) (dh * or)).intValue();
			height = dh;
		}

		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}

	private void serveResizedImg(HttpServletRequest request, HttpServletResponse response, InputStream is)
			throws IOException {
		PhotoDimension d = getDimension(request.getParameter("size"));

		// Read image
		BufferedImage resized = resizeImage(ImageIO.read(is), d);
		ImageIO.write(resized, "jpeg", response.getOutputStream());
	}

	public byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);

		}
		return output.toByteArray();

	}
}