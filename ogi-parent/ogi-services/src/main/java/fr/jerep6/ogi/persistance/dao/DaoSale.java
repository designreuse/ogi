package fr.jerep6.ogi.persistance.dao;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.transfert.ExpiredMandate;

public interface DaoSale extends DaoCRUD<Sale, Integer> {

	Sale readFromPropertyReference(String prpReference);

	/**
	 * List sale with end date between endDateBegin and endDateEnd
	 *
	 * @param endDateBegin
	 *            mandatEndDate must be greater than
	 * @param endDateEnd
	 *            mandatEndDate must be leather than
	 * @return
	 */
	List<ExpiredMandate> listMandats(Optional<ZonedDateTime> endDateBegin, Optional<ZonedDateTime> endDateEnd);

}
