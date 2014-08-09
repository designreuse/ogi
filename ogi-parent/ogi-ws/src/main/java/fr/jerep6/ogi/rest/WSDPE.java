package fr.jerep6.ogi.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.enumeration.EnumDPE;
import fr.jerep6.ogi.service.ServiceDPE;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.transfert.mapping.OrikaMapper;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/dpe", produces = "application/json;charset=UTF-8")
public class WSDPE extends AbtractWS {

	@Autowired
	private ServiceDPE			serviceDPE;

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Autowired
	private OrikaMapper			mapper;

	@RequestMapping(value = "/ges", method = RequestMethod.GET)
	public ResponseEntity<byte[]> generateImgGes(@RequestParam("dpe") Integer dpeValue,
			@RequestParam("width") Integer width) throws IOException {

		ByteArrayOutputStream baos = serviceDPE.generateDPEGesImage(dpeValue, width);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(EnumDPE.getImageMimeType()));
		return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/kwh", method = RequestMethod.GET)
	public ResponseEntity<byte[]> generateImgKwh(@RequestParam("dpe") Integer dpeValue,
			@RequestParam("width") Integer width) throws IOException {

		ByteArrayOutputStream baos = serviceDPE.generateDPEkWhImage(dpeValue, width);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(EnumDPE.getImageMimeType()));
		return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
	}

}