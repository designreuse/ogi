package fr.jerep6.ogi.service.impl;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.exception.business.enumeration.EnumBusinessErrorProperty;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.dao.DaoSale;
import fr.jerep6.ogi.service.ServiceSale;
import fr.jerep6.ogi.transfert.ExpiredMandate;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceSale")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceSaleImpl extends AbstractTransactionalService<Sale, Integer> implements ServiceSale {

	@Value("${mandat.soonexpired.days}")
	private Integer				nbDaySoonExpired;

	@Autowired
	private DaoSale				daoSale;

	@Autowired
	private OrikaMapperService	mapper;

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoSale);
	}

	@Override
	public Map<String, List<ExpiredMandate>> listExpiredMandates() {
		Map<String, List<ExpiredMandate>> result = new HashMap<>();
		// Expired => mandatEndDate is over
		result.put("EXPIRED", daoSale.listMandats(Optional.empty(), Optional.of(ZonedDateTime.now())));
		// soon expired => mandatEndDate is over in 15 days
		result.put(
				"SOON_EXPIRED",
				daoSale.listMandats(Optional.of(ZonedDateTime.now()),
						Optional.of(ZonedDateTime.now().plusDays(nbDaySoonExpired))));
		return result;
	}

	@Override
	public Sale merge(Sale saleOriginalBD, Sale saleModif) {
		// control the consistency of the object
		validate(saleModif);

		Sale saleMapping = null;

		// IHM send a sale
		if (saleModif != null) {
			// Map field only if sale found and saleModif (ihm) is not null
			if (saleOriginalBD != null) {
				saleMapping = saleOriginalBD;
				mapper.map(saleModif, saleMapping);
			}
			// no sale in database and ihm is not null
			else if (saleOriginalBD == null) {
				saleMapping = saleModif;
			}

		} else {
			// ihm null and database contain sale => remove sale in database
			if (saleOriginalBD != null) {
				remove(saleOriginalBD);
			}
		}

		return saleMapping;
	}

	@Override
	public void validate(Sale sale) throws MultipleBusinessException {

		// Accept null object
		if (sale == null) {
			return;
		}

		MultipleBusinessException mbe = new MultipleBusinessException();

		// Begin mandate < end mandate
		if (sale.getMandateStartDate() != null && sale.getMandateEndDate() != null //
				&& sale.getMandateStartDate().after(sale.getMandateEndDate())) {
			mbe.add(EnumBusinessErrorProperty.SALE_MANDAT_DATE);
		}

		mbe.checkErrors();
	}

}