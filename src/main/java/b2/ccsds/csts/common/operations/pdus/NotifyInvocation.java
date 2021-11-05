/**
 * This class file was automatically generated by jASN1 v1.11.3-SNAPSHOT (http://www.beanit.com)
 */

package b2.ccsds.csts.common.operations.pdus;

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

import b2.ccsds.csts.common.types.AbstractChoice;
import b2.ccsds.csts.common.types.AdditionalText;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.EventValue;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import b2.ccsds.csts.common.types.FunctionalResourceName;
import b2.ccsds.csts.common.types.IntUnsigned;
import b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b2.ccsds.csts.common.types.ListOfParametersEvents;
import b2.ccsds.csts.common.types.Name;
import b2.ccsds.csts.common.types.ProcedureName;
import b2.ccsds.csts.common.types.PublishedIdentifier;
import b2.ccsds.csts.common.types.QualifiedParameter;
import b2.ccsds.csts.common.types.StandardAcknowledgeHeader;
import b2.ccsds.csts.common.types.StandardInvocationHeader;
import b2.ccsds.csts.common.types.StandardReturnHeader;
import b2.ccsds.csts.common.types.Time;
import b2.ccsds.csts.common.types.TypeAndValue;

public class NotifyInvocation implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private StandardInvocationHeader standardInvocationHeader = null;
	private Time eventTime = null;
	private Name eventName = null;
	private EventValue eventValue = null;
	private Extended notifyInvocationExtension = null;
	
	public NotifyInvocation() {
	}

	public NotifyInvocation(byte[] code) {
		this.code = code;
	}

	public void setStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		this.standardInvocationHeader = standardInvocationHeader;
	}

	public StandardInvocationHeader getStandardInvocationHeader() {
		return standardInvocationHeader;
	}

	public void setEventTime(Time eventTime) {
		this.eventTime = eventTime;
	}

	public Time getEventTime() {
		return eventTime;
	}

	public void setEventName(Name eventName) {
		this.eventName = eventName;
	}

	public Name getEventName() {
		return eventName;
	}

	public void setEventValue(EventValue eventValue) {
		this.eventValue = eventValue;
	}

	public EventValue getEventValue() {
		return eventValue;
	}

	public void setNotifyInvocationExtension(Extended notifyInvocationExtension) {
		this.notifyInvocationExtension = notifyInvocationExtension;
	}

	public Extended getNotifyInvocationExtension() {
		return notifyInvocationExtension;
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
		codeLength += notifyInvocationExtension.encode(reverseOS);
		
		codeLength += eventValue.encode(reverseOS);
		
		codeLength += eventName.encode(reverseOS, true);
		
		codeLength += eventTime.encode(reverseOS);
		
		codeLength += standardInvocationHeader.encode(reverseOS, true);
		
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
		if (berTag.equals(StandardInvocationHeader.tag)) {
			standardInvocationHeader = new StandardInvocationHeader();
			subCodeLength += standardInvocationHeader.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		eventTime = new Time();
		subCodeLength += eventTime.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		if (berTag.equals(Name.tag)) {
			eventName = new Name();
			subCodeLength += eventName.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		eventValue = new EventValue();
		subCodeLength += eventValue.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		notifyInvocationExtension = new Extended();
		subCodeLength += notifyInvocationExtension.decode(is, berTag);
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
		if (standardInvocationHeader != null) {
			sb.append("standardInvocationHeader: ");
			standardInvocationHeader.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("standardInvocationHeader: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (eventTime != null) {
			sb.append("eventTime: ");
			eventTime.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("eventTime: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (eventName != null) {
			sb.append("eventName: ");
			eventName.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("eventName: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (eventValue != null) {
			sb.append("eventValue: ");
			eventValue.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("eventValue: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (notifyInvocationExtension != null) {
			sb.append("notifyInvocationExtension: ");
			notifyInvocationExtension.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("notifyInvocationExtension: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

