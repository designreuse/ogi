package fr.jerep6.ogi.search.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSale {
	private String	mandateReference;
	private Float	price;
	private Boolean	sold;
}
