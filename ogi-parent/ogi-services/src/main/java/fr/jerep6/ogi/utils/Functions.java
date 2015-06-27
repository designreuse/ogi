package fr.jerep6.ogi.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Functions {
	public static String computeRentReference(String ref) {
		return ref + suffixRent;
	}

	public static String computeSaleReference(String ref) {
		return ref + suffixSale;
	}

	public static String returnReference(String ref) {
		return ref;
	}
	
	public static String addReferenceToPhotoName(String photoName, String ref) {
		Path p = Paths.get(photoName);
		Path renamedFile = Paths.get(ref+"-"+p.getFileName());
		
		if(p.getParent() != null) {
			return p.getParent().resolve(renamedFile).toString(); 
		} else {
			return renamedFile.toString();
		}
	}
	
	
	public static String removeReferenceToPhotoName(String photoName) {
		Path p = Paths.get(photoName);

		Path renamedFile = Paths.get(p.getFileName().toString().replaceAll("^([a-zA-Z0-9_]+)-", ""));
		
		if(p.getParent() != null) {
			return p.getParent().resolve(renamedFile).toString(); 
		} else {
			return renamedFile.toString();
		}
	}
	

	private static String	suffixSale;

	private static String	suffixRent;

	@Value("${partner.reference.rent.suffix}")
	private void setSuffixRent(String suffix) {
		suffixRent = suffix;
	}

	@Value("${partner.reference.sale.suffix}")
	private void setSuffixSale(String suffix) {
		suffixSale = suffix;
	}
}
