package fr.jerep6.ogi.servlet.operation;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class OperationWatermark extends Operation {
	private Map<String, BufferedImage> watermark = new HashMap<>();
	private BufferedImage waterMarkToUse;

	public OperationWatermark(Map<String, String[]> parameter, Map<String, BufferedImage> builderWatermarks) {
		this.watermark = builderWatermarks;
		
		String[] water = parameter.get("w");
		if (water != null && water.length > 0) {
			waterMarkToUse = watermark.get(water[0]);
		}
	}

	@Override
	public BufferedImage compute(BufferedImage img) {
		if (waterMarkToUse != null) {
			BufferedImage watermarkImage = waterMarkToUse;

			// initializes necessary graphic properties
			Graphics2D g2d = (Graphics2D) img.getGraphics();
			AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
			g2d.setComposite(alphaChannel);

			// calculates the coordinate where the image is painted
			int topLeftX = (img.getWidth() - watermarkImage.getWidth()) / 2;
			int topLeftY = (img.getHeight() - watermarkImage.getHeight()) / 2;

			// paints the image watermark
			g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);
			g2d.dispose();
		}
		return img;
	}


	public static class Builder {
		private Map<String, String[]> parameters;
		private Map<String, BufferedImage> builderWatermarks = new HashMap<>();
		
		public Builder parameters(Map<String, String[]> parameters) {
			this.parameters = parameters;
			return this;
		}

		public Builder webSiteLogo(BufferedImage img) {
			builderWatermarks.put("website", img);
			return this;
		}

		public Builder photoSphere(BufferedImage img) {
			builderWatermarks.put("photosphere", img);
			return this;
		}

		public OperationWatermark build() {
			return new OperationWatermark(parameters, builderWatermarks);
		}
	}
	
	public static Builder Builder() {
		return new Builder();
	}
}