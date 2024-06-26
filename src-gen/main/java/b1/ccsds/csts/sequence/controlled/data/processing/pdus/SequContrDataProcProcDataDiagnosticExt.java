/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.sequence.controlled.data.processing.pdus;

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

import b1.ccsds.csts.common.types.ConditionalTime;
import b1.ccsds.csts.common.types.DataUnitId;
import b1.ccsds.csts.common.types.Embedded;
import b1.ccsds.csts.common.types.Extended;
import b1.ccsds.csts.pdus.CstsFrameworkPdu;

public class SequContrDataProcProcDataDiagnosticExt implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private BerNull unableToProcess = null;
	private BerNull serviceInstanceLocked = null;
	private BerNull outOfSequence = null;
	private BerNull inconsistentTimeRange = null;
	private BerNull invalidTime = null;
	private BerNull lateData = null;
	private BerNull dataError = null;
	private BerNull unableToStore = null;
	private Embedded sequContrDataProcProcDataDiagnosticExtExtension = null;
	
	public SequContrDataProcProcDataDiagnosticExt() {
	}

	public SequContrDataProcProcDataDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setUnableToProcess(BerNull unableToProcess) {
		this.unableToProcess = unableToProcess;
	}

	public BerNull getUnableToProcess() {
		return unableToProcess;
	}

	public void setServiceInstanceLocked(BerNull serviceInstanceLocked) {
		this.serviceInstanceLocked = serviceInstanceLocked;
	}

	public BerNull getServiceInstanceLocked() {
		return serviceInstanceLocked;
	}

	public void setOutOfSequence(BerNull outOfSequence) {
		this.outOfSequence = outOfSequence;
	}

	public BerNull getOutOfSequence() {
		return outOfSequence;
	}

	public void setInconsistentTimeRange(BerNull inconsistentTimeRange) {
		this.inconsistentTimeRange = inconsistentTimeRange;
	}

	public BerNull getInconsistentTimeRange() {
		return inconsistentTimeRange;
	}

	public void setInvalidTime(BerNull invalidTime) {
		this.invalidTime = invalidTime;
	}

	public BerNull getInvalidTime() {
		return invalidTime;
	}

	public void setLateData(BerNull lateData) {
		this.lateData = lateData;
	}

	public BerNull getLateData() {
		return lateData;
	}

	public void setDataError(BerNull dataError) {
		this.dataError = dataError;
	}

	public BerNull getDataError() {
		return dataError;
	}

	public void setUnableToStore(BerNull unableToStore) {
		this.unableToStore = unableToStore;
	}

	public BerNull getUnableToStore() {
		return unableToStore;
	}

	public void setSequContrDataProcProcDataDiagnosticExtExtension(Embedded sequContrDataProcProcDataDiagnosticExtExtension) {
		this.sequContrDataProcProcDataDiagnosticExtExtension = sequContrDataProcProcDataDiagnosticExtExtension;
	}

	public Embedded getSequContrDataProcProcDataDiagnosticExtExtension() {
		return sequContrDataProcProcDataDiagnosticExtExtension;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (sequContrDataProcProcDataDiagnosticExtExtension != null) {
			codeLength += sequContrDataProcProcDataDiagnosticExtExtension.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 100
			reverseOS.write(0x64);
			reverseOS.write(0xBF);
			codeLength += 2;
			return codeLength;
		}
		
		if (unableToStore != null) {
			codeLength += unableToStore.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 7
			reverseOS.write(0x87);
			codeLength += 1;
			return codeLength;
		}
		
		if (dataError != null) {
			codeLength += dataError.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 6
			reverseOS.write(0x86);
			codeLength += 1;
			return codeLength;
		}
		
		if (lateData != null) {
			codeLength += lateData.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 5
			reverseOS.write(0x85);
			codeLength += 1;
			return codeLength;
		}
		
		if (invalidTime != null) {
			codeLength += invalidTime.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 4
			reverseOS.write(0x84);
			codeLength += 1;
			return codeLength;
		}
		
		if (inconsistentTimeRange != null) {
			codeLength += inconsistentTimeRange.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (outOfSequence != null) {
			codeLength += outOfSequence.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			reverseOS.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (serviceInstanceLocked != null) {
			codeLength += serviceInstanceLocked.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			reverseOS.write(0x81);
			codeLength += 1;
			return codeLength;
		}
		
		if (unableToProcess != null) {
			codeLength += unableToProcess.encode(reverseOS, false);
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
			unableToProcess = new BerNull();
			codeLength += unableToProcess.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			serviceInstanceLocked = new BerNull();
			codeLength += serviceInstanceLocked.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			outOfSequence = new BerNull();
			codeLength += outOfSequence.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			inconsistentTimeRange = new BerNull();
			codeLength += inconsistentTimeRange.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 4)) {
			invalidTime = new BerNull();
			codeLength += invalidTime.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 5)) {
			lateData = new BerNull();
			codeLength += lateData.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 6)) {
			dataError = new BerNull();
			codeLength += dataError.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 7)) {
			unableToStore = new BerNull();
			codeLength += unableToStore.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 100)) {
			sequContrDataProcProcDataDiagnosticExtExtension = new Embedded();
			codeLength += sequContrDataProcProcDataDiagnosticExtExtension.decode(is, false);
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

		if (unableToProcess != null) {
			sb.append("unableToProcess: ").append(unableToProcess);
			return;
		}

		if (serviceInstanceLocked != null) {
			sb.append("serviceInstanceLocked: ").append(serviceInstanceLocked);
			return;
		}

		if (outOfSequence != null) {
			sb.append("outOfSequence: ").append(outOfSequence);
			return;
		}

		if (inconsistentTimeRange != null) {
			sb.append("inconsistentTimeRange: ").append(inconsistentTimeRange);
			return;
		}

		if (invalidTime != null) {
			sb.append("invalidTime: ").append(invalidTime);
			return;
		}

		if (lateData != null) {
			sb.append("lateData: ").append(lateData);
			return;
		}

		if (dataError != null) {
			sb.append("dataError: ").append(dataError);
			return;
		}

		if (unableToStore != null) {
			sb.append("unableToStore: ").append(unableToStore);
			return;
		}

		if (sequContrDataProcProcDataDiagnosticExtExtension != null) {
			sb.append("sequContrDataProcProcDataDiagnosticExtExtension: ");
			sequContrDataProcProcDataDiagnosticExtExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

