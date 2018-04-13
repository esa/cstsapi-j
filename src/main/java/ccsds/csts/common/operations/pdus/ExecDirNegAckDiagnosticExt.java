/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.common.operations.pdus;

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

import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.EventValue;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import ccsds.csts.common.types.IntUnsigned;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParametersEvents;
import ccsds.csts.common.types.Name;
import ccsds.csts.common.types.ProcedureInstanceId;
import ccsds.csts.common.types.PublishedIdentifier;
import ccsds.csts.common.types.QualifiedParameter;
import ccsds.csts.common.types.StandardAcknowledgeHeader;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.common.types.StandardReturnHeader;
import ccsds.csts.common.types.Time;
import ccsds.csts.common.types.TypeAndValueComplexQualified;

public class ExecDirNegAckDiagnosticExt implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class InvalidFunctionalResourceParameter implements Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 17);
		public byte[] code = null;
		private List<Name> seqOf = null;

		public InvalidFunctionalResourceParameter() {
			seqOf = new ArrayList<Name>();
		}

		public InvalidFunctionalResourceParameter(byte[] code) {
			this.code = code;
		}

		public List<Name> getName() {
			if (seqOf == null) {
				seqOf = new ArrayList<Name>();
			}
			return seqOf;
		}

		public int encode(BerByteArrayOutputStream os) throws IOException {
			return encode(os, true);
		}

		public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

			if (code != null) {
				for (int i = code.length - 1; i >= 0; i--) {
					os.write(code[i]);
				}
				if (withTag) {
					return tag.encode(os) + code.length;
				}
				return code.length;
			}

			int codeLength = 0;
			for (int i = (seqOf.size() - 1); i >= 0; i--) {
				codeLength += seqOf.get(i).encode(os, true);
			}

			codeLength += BerLength.encodeLength(os, codeLength);

			if (withTag) {
				codeLength += tag.encode(os);
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
				Name element = new Name();
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
			BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
			encode(os, false);
			code = os.getArray();
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
				Iterator<Name> it = seqOf.iterator();
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

	public static class InvalidProcedureParameter implements Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 17);
		public byte[] code = null;
		private List<Name> seqOf = null;

		public InvalidProcedureParameter() {
			seqOf = new ArrayList<Name>();
		}

		public InvalidProcedureParameter(byte[] code) {
			this.code = code;
		}

		public List<Name> getName() {
			if (seqOf == null) {
				seqOf = new ArrayList<Name>();
			}
			return seqOf;
		}

		public int encode(BerByteArrayOutputStream os) throws IOException {
			return encode(os, true);
		}

		public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

			if (code != null) {
				for (int i = code.length - 1; i >= 0; i--) {
					os.write(code[i]);
				}
				if (withTag) {
					return tag.encode(os) + code.length;
				}
				return code.length;
			}

			int codeLength = 0;
			for (int i = (seqOf.size() - 1); i >= 0; i--) {
				codeLength += seqOf.get(i).encode(os, true);
			}

			codeLength += BerLength.encodeLength(os, codeLength);

			if (withTag) {
				codeLength += tag.encode(os);
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
				Name element = new Name();
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
			BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
			encode(os, false);
			code = os.getArray();
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
				Iterator<Name> it = seqOf.iterator();
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

	public static class ParameterValueOutOfRange implements Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 17);
		public byte[] code = null;
		private List<Name> seqOf = null;

		public ParameterValueOutOfRange() {
			seqOf = new ArrayList<Name>();
		}

		public ParameterValueOutOfRange(byte[] code) {
			this.code = code;
		}

		public List<Name> getName() {
			if (seqOf == null) {
				seqOf = new ArrayList<Name>();
			}
			return seqOf;
		}

		public int encode(BerByteArrayOutputStream os) throws IOException {
			return encode(os, true);
		}

		public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

			if (code != null) {
				for (int i = code.length - 1; i >= 0; i--) {
					os.write(code[i]);
				}
				if (withTag) {
					return tag.encode(os) + code.length;
				}
				return code.length;
			}

			int codeLength = 0;
			for (int i = (seqOf.size() - 1); i >= 0; i--) {
				codeLength += seqOf.get(i).encode(os, true);
			}

			codeLength += BerLength.encodeLength(os, codeLength);

			if (withTag) {
				codeLength += tag.encode(os);
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
				Name element = new Name();
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
			BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
			encode(os, false);
			code = os.getArray();
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
				Iterator<Name> it = seqOf.iterator();
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

	private BerNull unknownDirective = null;
	private BerNull unknownQualifier = null;
	private BerNull invalidProcedureInstance = null;
	private BerNull invalidFunctionalResourceInstance = null;
	private InvalidFunctionalResourceParameter invalidFunctionalResourceParameter = null;
	private InvalidProcedureParameter invalidProcedureParameter = null;
	private ParameterValueOutOfRange parameterValueOutOfRange = null;
	private Embedded execDirNegAckDiagnosticExtExtension = null;
	
	public ExecDirNegAckDiagnosticExt() {
	}

	public ExecDirNegAckDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setUnknownDirective(BerNull unknownDirective) {
		this.unknownDirective = unknownDirective;
	}

	public BerNull getUnknownDirective() {
		return unknownDirective;
	}

	public void setUnknownQualifier(BerNull unknownQualifier) {
		this.unknownQualifier = unknownQualifier;
	}

	public BerNull getUnknownQualifier() {
		return unknownQualifier;
	}

	public void setInvalidProcedureInstance(BerNull invalidProcedureInstance) {
		this.invalidProcedureInstance = invalidProcedureInstance;
	}

	public BerNull getInvalidProcedureInstance() {
		return invalidProcedureInstance;
	}

	public void setInvalidFunctionalResourceInstance(BerNull invalidFunctionalResourceInstance) {
		this.invalidFunctionalResourceInstance = invalidFunctionalResourceInstance;
	}

	public BerNull getInvalidFunctionalResourceInstance() {
		return invalidFunctionalResourceInstance;
	}

	public void setInvalidFunctionalResourceParameter(InvalidFunctionalResourceParameter invalidFunctionalResourceParameter) {
		this.invalidFunctionalResourceParameter = invalidFunctionalResourceParameter;
	}

	public InvalidFunctionalResourceParameter getInvalidFunctionalResourceParameter() {
		return invalidFunctionalResourceParameter;
	}

	public void setInvalidProcedureParameter(InvalidProcedureParameter invalidProcedureParameter) {
		this.invalidProcedureParameter = invalidProcedureParameter;
	}

	public InvalidProcedureParameter getInvalidProcedureParameter() {
		return invalidProcedureParameter;
	}

	public void setParameterValueOutOfRange(ParameterValueOutOfRange parameterValueOutOfRange) {
		this.parameterValueOutOfRange = parameterValueOutOfRange;
	}

	public ParameterValueOutOfRange getParameterValueOutOfRange() {
		return parameterValueOutOfRange;
	}

	public void setExecDirNegAckDiagnosticExtExtension(Embedded execDirNegAckDiagnosticExtExtension) {
		this.execDirNegAckDiagnosticExtExtension = execDirNegAckDiagnosticExtExtension;
	}

	public Embedded getExecDirNegAckDiagnosticExtExtension() {
		return execDirNegAckDiagnosticExtExtension;
	}

	public int encode(BerByteArrayOutputStream os) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				os.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (execDirNegAckDiagnosticExtExtension != null) {
			codeLength += execDirNegAckDiagnosticExtExtension.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 100
			os.write(0x64);
			os.write(0xBF);
			codeLength += 2;
			return codeLength;
		}
		
		if (parameterValueOutOfRange != null) {
			codeLength += parameterValueOutOfRange.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 7
			os.write(0xA7);
			codeLength += 1;
			return codeLength;
		}
		
		if (invalidProcedureParameter != null) {
			codeLength += invalidProcedureParameter.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 6
			os.write(0xA6);
			codeLength += 1;
			return codeLength;
		}
		
		if (invalidFunctionalResourceParameter != null) {
			codeLength += invalidFunctionalResourceParameter.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 5
			os.write(0xA5);
			codeLength += 1;
			return codeLength;
		}
		
		if (invalidFunctionalResourceInstance != null) {
			codeLength += invalidFunctionalResourceInstance.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 4
			os.write(0x84);
			codeLength += 1;
			return codeLength;
		}
		
		if (invalidProcedureInstance != null) {
			codeLength += invalidProcedureInstance.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			os.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownQualifier != null) {
			codeLength += unknownQualifier.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			os.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownDirective != null) {
			codeLength += unknownDirective.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			os.write(0x81);
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

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			unknownDirective = new BerNull();
			codeLength += unknownDirective.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			unknownQualifier = new BerNull();
			codeLength += unknownQualifier.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			invalidProcedureInstance = new BerNull();
			codeLength += invalidProcedureInstance.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 4)) {
			invalidFunctionalResourceInstance = new BerNull();
			codeLength += invalidFunctionalResourceInstance.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 5)) {
			invalidFunctionalResourceParameter = new InvalidFunctionalResourceParameter();
			codeLength += invalidFunctionalResourceParameter.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 6)) {
			invalidProcedureParameter = new InvalidProcedureParameter();
			codeLength += invalidProcedureParameter.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 7)) {
			parameterValueOutOfRange = new ParameterValueOutOfRange();
			codeLength += parameterValueOutOfRange.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 100)) {
			execDirNegAckDiagnosticExtExtension = new Embedded();
			codeLength += execDirNegAckDiagnosticExtExtension.decode(is, false);
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

		if (unknownDirective != null) {
			sb.append("unknownDirective: ").append(unknownDirective);
			return;
		}

		if (unknownQualifier != null) {
			sb.append("unknownQualifier: ").append(unknownQualifier);
			return;
		}

		if (invalidProcedureInstance != null) {
			sb.append("invalidProcedureInstance: ").append(invalidProcedureInstance);
			return;
		}

		if (invalidFunctionalResourceInstance != null) {
			sb.append("invalidFunctionalResourceInstance: ").append(invalidFunctionalResourceInstance);
			return;
		}

		if (invalidFunctionalResourceParameter != null) {
			sb.append("invalidFunctionalResourceParameter: ");
			invalidFunctionalResourceParameter.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (invalidProcedureParameter != null) {
			sb.append("invalidProcedureParameter: ");
			invalidProcedureParameter.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (parameterValueOutOfRange != null) {
			sb.append("parameterValueOutOfRange: ");
			parameterValueOutOfRange.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (execDirNegAckDiagnosticExtExtension != null) {
			sb.append("execDirNegAckDiagnosticExtExtension: ");
			execDirNegAckDiagnosticExtExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

