package fr.jerep6.ogi.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.servlet.operation.Operation;
import fr.jerep6.ogi.servlet.operation.OperationCrop;
import fr.jerep6.ogi.servlet.operation.OperationSize;

@WebServlet(name = "Photos2", urlPatterns = { "/photos2/*" })
public class ServletPhotos extends HttpServlet {
	private static final long	serialVersionUID	= -8348693134869320794L;
	private final Logger		LOGGER				= LoggerFactory.getLogger(ServletPhotos.class);

	/** War context path */
	private String				contextPath;

	private String				photosStorageDir;

	/** Image to display when error occured */
	private Path				imgError;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-Type", "image/jpeg");

		try {
			// Read photo
			BufferedImage img = getImage(request);
			Map<String, String[]> parameters = request.getParameterMap();

			List<Operation> operations = new ArrayList<>(2);
			operations.add(new OperationSize(parameters));
			operations.add(new OperationCrop(parameters));

			for (Operation operation : operations) {
				img = operation.compute(img);
			}

			// One year cache
			response.setHeader("Cache-Control", "max-age=1314000");
			ImageIO.write(img, "jpeg", response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error for serving image " + request.getRequestURI(), e);
		}

	}

	/**
	 * @param request
	 *            http request into extract image path
	 * @return image corresponding to url path or ff url path doesn't exist return error img
	 * @throws IOException
	 */
	private BufferedImage getImage(HttpServletRequest request) throws IOException {
		Path img = Paths.get(photosStorageDir).resolve(getRelativePhotoPath(request));
		if (!Files.exists(img)) {
			img = imgError;
		}
		InputStream is = Files.newInputStream(img);
		BufferedImage buffered = ImageIO.read(is);
		is.close();
		return buffered;
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
		try {
			relativePath = URLDecoder.decode(relativePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error decode " + relativePath, e);
		}
		return Paths.get(relativePath);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextPath = config.getServletContext().getContextPath();
		photosStorageDir = ContextUtils.getProperty("document.storage.dir");
		imgError = Paths.get(photosStorageDir).resolve(Paths.get("error.jpg"));
	}
}