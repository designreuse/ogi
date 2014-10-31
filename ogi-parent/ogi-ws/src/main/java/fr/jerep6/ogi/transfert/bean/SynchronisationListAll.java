package fr.jerep6.ogi.transfert.bean;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class SynchronisationListAll {
	private Map<String, List<PartnerRequestTo>>	lastRequests;
	private List<PartnerPropertyCountTo>		partnerPropertyCount;
}
