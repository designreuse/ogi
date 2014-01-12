package fr.jerep6.ogi.utils;

import java.math.BigDecimal;

public class VenteUtils {

	public static Float round(Float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	public static Float roundPrice(Float price) {
		return round(price, 2);
	}
}
