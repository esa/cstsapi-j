/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.common.operations.pdus;

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

import b1.ccsds.csts.common.types.AbstractChoice;
import b1.ccsds.csts.common.types.AdditionalText;
import b1.ccsds.csts.common.types.DataUnitId;
import b1.ccsds.csts.common.types.Embedded;
import b1.ccsds.csts.common.types.EventValue;
import b1.ccsds.csts.common.types.Extended;
import b1.ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import b1.ccsds.csts.common.types.IntUnsigned;
import b1.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b1.ccsds.csts.common.types.ListOfParametersEvents;
import b1.ccsds.csts.common.types.Name;
import b1.ccsds.csts.common.types.ProcedureInstanceId;
import b1.ccsds.csts.common.types.PublishedIdentifier;
import b1.ccsds.csts.common.types.QualifiedParameter;
import b1.ccsds.csts.common.types.StandardAcknowledgeHeader;
import b1.ccsds.csts.common.types.StandardInvocationHeader;
import b1.ccsds.csts.common.types.StandardReturnHeader;
import b1.ccsds.csts.common.types.Time;
import b1.ccsds.csts.common.types.TypeAndValueComplexQualified;

public class SequenceOfParameterIdsAndValues implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class SEQUENCE implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private PublishedIdentifier parameterIdentifier = null;
		private TypeAndValueComplexQualified parameterValue = null;
		
		public SEQUENCE() {
		}

		public SEQUENCE(byte[] code) {
			this.code = code;
		}

		public void setParameterIdentifier(PublishedIdentifier parameterIdentifier) {
			this.parameterIdentifier = parameterIdentifier;
		}

		public PublishedIdentifier getParameterIdentifier() {
			return parameterIdentifier;
		}

		public void setParameterValue(TypeAndValueComplexQualified parameterValue) {
			this.parameterValue = parameterValue;
		}

		public TypeAndValueComplexQualified getParameterValue() {
			return parameterValue;
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
			codeLength += parameterValue.encode(reverseOS);
			
			codeLength += parameterIdentifier.encode(reverseOS, true);
			
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
			BerTag berTag = new BerTag();

			if (withTag) {
				codeLength += tag.decodeAndCheck(is);
			}

			BerLength length = new BerLength();
			codeLength += length.decode(is);

			int totalLength = length.val;
			codeLength += totalLength;

			subCodeLength += berTag.decode(is);
			if (berTag.equals(PublishedIdentifier.tag)) {
				parameterIdentifier = new PublishedIdentifier();
				subCodeLength += parameterIdentifier.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			parameterValue = new TypeAndValueComplexQualified();
			subCodeLength += parameterValue.decode(is, berTag);
			if (subCodeLength == totalLength) {
				return codeLength;
			}
			throw new IOException("Unexpected end of sequence, length tag: " + totalLength + ", actual sequence length: " + subCodeLength);

			
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

			sb.append("{");
			sb.append("\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (parameterIdentifier != null) {
				sb.append("parameterIdentifier: ").append(parameterIdentifier);
			}
			else {
				sb.append("parameterIdentifier: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (parameterValue != null) {
				sb.append("parameterValue: ");
				parameterValue.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("parameterValue: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);
	public byte[] code = null;
	private List<SEQUENCE> seqOf = null;

	public SequenceOfParameterIdsAndValues() {
		seqOf = new ArrayList<SEQUENCE>();
	}

	public SequenceOfParameterIdsAndValues(byte[] code) {
		this.code = code;
	}

	public List<SEQUENCE> getSEQUENCE() {
		if (seqOf == null) {
			seqOf = new ArrayList<SEQUENCE>();
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
			SEQUENCE element = new SEQUENCE();
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
			Iterator<SEQUENCE> it = seqOf.iterator();
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

