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


public class DirectiveQualifier implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class DirectiveQualifierValues implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static class SEQUENCE implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public static class ParameterValue implements BerType, Serializable {

				private static final long serialVersionUID = 1L;

				public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

				public byte[] code = null;
				private BerObjectIdentifier paramTypeOid = null;
				private BerOctetString paramValue = null;
				
				public ParameterValue() {
				}

				public ParameterValue(byte[] code) {
					this.code = code;
				}

				public void setParamTypeOid(BerObjectIdentifier paramTypeOid) {
					this.paramTypeOid = paramTypeOid;
				}

				public BerObjectIdentifier getParamTypeOid() {
					return paramTypeOid;
				}

				public void setParamValue(BerOctetString paramValue) {
					this.paramValue = paramValue;
				}

				public BerOctetString getParamValue() {
					return paramValue;
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
					codeLength += paramValue.encode(reverseOS, true);
					
					codeLength += paramTypeOid.encode(reverseOS, true);
					
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
					if (berTag.equals(BerObjectIdentifier.tag)) {
						paramTypeOid = new BerObjectIdentifier();
						subCodeLength += paramTypeOid.decode(is, false);
						subCodeLength += berTag.decode(is);
					}
					else {
						throw new IOException("Tag does not match the mandatory sequence element tag.");
					}
					
					if (berTag.equals(BerOctetString.tag)) {
						paramValue = new BerOctetString();
						subCodeLength += paramValue.decode(is, false);
						if (subCodeLength == totalLength) {
							return codeLength;
						}
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
					if (paramTypeOid != null) {
						sb.append("paramTypeOid: ").append(paramTypeOid);
					}
					else {
						sb.append("paramTypeOid: <empty-required-field>");
					}
					
					sb.append(",\n");
					for (int i = 0; i < indentLevel + 1; i++) {
						sb.append("\t");
					}
					if (paramValue != null) {
						sb.append("paramValue: ").append(paramValue);
					}
					else {
						sb.append("paramValue: <empty-required-field>");
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
			private BerObjectIdentifier paramId = null;
			private ParameterValue parameterValue = null;
			
			public SEQUENCE() {
			}

			public SEQUENCE(byte[] code) {
				this.code = code;
			}

			public void setParamId(BerObjectIdentifier paramId) {
				this.paramId = paramId;
			}

			public BerObjectIdentifier getParamId() {
				return paramId;
			}

			public void setParameterValue(ParameterValue parameterValue) {
				this.parameterValue = parameterValue;
			}

			public ParameterValue getParameterValue() {
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
				codeLength += parameterValue.encode(reverseOS, true);
				
				codeLength += paramId.encode(reverseOS, true);
				
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
				if (berTag.equals(BerObjectIdentifier.tag)) {
					paramId = new BerObjectIdentifier();
					subCodeLength += paramId.decode(is, false);
					subCodeLength += berTag.decode(is);
				}
				else {
					throw new IOException("Tag does not match the mandatory sequence element tag.");
				}
				
				if (berTag.equals(ParameterValue.tag)) {
					parameterValue = new ParameterValue();
					subCodeLength += parameterValue.decode(is, false);
					if (subCodeLength == totalLength) {
						return codeLength;
					}
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
				if (paramId != null) {
					sb.append("paramId: ").append(paramId);
				}
				else {
					sb.append("paramId: <empty-required-field>");
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

		public DirectiveQualifierValues() {
			seqOf = new ArrayList<SEQUENCE>();
		}

		public DirectiveQualifierValues(byte[] code) {
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

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private BerInteger functResourceInstanceNumber = null;
	private DirectiveQualifierValues directiveQualifierValues = null;
	
	public DirectiveQualifier() {
	}

	public DirectiveQualifier(byte[] code) {
		this.code = code;
	}

	public void setFunctResourceInstanceNumber(BerInteger functResourceInstanceNumber) {
		this.functResourceInstanceNumber = functResourceInstanceNumber;
	}

	public BerInteger getFunctResourceInstanceNumber() {
		return functResourceInstanceNumber;
	}

	public void setDirectiveQualifierValues(DirectiveQualifierValues directiveQualifierValues) {
		this.directiveQualifierValues = directiveQualifierValues;
	}

	public DirectiveQualifierValues getDirectiveQualifierValues() {
		return directiveQualifierValues;
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
		codeLength += directiveQualifierValues.encode(reverseOS, true);
		
		codeLength += functResourceInstanceNumber.encode(reverseOS, true);
		
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
		if (berTag.equals(BerInteger.tag)) {
			functResourceInstanceNumber = new BerInteger();
			subCodeLength += functResourceInstanceNumber.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		if (berTag.equals(DirectiveQualifierValues.tag)) {
			directiveQualifierValues = new DirectiveQualifierValues();
			subCodeLength += directiveQualifierValues.decode(is, false);
			if (subCodeLength == totalLength) {
				return codeLength;
			}
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
		if (functResourceInstanceNumber != null) {
			sb.append("functResourceInstanceNumber: ").append(functResourceInstanceNumber);
		}
		else {
			sb.append("functResourceInstanceNumber: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (directiveQualifierValues != null) {
			sb.append("directiveQualifierValues: ");
			directiveQualifierValues.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("directiveQualifierValues: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

