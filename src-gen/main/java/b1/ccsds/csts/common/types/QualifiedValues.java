/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.common.types;

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


public class QualifiedValues implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private TypeAndValueComplexQualified valid = null;
	private BerNull unavailable = null;
	private BerNull undefined = null;
	private BerNull error = null;
	
	public QualifiedValues() {
	}

	public QualifiedValues(byte[] code) {
		this.code = code;
	}

	public void setValid(TypeAndValueComplexQualified valid) {
		this.valid = valid;
	}

	public TypeAndValueComplexQualified getValid() {
		return valid;
	}

	public void setUnavailable(BerNull unavailable) {
		this.unavailable = unavailable;
	}

	public BerNull getUnavailable() {
		return unavailable;
	}

	public void setUndefined(BerNull undefined) {
		this.undefined = undefined;
	}

	public BerNull getUndefined() {
		return undefined;
	}

	public void setError(BerNull error) {
		this.error = error;
	}

	public BerNull getError() {
		return error;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		int sublength;

		if (error != null) {
			codeLength += error.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (undefined != null) {
			codeLength += undefined.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			reverseOS.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (unavailable != null) {
			codeLength += unavailable.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			reverseOS.write(0x81);
			codeLength += 1;
			return codeLength;
		}
		
		if (valid != null) {
			sublength = valid.encode(reverseOS);
			codeLength += sublength;
			codeLength += BerLength.encodeLength(reverseOS, sublength);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 0
			reverseOS.write(0xA0);
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

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 0)) {
			codeLength += BerLength.skip(is);
			valid = new TypeAndValueComplexQualified();
			codeLength += valid.decode(is, null);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			unavailable = new BerNull();
			codeLength += unavailable.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			undefined = new BerNull();
			codeLength += undefined.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			error = new BerNull();
			codeLength += error.decode(is, false);
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

		if (valid != null) {
			sb.append("valid: ");
			valid.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (unavailable != null) {
			sb.append("unavailable: ").append(unavailable);
			return;
		}

		if (undefined != null) {
			sb.append("undefined: ").append(undefined);
			return;
		}

		if (error != null) {
			sb.append("error: ").append(error);
			return;
		}

		sb.append("<none>");
	}

}
