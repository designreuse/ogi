package fr.jerep6.ogi.search.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRealProperty {
	private String				reference;
	private String				category;
	private Float				landArea;
	private Float				area;
	private Boolean				housingEstate;
	private List<String>		modes	= new ArrayList<>();

	private SearchAddress		address;
	private SearchSale			sale;
	private SearchRent			rent;

	private SearchImage			image;

	private List<SearchOwner>	owners	= new ArrayList<>();

}
