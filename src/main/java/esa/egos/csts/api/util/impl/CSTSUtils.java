package esa.egos.csts.api.util.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerNull;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;

import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;

/**
 * This class contains static methods to aid the use of the CSTS SFW.
 */
public class CSTSUtils {

	private static final Charset CSTS_CHARSET = StandardCharsets.UTF_8;
	
	public static byte[] encodeString(String string) {
		return string.getBytes(CSTS_CHARSET);
	}
	
	public static String decodeString(byte[] bytes) {
		return new String(bytes, CSTS_CHARSET);
	}
	
	/**
	 * Factory method for Extended objects which are initially set to "notUsed".
	 * 
	 * @return an Extended object which is set to "notUsed"
	 */
	public static Extended nonUsedExtension() {
		Extended extension = new Extended();
		extension.setNotUsed(new BerNull());
		return extension;
	}

	/**
	 * Compares the identifier of an Extended object with the specified
	 * identifier.
	 * 
	 * @param extension
	 *            the Extended object
	 * @param identifier
	 *            the BerObjectIdentifier object
	 * @return true if the identifiers are equal, false otherwise
	 */
	public static boolean equalsIdentifier(Extended extension, BerObjectIdentifier identifier) {
		return equalsIdentifier(extension.getExternal(), identifier);
	}

	/**
	 * Compares the identifier of an Embedded object with the specified
	 * identifier.
	 * 
	 * @param embedded
	 *            the Embedded object
	 * @param identifier
	 *            the BerObjectIdentifier object
	 * @return true if the identifiers are equal, false otherwise
	 */
	public static boolean equalsIdentifier(Embedded embedded, BerObjectIdentifier identifier) {
		return equalsIdentifier(embedded.getIdentification().getSyntax(), identifier);
	}

	/**
	 * Compares two BerObjectIdentifier objects with each other.
	 * 
	 * @param identifier1
	 *            the first BerObjectIdentifier object
	 * @param identifier2
	 *            the second BerObjectIdentifier object
	 * @return true if the identifiers are equal, false otherwise
	 */
	public static boolean equalsIdentifier(BerObjectIdentifier identifier1, BerObjectIdentifier identifier2) {
		return Arrays.equals(identifier1.value, identifier2.value);
	}

}
