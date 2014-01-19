package fr.jerep6.ogi.service;

import java.awt.image.BufferedImage;

import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.DPE;

public interface ServiceDPE extends TransactionalService<DPE, Integer> {

	BufferedImage generateDPEkWhImage(Integer dpe, Integer width) throws TechnicalException;

	BufferedImage generateDPEGesImage(Integer dpe, Integer width) throws TechnicalException;

}
