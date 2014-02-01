package fr.jerep6.ogi.servlet.operation;

import java.awt.image.BufferedImage;
import java.util.Map;

import org.imgscalr.Scalr;

public class OperationCrop extends Operation {
	private Integer	w, h;

	public OperationCrop(Map<String, String[]> parameter) {
		String[] sizes = parameter.get("crop");
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
	}

	@Override
	public BufferedImage compute(BufferedImage img) {
		if (w != null && h != null) {
			try {
				return Scalr.crop(img, w, h);
			} catch (Exception e) {}
		}
		return img;
	}

}
