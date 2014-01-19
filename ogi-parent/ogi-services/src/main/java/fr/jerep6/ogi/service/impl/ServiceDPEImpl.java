package fr.jerep6.ogi.service.impl;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.obj.DPEStep;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.dao.DaoDPE;
import fr.jerep6.ogi.service.ServiceDPE;

@Service("serviceDPE")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceDPEImpl extends AbstractTransactionalService<DPE, Integer> implements ServiceDPE {
	private final Logger			LOGGER		= LoggerFactory.getLogger(ServiceDPEImpl.class);
	private static final int		DPE_MAX		= 500;

	private static List<DPEStep>	DPE_STEPS	= Arrays.asList(new DPEStep(1, 0, 50),//
														new DPEStep(2, 51, 39),//
														new DPEStep(3, 91, 59),//
														new DPEStep(4, 151, 79),//
														new DPEStep(5, 231, 99),//
														new DPEStep(6, 331, 119),//
														new DPEStep(7, 451, 100));

	@Autowired
	private DaoDPE					daoDpe;

	@Override
	public BufferedImage generateDPEkWhImage(DPE d, Integer width) throws TechnicalException {
		Integer w = Objects.firstNonNull(width, 250);
		try {
			// Init according to images templates
			Integer pxStep = 63;
			Integer pxStepGap = 8;

			Integer pxMin = 49 - pxStepGap;
			Integer pxMiddleArrow = 32; // nbre de px à partir du haut de l'image ou se trouve le milieu de la flèche
										// (dpe_value.png)

			Integer dpe = d.getKWh();

			DPEStep currentStep = DPE_STEPS.get(0);
			for (DPEStep aStep : DPE_STEPS) {
				if (dpe >= aStep.getMin()) {
					currentStep = aStep;
				}
			}

			BufferedImage dpeTemplate = ImageIO.read(new ClassPathResource("img/dpe_template.png").getInputStream());
			BufferedImage dpeValue = ImageIO.read(new ClassPathResource("img/dpe_value.png").getInputStream());

			Integer pxBoundMin = pxMin + (currentStep.getNum() - 1) * pxStep + currentStep.getNum() * pxStepGap;
			Integer pxDpe = pxBoundMin + ((dpe > DPE_MAX ? DPE_MAX : dpe) - currentStep.getMin()) * pxStep
					/ currentStep.getPlage() - pxMiddleArrow;

			Graphics graphicsDpeValue = dpeValue.getGraphics();
			graphicsDpeValue.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			graphicsDpeValue.drawString(dpe.toString(), 50, 42);

			BufferedImage combined = new BufferedImage(dpeTemplate.getWidth(), dpeTemplate.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			Graphics g = combined.getGraphics();
			g.drawImage(dpeTemplate, 0, 0, null);
			g.drawImage(dpeValue, 520, pxDpe, null);

			combined = Scalr.resize(combined, w);
			return combined;

		} catch (IOException ioe) {
			throw new TechnicalException();
		}
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoDpe);
	}

}