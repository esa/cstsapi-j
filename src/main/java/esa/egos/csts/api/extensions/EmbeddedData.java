package esa.egos.csts.api.extensions;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerEmbeddedPdv.Identification;
import org.openmuc.jasn1.ber.types.BerObjectIdentifier;
import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.types.Embedded;
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
	public Embedded encode() {
		Embedded embedded = new Embedded();
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
	public static EmbeddedData decode(Embedded embedded) {
		ObjectIdentifier oid = ObjectIdentifier.of(embedded.getIdentification().getSyntax().value);
		byte[] data = embedded.getDataValue().value;
		return new EmbeddedData(oid, data);
	}

	@Override
	public String toString() {
		return "EmbeddedData [oid=" + oid + ", data=" + Arrays.toString(data) + "]";
	}

}
