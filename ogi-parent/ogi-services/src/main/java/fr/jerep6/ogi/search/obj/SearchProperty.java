package fr.jerep6.ogi.search.obj;

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

	private SearchAddress	address;
	private SearchSale		sale;
	private SearchRent		rent;

	private SearchImage		image;

}
