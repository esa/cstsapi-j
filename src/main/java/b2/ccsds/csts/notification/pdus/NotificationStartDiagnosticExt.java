/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.notification.pdus;

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

import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b2.ccsds.csts.common.types.ListOfParametersEvents;
import b2.ccsds.csts.pdus.CstsFrameworkPdu;

public class NotificationStartDiagnosticExt implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private ListOfParamEventsDiagnostics common = null;
	private Embedded notificationStartDiagnosticExtExtension = null;
	
	public NotificationStartDiagnosticExt() {
	}

	public NotificationStartDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setCommon(ListOfParamEventsDiagnostics common) {
		this.common = common;
	}

	public ListOfParamEventsDiagnostics getCommon() {
		return common;
	}

	public void setNotificationStartDiagnosticExtExtension(Embedded notificationStartDiagnosticExtExtension) {
		this.notificationStartDiagnosticExtExtension = notificationStartDiagnosticExtExtension;
	}

	public Embedded getNotificationStartDiagnosticExtExtension() {
		return notificationStartDiagnosticExtExtension;
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

		if (notificationStartDiagnosticExtExtension != null) {
			codeLength += notificationStartDiagnosticExtExtension.encode(reverseOS, false);
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
			notificationStartDiagnosticExtExtension = new Embedded();
			codeLength += notificationStartDiagnosticExtExtension.decode(is, false);
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

		if (notificationStartDiagnosticExtExtension != null) {
			sb.append("notificationStartDiagnosticExtExtension: ");
			notificationStartDiagnosticExtExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

