package fr.jerep6.ogi.persistance.dao;

import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.Sale;

public interface DaoSale extends DaoCRUD<Sale, Integer> {

	Sale readFromPropertyReference(String prpReference);

}
