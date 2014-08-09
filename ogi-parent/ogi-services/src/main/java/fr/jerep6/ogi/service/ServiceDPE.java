package fr.jerep6.ogi.service;

import java.io.ByteArrayOutputStream;

import fr.jerep6.ogi.framework.exception.TechnicalException;
import fr.jerep6.ogi.framework.service.TransactionalService;
import fr.jerep6.ogi.persistance.bo.DPE;

public interface ServiceDPE extends TransactionalService<DPE, Integer> {

	ByteArrayOutputStream generateDPEkWhImage(Integer dpe, Integer width) throws TechnicalException;

	ByteArrayOutputStream generateDPEGesImage(Integer dpe, Integer width) throws TechnicalException;

	/**
	 * Write to disk dpe images into dpe folder of property.
	 * If dpe is null write an image without value
	 *
	 * Images names :
	 * <ul>
	 * <li>kwh-180.png</li>
	 * <li>kwh-260.png</li>
	 * <li>ges-180.png</li>
	 * <li>ges-260.png</li>
	 * </ul>
	 *
	 * @param prpReference
	 *            property reference
	 * @param dpe
	 *            dpe dpe values. if null do nothing
	 */
	void writeDPEFiles(String prpReference, DPE dpe) throws TechnicalException;

}
