package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.transfert.PartnerPropertyCount;

public interface DaoPartnerRequest extends DaoCRUD<PartnerRequest, Integer> {

	List<PartnerPropertyCount> countPropertyOnPartners();

	PartnerRequest lastRequests(EnumPartner partner, Integer prpTechid);

	List<PartnerRequest> lastRequests();

}
