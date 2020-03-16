/**
 * This class file was automatically generated by jASN1 v1.11.2 (http://www.beanit.com)
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

public class GetDiagnosticExt implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private ListOfParamEventsDiagnostics common = null;
	private Embedded getDiagnosticExtExtension = null;
	
	public GetDiagnosticExt() {
	}

	public GetDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setCommon(ListOfParamEventsDiagnostics common) {
		this.common = common;
	}

	public ListOfParamEventsDiagnostics getCommon() {
		return common;
	}

	public void setGetDiagnosticExtExtension(Embedded getDiagnosticExtExtension) {
		this.getDiagnosticExtExtension = getDiagnosticExtExtension;
	}

	public Embedded getGetDiagnosticExtExtension() {
		return getDiagnosticExtExtension;
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

		if (getDiagnosticExtExtension != null) {
			codeLength += getDiagnosticExtExtension.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 100
			reverseOS.write(0x64);
			reverseOS.write(0xBF);
			codeLength += 2;
			return codeLength;
		}
		
		if (common != null) {
			sublength = common.encode(reverseOS);
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
			common = new ListOfParamEventsDiagnostics();
			codeLength += common.decode(is, null);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 100)) {
			getDiagnosticExtExtension = new Embedded();
			codeLength += getDiagnosticExtExtension.decode(is, false);
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

		if (common != null) {
			sb.append("common: ");
			common.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (getDiagnosticExtExtension != null) {
			sb.append("getDiagnosticExtExtension: ");
			getDiagnosticExtExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}
