/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.common.types;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import org.openmuc.jasn1.ber.*;
import org.openmuc.jasn1.ber.types.*;
import org.openmuc.jasn1.ber.types.string.*;


public class AbstractChoice implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private BerOctetString opaqueString = null;
	private Embedded extendedData = null;
	
	public AbstractChoice() {
	}

	public AbstractChoice(byte[] code) {
		this.code = code;
	}

	public void setOpaqueString(BerOctetString opaqueString) {
		this.opaqueString = opaqueString;
	}

	public BerOctetString getOpaqueString() {
		return opaqueString;
	}

	public void setExtendedData(Embedded extendedData) {
		this.extendedData = extendedData;
	}

	public Embedded getExtendedData() {
		return extendedData;
	}

	public int encode(BerByteArrayOutputStream os) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				os.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (extendedData != null) {
			codeLength += extendedData.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			os.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (opaqueString != null) {
			codeLength += opaqueString.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 0
			os.write(0x80);
			codeLength += 1;
			return codeLength;
		}
		
		throw new IOException("Error encoding CHOICE: No element of CHOICE was selected.");
	}

	public int decode(InputStream is) throws IOException {
		return decode(is, null);
	}

	public int decode(InputStream is, BerTag berTag) throws IOException {

		int codeLength = 0;
		BerTag passedTag = berTag;

		if (berTag == null) {
			berTag = new BerTag();
			codeLength += berTag.decode(is);
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 0)) {
			opaqueString = new BerOctetString();
			codeLength += opaqueString.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			extendedData = new Embedded();
			codeLength += extendedData.decode(is, false);
			return codeLength;
		}

		if (passedTag != null) {
			return 0;
		}

		throw new IOException("Error decoding CHOICE: Tag " + berTag + " matched to no item.");
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
		encode(os);
		code = os.getArray();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendAsString(sb, 0);
		return sb.toString();
	}

	public void appendAsString(StringBuilder sb, int indentLevel) {

		if (opaqueString != null) {
			sb.append("opaqueString: ").append(opaqueString);
			return;
		}

		if (extendedData != null) {
			sb.append("extendedData: ");
			extendedData.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

