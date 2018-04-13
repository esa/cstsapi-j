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

public class ExecDirNegReturnDiagnosticExt implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private ActionNotCompletedDiag actionNotCompleted = null;
	private Embedded execDirNegReturnDiagnosticExtExtension = null;
	
	public ExecDirNegReturnDiagnosticExt() {
	}

	public ExecDirNegReturnDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setActionNotCompleted(ActionNotCompletedDiag actionNotCompleted) {
		this.actionNotCompleted = actionNotCompleted;
	}

	public ActionNotCompletedDiag getActionNotCompleted() {
		return actionNotCompleted;
	}

	public void setExecDirNegReturnDiagnosticExtExtension(Embedded execDirNegReturnDiagnosticExtExtension) {
		this.execDirNegReturnDiagnosticExtExtension = execDirNegReturnDiagnosticExtExtension;
	}

	public Embedded getExecDirNegReturnDiagnosticExtExtension() {
		return execDirNegReturnDiagnosticExtExtension;
	}

	public int encode(BerByteArrayOutputStream os) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				os.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		int sublength;

		if (execDirNegReturnDiagnosticExtExtension != null) {
			codeLength += execDirNegReturnDiagnosticExtExtension.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 100
			os.write(0x64);
			os.write(0xBF);
			codeLength += 2;
			return codeLength;
		}
		
		if (actionNotCompleted != null) {
			sublength = actionNotCompleted.encode(os);
			codeLength += sublength;
			codeLength += BerLength.encodeLength(os, sublength);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 0
			os.write(0xA0);
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
			actionNotCompleted = new ActionNotCompletedDiag();
			codeLength += actionNotCompleted.decode(is, null);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 100)) {
			execDirNegReturnDiagnosticExtExtension = new Embedded();
			codeLength += execDirNegReturnDiagnosticExtExtension.decode(is, false);
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

		if (actionNotCompleted != null) {
			sb.append("actionNotCompleted: ");
			actionNotCompleted.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (execDirNegReturnDiagnosticExtExtension != null) {
			sb.append("execDirNegReturnDiagnosticExtExtension: ");
			execDirNegReturnDiagnosticExtExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

