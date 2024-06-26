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

public class DirectiveQualifierValues implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private SequenceOfParameterIdsAndValues sequenceOfParamIdsAndValues = null;
	private TypeAndValueComplexQualified parameterlessValues = null;
	private BerNull noQualifierValues = null;
	
	public DirectiveQualifierValues() {
	}

	public DirectiveQualifierValues(byte[] code) {
		this.code = code;
	}

	public void setSequenceOfParamIdsAndValues(SequenceOfParameterIdsAndValues sequenceOfParamIdsAndValues) {
		this.sequenceOfParamIdsAndValues = sequenceOfParamIdsAndValues;
	}

	public SequenceOfParameterIdsAndValues getSequenceOfParamIdsAndValues() {
		return sequenceOfParamIdsAndValues;
	}

	public void setParameterlessValues(TypeAndValueComplexQualified parameterlessValues) {
		this.parameterlessValues = parameterlessValues;
	}

	public TypeAndValueComplexQualified getParameterlessValues() {
		return parameterlessValues;
	}

	public void setNoQualifierValues(BerNull noQualifierValues) {
		this.noQualifierValues = noQualifierValues;
	}

	public BerNull getNoQualifierValues() {
		return noQualifierValues;
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

		if (noQualifierValues != null) {
			codeLength += noQualifierValues.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (parameterlessValues != null) {
			sublength = parameterlessValues.encode(reverseOS);
			codeLength += sublength;
			codeLength += BerLength.encodeLength(reverseOS, sublength);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 2
			reverseOS.write(0xA2);
			codeLength += 1;
			return codeLength;
		}
		
		if (sequenceOfParamIdsAndValues != null) {
			codeLength += sequenceOfParamIdsAndValues.encode(reverseOS, false);
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
			sequenceOfParamIdsAndValues = new SequenceOfParameterIdsAndValues();
			codeLength += sequenceOfParamIdsAndValues.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 2)) {
			codeLength += BerLength.skip(is);
			parameterlessValues = new TypeAndValueComplexQualified();
			codeLength += parameterlessValues.decode(is, null);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			noQualifierValues = new BerNull();
			codeLength += noQualifierValues.decode(is, false);
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

		if (sequenceOfParamIdsAndValues != null) {
			sb.append("sequenceOfParamIdsAndValues: ");
			sequenceOfParamIdsAndValues.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (parameterlessValues != null) {
			sb.append("parameterlessValues: ");
			parameterlessValues.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (noQualifierValues != null) {
			sb.append("noQualifierValues: ").append(noQualifierValues);
			return;
		}

		sb.append("<none>");
	}

}

