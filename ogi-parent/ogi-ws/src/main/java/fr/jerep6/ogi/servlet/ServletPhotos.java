package fr.jerep6.ogi.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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

			ImageIO.write(img, "jpeg", response.getOutputStream());
		} catch (Exception e) {
			LOGGER.warn("Error for serving image " + request.getRequestURI(), e);
		}

	}

	private BufferedImage getImage(HttpServletRequest request) throws IOException {
		InputStream is = Files.newInputStream(Paths.get(photosStorageDir).resolve(getRelativePhotoPath(request)));
		return ImageIO.read(is);
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
		photosStorageDir = ContextUtils.getProperty("document.storage.dir");
	}
}