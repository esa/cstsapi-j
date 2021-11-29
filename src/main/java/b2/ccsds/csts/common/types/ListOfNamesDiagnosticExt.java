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


public class ListOfNamesDiagnosticExt implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class UnknownNames implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);
		public byte[] code = null;
		private List<UnknownName> seqOf = null;

		public UnknownNames() {
			seqOf = new ArrayList<UnknownName>();
		}

		public UnknownNames(byte[] code) {
			this.code = code;
		}

		public List<UnknownName> getUnknownName() {
			if (seqOf == null) {
				seqOf = new ArrayList<UnknownName>();
			}
			return seqOf;
		}

		public int encode(OutputStream reverseOS) throws IOException {
			return encode(reverseOS, true);
		}

		public int encode(OutputStream reverseOS, boolean withTag) throws IOException {

			if (code != null) {
				for (int i = code.length - 1; i >= 0; i--) {
					reverseOS.write(code[i]);
				}
				if (withTag) {
					return tag.encode(reverseOS) + code.length;
				}
				return code.length;
			}

			int codeLength = 0;
			for (int i = (seqOf.size() - 1); i >= 0; i--) {
				codeLength += seqOf.get(i).encode(reverseOS, true);
			}

			codeLength += BerLength.encodeLength(reverseOS, codeLength);

			if (withTag) {
				codeLength += tag.encode(reverseOS);
			}

			return codeLength;
		}

		public int decode(InputStream is) throws IOException {
			return decode(is, true);
		}

		public int decode(InputStream is, boolean withTag) throws IOException {
			int codeLength = 0;
			int subCodeLength = 0;
			if (withTag) {
				codeLength += tag.decodeAndCheck(is);
			}

			BerLength length = new BerLength();
			codeLength += length.decode(is);
			int totalLength = length.val;

			while (subCodeLength < totalLength) {
				UnknownName element = new UnknownName();
				subCodeLength += element.decode(is, true);
				seqOf.add(element);
			}
			if (subCodeLength != totalLength) {
				throw new IOException("Decoded SequenceOf or SetOf has wrong length. Expected " + totalLength + " but has " + subCodeLength);

			}
			codeLength += subCodeLength;

			return codeLength;
		}

		public void encodeAndSave(int encodingSizeGuess) throws IOException {
			ReverseByteArrayOutputStream reverseOS = new ReverseByteArrayOutputStream(encodingSizeGuess);
			encode(reverseOS, false);
			code = reverseOS.getArray();
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			appendAsString(sb, 0);
			return sb.toString();
		}

		public void appendAsString(StringBuilder sb, int indentLevel) {

			sb.append("{\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (seqOf == null) {
				sb.append("null");
			}
			else {
				Iterator<UnknownName> it = seqOf.iterator();
				if (it.hasNext()) {
					it.next().appendAsString(sb, indentLevel + 1);
					while (it.hasNext()) {
						sb.append(",\n");
						for (int i = 0; i < indentLevel + 1; i++) {
							sb.append("\t");
						}
						it.next().appendAsString(sb, indentLevel + 1);
					}
				}
			}

			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	private UnknownNames unknownNames = null;
	private AdditionalText unknownDefault = null;
	private Embedded diagnosticExtension = null;
	
	public ListOfNamesDiagnosticExt() {
	}

	public ListOfNamesDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setUnknownNames(UnknownNames unknownNames) {
		this.unknownNames = unknownNames;
	}

	public UnknownNames getUnknownNames() {
		return unknownNames;
	}

	public void setUnknownDefault(AdditionalText unknownDefault) {
		this.unknownDefault = unknownDefault;
	}

	public AdditionalText getUnknownDefault() {
		return unknownDefault;
	}

	public void setDiagnosticExtension(Embedded diagnosticExtension) {
		this.diagnosticExtension = diagnosticExtension;
	}

	public Embedded getDiagnosticExtension() {
		return diagnosticExtension;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (diagnosticExtension != null) {
			codeLength += diagnosticExtension.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 100
			reverseOS.write(0x64);
			reverseOS.write(0xBF);
			codeLength += 2;
			return codeLength;
		}
		
		if (unknownDefault != null) {
			codeLength += unknownDefault.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownNames != null) {
			codeLength += unknownNames.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			reverseOS.write(0xA1);
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

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			unknownNames = new UnknownNames();
			codeLength += unknownNames.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			unknownDefault = new AdditionalText();
			codeLength += unknownDefault.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 100)) {
			diagnosticExtension = new Embedded();
			codeLength += diagnosticExtension.decode(is, false);
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

		if (unknownNames != null) {
			sb.append("unknownNames: ");
			unknownNames.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (unknownDefault != null) {
			sb.append("unknownDefault: ").append(unknownDefault);
			return;
		}

		if (diagnosticExtension != null) {
			sb.append("diagnosticExtension: ");
			diagnosticExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

