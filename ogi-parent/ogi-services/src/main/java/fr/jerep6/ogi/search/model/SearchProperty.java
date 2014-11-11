package fr.jerep6.ogi.search.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchProperty {
	private String			reference;
	private String			category;
	private Float			landArea;
	private Float			area;
	private Boolean			housingEstate;
	private List<String>	modes;

	private SearchAddress	address;
	private SearchSale		sale;
	private SearchRent		rent;

	private SearchImage		image;

}
