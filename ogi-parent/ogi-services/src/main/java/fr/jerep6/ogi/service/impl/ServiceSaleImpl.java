package fr.jerep6.ogi.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.jerep6.ogi.exception.business.enumeration.EnumError;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.dao.DaoSale;
import fr.jerep6.ogi.service.ServiceSale;
import fr.jerep6.ogi.transfert.mapping.OrikaMapperService;

@Service("serviceSale")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceSaleImpl extends AbstractTransactionalService<Sale, Integer> implements ServiceSale {

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
	public Sale merge(String prpReference, Sale saleModif) {
		// control the consistency of the object
		validate(saleModif);

		Sale saleMapping = null;

		// Read sale from database according to property reference
		Sale saleBd = daoSale.readFromPropertyReference(prpReference);

		// Map field only if sale found
		if (saleBd != null) {
			saleMapping = saleBd;
			mapper.map(saleModif, saleMapping);
		} else { // no sale in database so create
			saleMapping = saleModif;
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
			mbe.add(EnumError.SALE_MANDAT_DATE);
		}

		mbe.checkErrors();
	}

}