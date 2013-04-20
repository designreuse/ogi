package fr.jerep6.ogi.transfert.bean;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealPropertyTo {
	private String				reference;
	private Set<String>			equipments;
	private Integer				landArea;
	private Float				cos;
	private Boolean				housingEstate;
	private AddressTo			address;
	private Set<DescriptionTo>	descriptions;
	private CategoryTo			category;
	private String				type;
}