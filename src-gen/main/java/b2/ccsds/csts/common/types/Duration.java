/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.common.types;

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


public class Duration implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private IntUnsigned seconds = null;
	private IntUnsigned milliseconds = null;
	private IntUnsigned microseconds = null;
	
	public Duration() {
	}

	public Duration(byte[] code) {
		this.code = code;
	}

	public void setSeconds(IntUnsigned seconds) {
		this.seconds = seconds;
	}

	public IntUnsigned getSeconds() {
		return seconds;
	}

	public void setMilliseconds(IntUnsigned milliseconds) {
		this.milliseconds = milliseconds;
	}

	public IntUnsigned getMilliseconds() {
		return milliseconds;
	}

	public void setMicroseconds(IntUnsigned microseconds) {
		this.microseconds = microseconds;
	}

	public IntUnsigned getMicroseconds() {
		return microseconds;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (microseconds != null) {
			codeLength += microseconds.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			reverseOS.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (milliseconds != null) {
			codeLength += milliseconds.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			reverseOS.write(0x81);
			codeLength += 1;
			return codeLength;
		}
		
		if (seconds != null) {
			codeLength += seconds.encode(reverseOS, false);
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
			seconds = new IntUnsigned();
			codeLength += seconds.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			milliseconds = new IntUnsigned();
			codeLength += milliseconds.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			microseconds = new IntUnsigned();
			codeLength += microseconds.decode(is, false);
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

		if (seconds != null) {
			sb.append("seconds: ").append(seconds);
			return;
		}

		if (milliseconds != null) {
			sb.append("milliseconds: ").append(milliseconds);
			return;
		}

		if (microseconds != null) {
			sb.append("microseconds: ").append(microseconds);
			return;
		}

		sb.append("<none>");
	}

}
