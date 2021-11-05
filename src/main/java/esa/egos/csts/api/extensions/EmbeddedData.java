package esa.egos.csts.api.extensions;

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
		return "EmbeddedData [oid=" + oid + ", data=" + Arrays.toString(data) + "]";
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
