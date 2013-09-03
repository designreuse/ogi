package fr.jerep6.ogi.servlet;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.obj.PhotoDimension;

public class Photos extends HttpServlet {
	private static final long	serialVersionUID	= -8348693134869320794L;

	/** War context path */
	private String				contextPath;

	private String				photosDir;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-Type", "image/jpeg");

		Path p = getRealPhotoPath(request);
		PhotoDimension d = getDimension(request.getParameter("size"));

		// Dimension is defined => resize
		if (d != null) {
			// Read image
			BufferedImage originalImage = ImageIO.read(p.toFile());
			BufferedImage resized = resizeImage(originalImage, d);
			ImageIO.write(resized, "jpeg", response.getOutputStream());
		}
		// Serve original image
		else {
			response.getOutputStream().write(Files.readAllBytes(p));
		}

	}

	/**
	 * If parameter size is not set return will be null
	 * 
	 * @param parameter
	 * @return
	 */
	private PhotoDimension getDimension(String parameter) {
		if (parameter == null) { return null; }

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

	/**
	 * Get photo path into url <br />
	 * Exemple : http://localhost:8080/ogi-ws/photos/ref1/a?size=thumb return ref1/a
	 * 
	 * @param request
	 * @return
	 */
	private Path getRealPhotoPath(HttpServletRequest request) {
		String begin = contextPath + request.getServletPath() + "/";
		String relativePath = request.getRequestURI().replaceFirst(begin, "");

		return Paths.get(photosDir, relativePath);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextPath = config.getServletContext().getContextPath();
		photosDir = ContextUtils.getProperty("photos.dir");
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
}