package fr.jerep6.ogi.servlet.operation;

import java.awt.image.BufferedImage;
import java.util.Map;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

public class OperationSize extends Operation {
	private Integer	w, h;
	private String	mode	= "automatic";

	public OperationSize(Map<String, String[]> parameter) {
		// extract size
		String[] sizes = parameter.get("size");
		if (sizes != null && sizes.length > 0) {
			String[] dimensions = sizes[0].split(",");
			try {
				w = Integer.valueOf(dimensions[0]);
				h = Integer.valueOf(dimensions[1]);
			} catch (NumberFormatException nfe) {
				w = null;
				h = null;
			}
		}

		// extract mode
		String[] modes = parameter.get("sizeMode");
		if (modes != null && modes.length > 0) {
			mode = modes[0];
		}
	}

	@Override
	public BufferedImage compute(BufferedImage img) {
		if (w != null && h != null) {
			Mode scalrMode = Mode.AUTOMATIC;
			Integer width = w;
			Integer height = h;

			if ("inversed_automatic".equals(mode)) {
				scalrMode = Mode.FIT_EXACT;
				if (img.getWidth() > img.getHeight()) {
					float ratio = height * 1.0F / img.getHeight();
					width = (int) (img.getWidth() * ratio);
				} else {
					float ratio = width * 1.0F / img.getWidth();
					height = (int) (img.getHeight() * ratio);
				}
			}
			return Scalr.resize(img, scalrMode, width, height);
		}
		return img;
	}

}
