package fr.jerep6.ogi.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Functions {
	private static String	suffixSale;
	private static String	suffixRent;

	public static String computeRentReference(String ref) {
		return ref + suffixRent;
	}

	public static String computeSaleReference(String ref) {
		return ref + suffixSale;
	}

	@Value("${partner.reference.rent.suffix}")
	private void setSuffixRent(String suffix) {
		suffixRent = suffix;
	}

	@Value("${partner.reference.sale.suffix}")
	private void setSuffixSale(String suffix) {
		suffixSale = suffix;
	}
}
