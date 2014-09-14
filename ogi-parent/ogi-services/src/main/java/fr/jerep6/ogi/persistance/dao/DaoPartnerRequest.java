package fr.jerep6.ogi.persistance.dao;

import java.util.List;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;

public interface DaoPartnerRequest extends DaoCRUD<PartnerRequest, Integer> {

	boolean lastRequestIs(EnumPartner partner, Integer prpTechid, List<EnumPartnerRequestType> l);

	PartnerRequest lastRequests(EnumPartner partner, Integer prpTechid);

	List<PartnerRequest> lastRequests();

}
