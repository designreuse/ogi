package fr.jerep6.ogi.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jerep6.ogi.service.ServiceSale;
import fr.jerep6.ogi.transfert.ExpiredMandate;

/**
 * @author jerep6
 */
@RestController
@RequestMapping(value = "/mandate", produces = "application/json;charset=UTF-8")
public class WSMandate extends AbtractWS {

	@Autowired
	private ServiceSale	serviceSale;

	@RequestMapping(value = "/expired", method = RequestMethod.GET)
	public Map<String, List<ExpiredMandate>> listExpired() {
		return serviceSale.listExpiredMandates();

	}
}