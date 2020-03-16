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


public class TcPlopSyncPlop implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private BerInteger plop1IdleSequenceLength = null;
	private BerNull plop2 = null;
	
	public TcPlopSyncPlop() {
	}

	public TcPlopSyncPlop(byte[] code) {
		this.code = code;
	}

	public void setPlop1IdleSequenceLength(BerInteger plop1IdleSequenceLength) {
		this.plop1IdleSequenceLength = plop1IdleSequenceLength;
	}

	public BerInteger getPlop1IdleSequenceLength() {
		return plop1IdleSequenceLength;
	}

	public void setPlop2(BerNull plop2) {
		this.plop2 = plop2;
	}

	public BerNull getPlop2() {
		return plop2;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (plop2 != null) {
			codeLength += plop2.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			reverseOS.write(0x81);
			codeLength += 1;
			return codeLength;
		}
		
		if (plop1IdleSequenceLength != null) {
			codeLength += plop1IdleSequenceLength.encode(reverseOS, false);
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
			plop1IdleSequenceLength = new BerInteger();
			codeLength += plop1IdleSequenceLength.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			plop2 = new BerNull();
			codeLength += plop2.decode(is, false);
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

		if (plop1IdleSequenceLength != null) {
			sb.append("plop1IdleSequenceLength: ").append(plop1IdleSequenceLength);
			return;
		}

		if (plop2 != null) {
			sb.append("plop2: ").append(plop2);
			return;
		}

		sb.append("<none>");
	}

}
