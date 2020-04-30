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


public class LdpcCodeRateAndInfoBlockLength implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private BerInteger rate1Over2 = null;
	private BerInteger rate2Over3 = null;
	private BerInteger rate4Over5 = null;
	private BerInteger rate223Over255 = null;
	
	public LdpcCodeRateAndInfoBlockLength() {
	}

	public LdpcCodeRateAndInfoBlockLength(byte[] code) {
		this.code = code;
	}

	public void setRate1Over2(BerInteger rate1Over2) {
		this.rate1Over2 = rate1Over2;
	}

	public BerInteger getRate1Over2() {
		return rate1Over2;
	}

	public void setRate2Over3(BerInteger rate2Over3) {
		this.rate2Over3 = rate2Over3;
	}

	public BerInteger getRate2Over3() {
		return rate2Over3;
	}

	public void setRate4Over5(BerInteger rate4Over5) {
		this.rate4Over5 = rate4Over5;
	}

	public BerInteger getRate4Over5() {
		return rate4Over5;
	}

	public void setRate223Over255(BerInteger rate223Over255) {
		this.rate223Over255 = rate223Over255;
	}

	public BerInteger getRate223Over255() {
		return rate223Over255;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (rate223Over255 != null) {
			codeLength += rate223Over255.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (rate4Over5 != null) {
			codeLength += rate4Over5.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			reverseOS.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (rate2Over3 != null) {
			codeLength += rate2Over3.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			reverseOS.write(0x81);
			codeLength += 1;
			return codeLength;
		}
		
		if (rate1Over2 != null) {
			codeLength += rate1Over2.encode(reverseOS, false);
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
			rate1Over2 = new BerInteger();
			codeLength += rate1Over2.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			rate2Over3 = new BerInteger();
			codeLength += rate2Over3.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			rate4Over5 = new BerInteger();
			codeLength += rate4Over5.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			rate223Over255 = new BerInteger();
			codeLength += rate223Over255.decode(is, false);
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

		if (rate1Over2 != null) {
			sb.append("rate1Over2: ").append(rate1Over2);
			return;
		}

		if (rate2Over3 != null) {
			sb.append("rate2Over3: ").append(rate2Over3);
			return;
		}

		if (rate4Over5 != null) {
			sb.append("rate4Over5: ").append(rate4Over5);
			return;
		}

		if (rate223Over255 != null) {
			sb.append("rate223Over255: ").append(rate223Over255);
			return;
		}

		sb.append("<none>");
	}

}
