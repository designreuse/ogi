package fr.jerep6.ogi.framework.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import fr.jerep6.ogi.framework.security.CryptageAES;

/**
 * Property configurer to load encrypted values with AES algorithm.
 * Encrypted value must be prefix ENC( and sufixed with ) like Jasypt
 * Exemple : ENC(G6N718UuyPE5bHyWKyuLQSm02auQPUtm)
 * 
 * @author jerep6 16 févr. 2014
 */
public class CryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private static Logger		LOGGER					= LoggerFactory
																.getLogger(CryptablePropertyPlaceholderConfigurer.class);
	private static final String	ENCRYPTED_VALUE_PREFIX	= "ENC(";
	private static final String	ENCRYPTED_VALUE_SUFFIX	= ")";

	private final CryptageAES	algo;

	public CryptablePropertyPlaceholderConfigurer(String skey) {
		super();
		algo = new CryptageAES(skey);
	}

	@Override
	protected String convertPropertyValue(String originalValue) {
		String v = originalValue;

		if (isEncryptedValue(originalValue)) {
			try {
				v = algo.decrypt(getInnerEncryptedValue(originalValue));
			} catch (Exception e) {
				LOGGER.error("Error decrypt property. Original value = " + originalValue, e);
			}
		}
		return v;
	}

	private String getInnerEncryptedValue(String value) {
		return value.substring(ENCRYPTED_VALUE_PREFIX.length(), value.length() - ENCRYPTED_VALUE_SUFFIX.length());
	}

	public boolean isEncryptedValue(String value) {
		if (value == null) {
			return false;
		}

		String trimmedValue = value.trim();
		return trimmedValue.startsWith(ENCRYPTED_VALUE_PREFIX) && trimmedValue.endsWith(ENCRYPTED_VALUE_SUFFIX);
	}

}