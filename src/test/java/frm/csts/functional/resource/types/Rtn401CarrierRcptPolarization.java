/**
 * This class file was automatically generated by jASN1 v1.11.3-SNAPSHOT (http://www.beanit.com)
 */

package frm.csts.functional.resource.types;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import com.beanit.jasn1.ber.*;
import com.beanit.jasn1.ber.types.*;
import com.beanit.jasn1.ber.types.string.*;


public class Rtn401CarrierRcptPolarization implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private BerNull lhc = null;
	private BerNull rhc = null;
	private BerInteger autoHysteresis = null;
	private BerInteger combiningBwdth = null;
	
	public Rtn401CarrierRcptPolarization() {
	}

	public Rtn401CarrierRcptPolarization(byte[] code) {
		this.code = code;
	}

	public void setLhc(BerNull lhc) {
		this.lhc = lhc;
	}

	public BerNull getLhc() {
		return lhc;
	}

	public void setRhc(BerNull rhc) {
		this.rhc = rhc;
	}

	public BerNull getRhc() {
		return rhc;
	}

	public void setAutoHysteresis(BerInteger autoHysteresis) {
		this.autoHysteresis = autoHysteresis;
	}

	public BerInteger getAutoHysteresis() {
		return autoHysteresis;
	}

	public void setCombiningBwdth(BerInteger combiningBwdth) {
		this.combiningBwdth = combiningBwdth;
	}

	public BerInteger getCombiningBwdth() {
		return combiningBwdth;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (combiningBwdth != null) {
			codeLength += combiningBwdth.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (autoHysteresis != null) {
			codeLength += autoHysteresis.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			reverseOS.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (rhc != null) {
			codeLength += rhc.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			reverseOS.write(0x81);
			codeLength += 1;
			return codeLength;
		}
		
		if (lhc != null) {
			codeLength += lhc.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 0
			reverseOS.write(0x80);
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
			lhc = new BerNull();
			codeLength += lhc.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			rhc = new BerNull();
			codeLength += rhc.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			autoHysteresis = new BerInteger();
			codeLength += autoHysteresis.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			combiningBwdth = new BerInteger();
			codeLength += combiningBwdth.decode(is, false);
			return codeLength;
		}

		if (passedTag != null) {
			return 0;
		}

		throw new IOException("Error decoding CHOICE: Tag " + berTag + " matched to no item.");
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		ReverseByteArrayOutputStream reverseOS = new ReverseByteArrayOutputStream(encodingSizeGuess);
		encode(reverseOS);
		code = reverseOS.getArray();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendAsString(sb, 0);
		return sb.toString();
	}

	public void appendAsString(StringBuilder sb, int indentLevel) {

		if (lhc != null) {
			sb.append("lhc: ").append(lhc);
			return;
		}

		if (rhc != null) {
			sb.append("rhc: ").append(rhc);
			return;
		}

		if (autoHysteresis != null) {
			sb.append("autoHysteresis: ").append(autoHysteresis);
			return;
		}

		if (combiningBwdth != null) {
			sb.append("combiningBwdth: ").append(combiningBwdth);
			return;
		}

		sb.append("<none>");
	}

}

