package fr.jerep6.ogi.enumeration;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum EnumDPE {
	KWH_180("kwh", 180), //
	KWH_260("kwh", 260), //
	GES_180("ges", 180), //
	GES_260("ges", 260); //

	/** Image format */
	private static String	IMG_MIME_TYPE	= "image/png";

	public static List<EnumDPE> getGes() {
		List<EnumDPE> l = new ArrayList<>(2);

		for (EnumDPE aEnum : EnumDPE.values()) {
			if (aEnum.getType().equals("ges")) {
				l.add(aEnum);
			}
		}

		return l;
	}

	/**
	 * Compute format name from mime type. (remove "image/")
	 * 
	 * @return format name usable for ImageIO.write
	 */
	public static String getImageFormatName() {
		return IMG_MIME_TYPE.replace("image/", "");
	}

	public static String getImageMimeType() {
		return IMG_MIME_TYPE;
	}

	public static List<EnumDPE> getKwh() {
		List<EnumDPE> l = new ArrayList<>(2);

		for (EnumDPE aEnum : EnumDPE.values()) {
			if (aEnum.getType().equals("kwh")) {
				l.add(aEnum);
			}
		}

		return l;
	}

	/**
	 * Get the enumeration from this code
	 * 
	 * @param code
	 * @return
	 */
	public static EnumDPE valueOf(String type, Integer size) {
		for (EnumDPE oneEnum : EnumDPE.values()) {
			if (oneEnum.getType().equals(type) && oneEnum.getSize().equals(size)) {
				return oneEnum;
			}
		}

		throw new IllegalArgumentException("No " + EnumDPE.class.getSimpleName() + " for " + type + " and " + size);
	}

	private String	type;

	private Integer	size;

	private EnumDPE(String type, Integer size) {
		this.type = type;
		this.size = size;
	}

	public String getFileName() {
		StringBuilder sb = new StringBuilder();
		sb.append(type).append("-").append(size);
		sb.append(".").append(getImageFormatName());
		return sb.toString();
	}
}
