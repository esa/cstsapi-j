/**
 * This class file was automatically generated by jASN1 v1.11.3-SNAPSHOT (http://www.beanit.com)
 */

package b2.ccsds.csts.buffered.data.delivery.pdus;

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

import b2.ccsds.csts.common.operations.pdus.NotifyInvocation;
import b2.ccsds.csts.common.operations.pdus.TransferDataInvocation;
import b2.ccsds.csts.common.types.AdditionalText;
import b2.ccsds.csts.common.types.ConditionalTime;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.pdus.CstsFrameworkPdu;

public class BuffDataDelStartInvocExt implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private ConditionalTime startGenerationTime = null;
	private ConditionalTime stopGenerationTime = null;
	private Extended buffDataDelStartInvocExtExtension = null;
	
	public BuffDataDelStartInvocExt() {
	}

	public BuffDataDelStartInvocExt(byte[] code) {
		this.code = code;
	}

	public void setStartGenerationTime(ConditionalTime startGenerationTime) {
		this.startGenerationTime = startGenerationTime;
	}

	public ConditionalTime getStartGenerationTime() {
		return startGenerationTime;
	}

	public void setStopGenerationTime(ConditionalTime stopGenerationTime) {
		this.stopGenerationTime = stopGenerationTime;
	}

	public ConditionalTime getStopGenerationTime() {
		return stopGenerationTime;
	}

	public void setBuffDataDelStartInvocExtExtension(Extended buffDataDelStartInvocExtExtension) {
		this.buffDataDelStartInvocExtExtension = buffDataDelStartInvocExtExtension;
	}

	public Extended getBuffDataDelStartInvocExtExtension() {
		return buffDataDelStartInvocExtExtension;
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
		codeLength += buffDataDelStartInvocExtExtension.encode(reverseOS);
		
		codeLength += stopGenerationTime.encode(reverseOS);
		
		codeLength += startGenerationTime.encode(reverseOS);
		
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
		startGenerationTime = new ConditionalTime();
		subCodeLength += startGenerationTime.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		stopGenerationTime = new ConditionalTime();
		subCodeLength += stopGenerationTime.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		buffDataDelStartInvocExtExtension = new Extended();
		subCodeLength += buffDataDelStartInvocExtExtension.decode(is, berTag);
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
		if (startGenerationTime != null) {
			sb.append("startGenerationTime: ");
			startGenerationTime.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("startGenerationTime: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (stopGenerationTime != null) {
			sb.append("stopGenerationTime: ");
			stopGenerationTime.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("stopGenerationTime: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (buffDataDelStartInvocExtExtension != null) {
			sb.append("buffDataDelStartInvocExtExtension: ");
			buffDataDelStartInvocExtExtension.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("buffDataDelStartInvocExtExtension: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

