package fr.jerep6.ogi.service.impl;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import fr.jerep6.ogi.enumeration.EnumDPE;
import fr.jerep6.ogi.exception.technical.enumeration.EnumTechnicalError;
import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.obj.DPEStep;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.dao.DaoDPE;
import fr.jerep6.ogi.service.ServiceDPE;
import fr.jerep6.ogi.utils.DocumentUtils;

@Service("serviceDPE")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDPEImpl extends AbstractTransactionalService<DPE, Integer> implements ServiceDPE {
	private final Logger			LOGGER				= LoggerFactory.getLogger(ServiceDPEImpl.class);

	/** Size of generated image if not provided */
	private static final Integer	DEFAULT_IMG_SIZE	= 260;

	private static final int		DPE_KWH_MAX			= 500;
	private static final int		DPE_GES_MAX			= 90;

	/** Configuration for kwh dpe */
	private static List<DPEStep>	DPE_KWH_STEPS		= Arrays.asList(new DPEStep(1, 0, 50),//
																new DPEStep(2, 51, 39),//
																new DPEStep(3, 91, 59),//
																new DPEStep(4, 151, 79),//
																new DPEStep(5, 231, 99),//
																new DPEStep(6, 331, 119),//
																new DPEStep(7, 451, 100));
	/** Configuration for ges dpe */
	private static List<DPEStep>	DPE_GES_STEPS		= Arrays.asList(new DPEStep(1, 0, 5),//
																new DPEStep(2, 6, 4),//
																new DPEStep(3, 11, 9),//
																new DPEStep(4, 21, 14),//
																new DPEStep(5, 36, 19),//
																new DPEStep(6, 56, 24),//
																new DPEStep(7, 81, 20));

	@Autowired
	private DaoDPE					daoDpe;

	private BufferedImage generateDPEGesImage(Integer dpe, Integer width) throws TechnicalException {
		// If width not provided => 260px
		Integer w = Objects.firstNonNull(width, DEFAULT_IMG_SIZE);

		try {
			// Init according to images templates
			Map<String, Integer> conf = new HashMap<String, Integer>();
			Integer pxStepGap = 6;
			conf.put("pxStep", 62); // Taille en px d'un step de DPE (A, B, ...)
			conf.put("pxStepGap", pxStepGap);
			conf.put("pxMin", 35 - pxStepGap);
			conf.put("pxMiddleArrow", 31); // nbre de px à partir du haut de l'image ou se trouve le milieu de la flèche
			conf.put("widthPasteArrow", 383);
			conf.put("pxTxtWidth", 65);
			conf.put("pxTxtHeight", 42);
			conf.put("txtFontSize", 30);

			DPEStep step;
			String sDpe;
			if (dpe != null) {
				// Search dpe step
				sDpe = dpe.toString();
				step = DPE_GES_STEPS.get(0);
				for (DPEStep aStep : DPE_GES_STEPS) {
					if (dpe >= aStep.getMin()) {
						step = aStep;
					}
				}
			} else { // No dpe given => arrow in middle width no text
				step = DPE_GES_STEPS.get(3);
				dpe = 35;
				sDpe = "";
			}

			return writeDpeImg(step, sDpe, dpe, DPE_GES_MAX, "img/dpe-ges_template.png", "img/dpe-ges_value.png", conf,
					w);

		} catch (IOException ioe) {
			throw new TechnicalException(EnumTechnicalError.DPE, ioe);
		}
	}

	@Override
	public void generateDPEGesImage(OutputStream output, Integer dpe, Integer width) throws TechnicalException {
		Preconditions.checkNotNull(output);
		BufferedImage img = generateDPEGesImage(dpe, width);
		try {
			ImageIO.write(img, EnumDPE.getImageFormatName(), output);
			output.flush();
			output.close();
		} catch (IOException ioe) {
			throw new TechnicalException(EnumTechnicalError.DPE, ioe);
		}

	}

	private BufferedImage generateDPEkWhImage(Integer dpe, Integer width) throws TechnicalException {
		// If width not provided => 250px
		Integer w = Objects.firstNonNull(width, DEFAULT_IMG_SIZE);

		try {
			// Init according to images templates
			Map<String, Integer> conf = new HashMap<String, Integer>();
			Integer pxStepGap = 3;
			conf.put("pxStep", 47); // Taille en px d'un step de DPE (A, B, ...)
			conf.put("pxStepGap", pxStepGap);
			conf.put("pxMin", 31 - pxStepGap);
			conf.put("pxMiddleArrow", 23); // nbre de px à partir duquel ou se trouve le milieu de la flèche
			conf.put("widthPasteArrow", 305);
			conf.put("pxTxtWidth", 40);
			conf.put("pxTxtHeight", 33);
			conf.put("txtFontSize", 25);

			DPEStep step;
			String sDpe;
			if (dpe != null) {
				// Search dpe step
				sDpe = dpe.toString();
				step = DPE_KWH_STEPS.get(0);
				for (DPEStep aStep : DPE_KWH_STEPS) {
					if (dpe >= aStep.getMin()) {
						step = aStep;
					}
				}
			} else { // No dpe given => arrow in middle width no text
				step = DPE_KWH_STEPS.get(3);
				dpe = 230;
				sDpe = "";
			}

			return writeDpeImg(step, sDpe, dpe, DPE_KWH_MAX, "img/dpe-kwh_template.png", "img/dpe-kwh_value.png", conf,
					w);

		} catch (IOException ioe) {
			throw new TechnicalException(EnumTechnicalError.DPE, ioe);
		}
	}

	@Override
	public void generateDPEkWhImage(OutputStream output, Integer dpe, Integer width) throws TechnicalException {
		Preconditions.checkNotNull(output);
		BufferedImage img = generateDPEkWhImage(dpe, width);
		try {
			ImageIO.write(img, EnumDPE.getImageFormatName(), output);
			output.flush();
			output.close();
		} catch (IOException ioe) {
			throw new TechnicalException(EnumTechnicalError.DPE, ioe);
		}

	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDpe);
	}

	public void setAlpha(BufferedImage img, byte alpha) {
		alpha %= 0xff;
		for (int cx = 0; cx < img.getWidth(); cx++) {
			for (int cy = 0; cy < img.getHeight(); cy++) {
				int color = img.getRGB(cx, cy);

				int mc = alpha << 24 | 0x00ffffff;
				int newcolor = color & mc;
				img.setRGB(cx, cy, newcolor);
			}

		}
	}

	@Override
	public void writeDPEFiles(String prpReference, DPE dpe) {
		Preconditions.checkNotNull(prpReference);

		// If no dpe => do nothing
		if (dpe == null) {
			return;
		}

		try {
			Path dpeDirectory = DocumentUtils.getDirectory(prpReference).resolve(Paths.get("dpe"));
			Files.createDirectories(dpeDirectory);

			if (dpe.getKwh() != null) {
				for (EnumDPE aDpe : EnumDPE.getKwh()) {
					// Compute file path
					Path p = DocumentUtils.absolutize(dpeDirectory.resolve(aDpe.getFileName()));
					FileOutputStream fos = new FileOutputStream(p.toString());
					generateDPEkWhImage(fos, dpe.getKwh(), aDpe.getSize());
				}
			}

			if (dpe.getGes() != null) {
				for (EnumDPE aDpe : EnumDPE.getGes()) {
					// Compute file path
					Path p = DocumentUtils.absolutize(dpeDirectory.resolve(aDpe.getFileName()));
					FileOutputStream fos = new FileOutputStream(p.toString());
					generateDPEGesImage(fos, dpe.getGes(), aDpe.getSize());
				}
			}

		} catch (IOException ioe) {
			throw new TechnicalException(EnumTechnicalError.DPE, ioe);
		}
	}

	/**
	 * 
	 * @param step
	 *            step du DPE (A, B, C ...)
	 * @param sDpe
	 *            texte à afficher dans la flèche noire
	 * @param dpe
	 *            valeur du dpe pour faire les calculs de positionnement
	 * @param pathTemplate
	 *            chemin de l'image template du dpe
	 * @param pathValue
	 *            chemin de l'image contenant la flèche noire du dpe
	 * @param width
	 *            taille de l'image à générer
	 * @param conf
	 *            Map conportant obligatoirement les paramètres suivants :
	 *            pxMin : hauteur dans l'image (px) ayant la valeur 0 du dpe
	 *            pxStep : hauteur d'un step en px
	 *            pxStepGap : hauteur entre deux strep en px
	 *            pxMiddleArrow : hauteur d'un step en px
	 *            pxStep : hauteur dans l'image du milieu de ma flèche
	 *            pxTxtWidth : nbre de pixel à partir duquel écrire la valeur du DPE (horizontal) dans la flèche
	 *            pxTxtHeight : nbre de pixel à partir duquel écrire la valeur du DPE (vertical) dans la flèche
	 *            txtFontSize : taille de la police d'écriture dans la flèche
	 * @return
	 * @throws IOException
	 */
	private BufferedImage writeDpeImg(DPEStep step, String sDpe, Integer dpe, Integer dpeMax, String pathTemplate,
			String pathValue, Map<String, Integer> conf, Integer width) throws IOException {
		BufferedImage dpeTemplate = ImageIO.read(new ClassPathResource(pathTemplate).getInputStream());
		BufferedImage dpeValue = ImageIO.read(new ClassPathResource(pathValue).getInputStream());

		Integer pxBoundMin = conf.get("pxMin") + (step.getNum() - 1) * conf.get("pxStep") + step.getNum()
				* conf.get("pxStepGap");
		Integer pxDpe = pxBoundMin + ((dpe > dpeMax ? dpeMax : dpe) - step.getMin()) * conf.get("pxStep")
				/ step.getPlage() - conf.get("pxMiddleArrow");

		Graphics graphicsDpeValue = dpeValue.getGraphics();
		graphicsDpeValue.setFont(new Font("TimesRoman", Font.PLAIN, conf.get("txtFontSize")));
		graphicsDpeValue.drawString(sDpe, conf.get("pxTxtWidth"), conf.get("pxTxtHeight"));

		BufferedImage combined = new BufferedImage(dpeTemplate.getWidth(), dpeTemplate.getHeight(),
				BufferedImage.TYPE_INT_BGR);

		Graphics g = combined.getGraphics();
		g.drawImage(dpeTemplate, 0, 0, null);
		g.drawImage(dpeValue, conf.get("widthPasteArrow"), pxDpe, null);

		combined = Scalr.resize(combined, width);

		return combined;
	}

}