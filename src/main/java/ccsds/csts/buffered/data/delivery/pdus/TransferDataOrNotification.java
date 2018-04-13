/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.buffered.data.delivery.pdus;

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

import ccsds.csts.common.operations.pdus.NotifyInvocation;
import ccsds.csts.common.operations.pdus.TransferDataInvocation;
import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.ConditionalTime;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.pdus.CstsFrameworkPdu;

public class TransferDataOrNotification implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private TransferDataInvocation transferDataInvocation = null;
	private NotifyInvocation notifyInvocation = null;
	
	public TransferDataOrNotification() {
	}

	public TransferDataOrNotification(byte[] code) {
		this.code = code;
	}

	public void setTransferDataInvocation(TransferDataInvocation transferDataInvocation) {
		this.transferDataInvocation = transferDataInvocation;
	}

	public TransferDataInvocation getTransferDataInvocation() {
		return transferDataInvocation;
	}

	public void setNotifyInvocation(NotifyInvocation notifyInvocation) {
		this.notifyInvocation = notifyInvocation;
	}

	public NotifyInvocation getNotifyInvocation() {
		return notifyInvocation;
	}

	public int encode(BerByteArrayOutputStream os) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				os.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (notifyInvocation != null) {
			codeLength += notifyInvocation.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			os.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (transferDataInvocation != null) {
			codeLength += transferDataInvocation.encode(os, false);
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
			transferDataInvocation = new TransferDataInvocation();
			codeLength += transferDataInvocation.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			notifyInvocation = new NotifyInvocation();
			codeLength += notifyInvocation.decode(is, false);
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

		if (transferDataInvocation != null) {
			sb.append("transferDataInvocation: ");
			transferDataInvocation.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (notifyInvocation != null) {
			sb.append("notifyInvocation: ");
			notifyInvocation.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

