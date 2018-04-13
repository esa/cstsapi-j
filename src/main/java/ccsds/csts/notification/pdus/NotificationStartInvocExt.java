/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.notification.pdus;

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

import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import ccsds.csts.common.types.ListOfParametersEvents;
import ccsds.csts.pdus.CstsFrameworkPdu;

public class NotificationStartInvocExt implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private ListOfParametersEvents listOfEvents = null;
	private Extended notificationStartInvocExtExtension = null;
	
	public NotificationStartInvocExt() {
	}

	public NotificationStartInvocExt(byte[] code) {
		this.code = code;
	}

	public void setListOfEvents(ListOfParametersEvents listOfEvents) {
		this.listOfEvents = listOfEvents;
	}

	public ListOfParametersEvents getListOfEvents() {
		return listOfEvents;
	}

	public void setNotificationStartInvocExtExtension(Extended notificationStartInvocExtExtension) {
		this.notificationStartInvocExtExtension = notificationStartInvocExtExtension;
	}

	public Extended getNotificationStartInvocExtExtension() {
		return notificationStartInvocExtExtension;
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
		codeLength += notificationStartInvocExtExtension.encode(os);
		
		codeLength += listOfEvents.encode(os);
		
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
		BerTag berTag = new BerTag();

		if (withTag) {
			codeLength += tag.decodeAndCheck(is);
		}

		BerLength length = new BerLength();
		codeLength += length.decode(is);

		int totalLength = length.val;
		codeLength += totalLength;

		subCodeLength += berTag.decode(is);
		listOfEvents = new ListOfParametersEvents();
		subCodeLength += listOfEvents.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		notificationStartInvocExtExtension = new Extended();
		subCodeLength += notificationStartInvocExtExtension.decode(is, berTag);
		if (subCodeLength == totalLength) {
			return codeLength;
		}
		throw new IOException("Unexpected end of sequence, length tag: " + totalLength + ", actual sequence length: " + subCodeLength);

		
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

		sb.append("{");
		sb.append("\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (listOfEvents != null) {
			sb.append("listOfEvents: ");
			listOfEvents.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("listOfEvents: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (notificationStartInvocExtExtension != null) {
			sb.append("notificationStartInvocExtExtension: ");
			notificationStartInvocExtExtension.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("notificationStartInvocExtExtension: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

