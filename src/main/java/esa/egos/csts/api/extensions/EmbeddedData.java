package esa.egos.csts.api.extensions;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.beanit.jasn1.ber.types.BerEmbeddedPdv.Identification;

import com.beanit.jasn1.ber.types.BerObjectIdentifier;
import com.beanit.jasn1.ber.types.BerOctetString;

import esa.egos.csts.api.oids.ObjectIdentifier;

/**
 * This class represents the CCSDS Embedded type.
 * 
 * This class is immutable.
 */
public class EmbeddedData {

	private final ObjectIdentifier oid;
	private final byte[] data;

	private EmbeddedData(ObjectIdentifier oid, byte[] data) {
		this.oid = oid;
		this.data = data;
	}

	/**
	 * Creates an EmbeddedData from the specified Object Identifier and an encoded
	 * byte array.
	 * 
	 * @param oid
	 *            the specified ObjectIdentifier
	 * @param data
	 *            the specified encoded byte array
	 * @return an EmbeddedData with the specified parameter
	 */
	public static EmbeddedData of(ObjectIdentifier oid, byte[] data) {
		return new EmbeddedData(oid, data);
	}

	/**
	 * Returns the Object Identifier.
	 * 
	 * @return the Object Identifier
	 */
	public ObjectIdentifier getOid() {
		return oid;
	}

	/**
	 * Returns the encoded byte array.
	 * 
	 * @return the encoded byte array
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Encodes this EmbeddedData into an CCSDS Embedded type.
	 * 
	 * @return the encoded CCSDS Embedded type
	 */
	public b1.ccsds.csts.common.types.Embedded encode(b1.ccsds.csts.common.types.Embedded embedded) {
		Identification identification = new Identification();
		identification.setSyntax(new BerObjectIdentifier(oid.toArray()));
		embedded.setIdentification(identification);
		embedded.setDataValue(new BerOctetString(data));
		return embedded;
	}
	
	public b2.ccsds.csts.common.types.Embedded encode(b2.ccsds.csts.common.types.Embedded embedded) {
		Identification identification = new Identification();
		identification.setSyntax(new BerObjectIdentifier(oid.toArray()));
		embedded.setIdentification(identification);
		embedded.setDataValue(new BerOctetString(data));
		return embedded;
	}

	/**
	 * Decodes a specified CCSDS Embedded type.
	 * 
	 * @param embedded
	 *            the specified CCSDS Embedded type
	 * @return a new EmbeddedData decoded from the specified CCSDS Embedded type
	 */
	public static EmbeddedData decode( b1.ccsds.csts.common.types.Embedded embedded) {
		ObjectIdentifier oid = ObjectIdentifier.of(embedded.getIdentification().getSyntax().value);
		byte[] data = embedded.getDataValue().value;
		return new EmbeddedData(oid, data);
	}
	
	public static EmbeddedData decode( b2.ccsds.csts.common.types.Embedded embedded) {
		ObjectIdentifier oid = ObjectIdentifier.of(embedded.getIdentification().getSyntax().value);
		byte[] data = embedded.getDataValue().value;
		return new EmbeddedData(oid, data);
	}

	@Override
	public String toString() {
		return "EmbeddedData [oid=" + oid + ", data=\n" + dumpHex(data, 1024) + "]";
	}

	/**
     * Dumps the given byte array into the result argument
     * 
     * @param bytes
     *            The bytes to dump
     * @param numHexBytes the number of hex bytes to dump           
     * @param result
     *            The output parameter carrying the result
     * @throws UnsupportedEncodingException
     */
    public static String dumpHex(byte[] bytes, int numHexBytes)
    {
    	StringBuilder result = new StringBuilder();
    	
        if (bytes == null || result == null)
        {
            return "";
        }
        
		try {

			// use the smaller length provided
			final int length = numHexBytes < bytes.length ? numHexBytes : bytes.length;

			final int columnWidth = 32;
			for (int row = 0; row < length; row += columnWidth) {
				for (int c = row; c < (columnWidth + row) && c < bytes.length; c++) {
					result.append(String.format("%02X", bytes[c]) + " ");
				}

				result.append("\t\t");

				for (int c = row; c < (columnWidth + row) && c < bytes.length; c++) {
					String s = new String(bytes, c, 1, "UTF-8");
					if (Character.isLetter(s.charAt(0)) == false) {
						s = ".";
					}
					result.append(s);
				}

				result.append(System.lineSeparator());
			}
		} catch (Exception e) {
			result.append("Exception formatting hex dump: " + e);
		}
        return result.toString();
    }	
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof EmbeddedData)) {
			return false;
		}
		EmbeddedData embeddedData = (EmbeddedData)o;
		if (!embeddedData.getOid().equals(this.oid)) {
			return false;
		}
	    return Arrays.equals(embeddedData.getData(), this.data);
	}
	
	@Override
	public int hashCode() {
	    int hash = 31 * (31 + this.oid.hashCode()) + Arrays.hashCode(this.data);
	    return hash;
	}

}
