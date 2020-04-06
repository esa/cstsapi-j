package esa.egos.csts.api.util.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class contains static methods to aid the use of the CSTS.
 */
public class CSTSUtils {

	private static final Charset CSTS_CHARSET = StandardCharsets.UTF_8;

	/**
	 * Encodes a String to a byte array according the specified Charset for the
	 * CSTS.
	 * 
	 * @param string
	 *            the String to encode
	 * @return the encoded String in a byte array
	 */
	public static byte[] encodeString(String string) {
		return string.getBytes(CSTS_CHARSET);
	}

	/**
	 * Decodes a byte array to a String according the specified Charset for the
	 * CSTS.
	 * 
	 * @param bytes
	 *            the byte array to decode
	 * @return the decoded byte array as a String
	 */
	public static String decodeString(byte[] bytes) {
		return new String(bytes, CSTS_CHARSET);
	}

	/**
	 * Make the first character of the string upper-case
	 * @param str The string 
	 * @return The updated string
	 */
    public static String firstToUpperCase(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Make the first character of the string lower-case
     * @param str The string 
     * @return The updated string
     */
    public static String firstToLowerCase(String str) {
    return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
