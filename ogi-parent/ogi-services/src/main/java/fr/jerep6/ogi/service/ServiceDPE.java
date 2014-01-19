package fr.jerep6.ogi.service;

import java.awt.image.BufferedImage;

import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.DPE;

public interface ServiceDPE extends TransactionalService<DPE, Integer> {

	BufferedImage generateDPEkWhImage(DPE d, Integer width) throws TechnicalException;

}
