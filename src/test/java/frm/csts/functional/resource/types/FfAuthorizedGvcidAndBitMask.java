/**
 * This class file was automatically generated by jASN1 v1.11.3-SNAPSHOT (http://www.beanit.com)
 */

package frm.csts.functional.resource.types;

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


public class FfAuthorizedGvcidAndBitMask implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class TcGvcid implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerInteger tcScid = null;
		private BerInteger tcVcid = null;
		
		public TcGvcid() {
		}

		public TcGvcid(byte[] code) {
			this.code = code;
		}

		public void setTcScid(BerInteger tcScid) {
			this.tcScid = tcScid;
		}

		public BerInteger getTcScid() {
			return tcScid;
		}

		public void setTcVcid(BerInteger tcVcid) {
			this.tcVcid = tcVcid;
		}

		public BerInteger getTcVcid() {
			return tcVcid;
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
			codeLength += tcVcid.encode(reverseOS, true);
			
			codeLength += tcScid.encode(reverseOS, true);
			
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
			if (berTag.equals(BerInteger.tag)) {
				tcScid = new BerInteger();
				subCodeLength += tcScid.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				tcVcid = new BerInteger();
				subCodeLength += tcVcid.decode(is, false);
				if (subCodeLength == totalLength) {
					return codeLength;
				}
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
			if (tcScid != null) {
				sb.append("tcScid: ").append(tcScid);
			}
			else {
				sb.append("tcScid: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (tcVcid != null) {
				sb.append("tcVcid: ").append(tcVcid);
			}
			else {
				sb.append("tcVcid: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class AosGvcid implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerInteger aosScid = null;
		private BerInteger aosVcid = null;
		
		public AosGvcid() {
		}

		public AosGvcid(byte[] code) {
			this.code = code;
		}

		public void setAosScid(BerInteger aosScid) {
			this.aosScid = aosScid;
		}

		public BerInteger getAosScid() {
			return aosScid;
		}

		public void setAosVcid(BerInteger aosVcid) {
			this.aosVcid = aosVcid;
		}

		public BerInteger getAosVcid() {
			return aosVcid;
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
			codeLength += aosVcid.encode(reverseOS, true);
			
			codeLength += aosScid.encode(reverseOS, true);
			
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
			if (berTag.equals(BerInteger.tag)) {
				aosScid = new BerInteger();
				subCodeLength += aosScid.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				aosVcid = new BerInteger();
				subCodeLength += aosVcid.decode(is, false);
				if (subCodeLength == totalLength) {
					return codeLength;
				}
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
			if (aosScid != null) {
				sb.append("aosScid: ").append(aosScid);
			}
			else {
				sb.append("aosScid: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (aosVcid != null) {
				sb.append("aosVcid: ").append(aosVcid);
			}
			else {
				sb.append("aosVcid: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class UslpGvcid implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerInteger uslpScid = null;
		private BerInteger uslpVcid = null;
		
		public UslpGvcid() {
		}

		public UslpGvcid(byte[] code) {
			this.code = code;
		}

		public void setUslpScid(BerInteger uslpScid) {
			this.uslpScid = uslpScid;
		}

		public BerInteger getUslpScid() {
			return uslpScid;
		}

		public void setUslpVcid(BerInteger uslpVcid) {
			this.uslpVcid = uslpVcid;
		}

		public BerInteger getUslpVcid() {
			return uslpVcid;
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
			codeLength += uslpVcid.encode(reverseOS, true);
			
			codeLength += uslpScid.encode(reverseOS, true);
			
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
			if (berTag.equals(BerInteger.tag)) {
				uslpScid = new BerInteger();
				subCodeLength += uslpScid.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				uslpVcid = new BerInteger();
				subCodeLength += uslpVcid.decode(is, false);
				if (subCodeLength == totalLength) {
					return codeLength;
				}
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
			if (uslpScid != null) {
				sb.append("uslpScid: ").append(uslpScid);
			}
			else {
				sb.append("uslpScid: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (uslpVcid != null) {
				sb.append("uslpVcid: ").append(uslpVcid);
			}
			else {
				sb.append("uslpVcid: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class Other implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerOctetString otherBitMask = null;
		private BerOctetString otherAuthorizedValue = null;
		
		public Other() {
		}

		public Other(byte[] code) {
			this.code = code;
		}

		public void setOtherBitMask(BerOctetString otherBitMask) {
			this.otherBitMask = otherBitMask;
		}

		public BerOctetString getOtherBitMask() {
			return otherBitMask;
		}

		public void setOtherAuthorizedValue(BerOctetString otherAuthorizedValue) {
			this.otherAuthorizedValue = otherAuthorizedValue;
		}

		public BerOctetString getOtherAuthorizedValue() {
			return otherAuthorizedValue;
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
			codeLength += otherAuthorizedValue.encode(reverseOS, true);
			
			codeLength += otherBitMask.encode(reverseOS, true);
			
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
			if (berTag.equals(BerOctetString.tag)) {
				otherBitMask = new BerOctetString();
				subCodeLength += otherBitMask.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerOctetString.tag)) {
				otherAuthorizedValue = new BerOctetString();
				subCodeLength += otherAuthorizedValue.decode(is, false);
				if (subCodeLength == totalLength) {
					return codeLength;
				}
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
			if (otherBitMask != null) {
				sb.append("otherBitMask: ").append(otherBitMask);
			}
			else {
				sb.append("otherBitMask: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (otherAuthorizedValue != null) {
				sb.append("otherAuthorizedValue: ").append(otherAuthorizedValue);
			}
			else {
				sb.append("otherAuthorizedValue: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	private TcGvcid tcGvcid = null;
	private AosGvcid aosGvcid = null;
	private UslpGvcid uslpGvcid = null;
	private BerNull cadu = null;
	private Other other = null;
	
	public FfAuthorizedGvcidAndBitMask() {
	}

	public FfAuthorizedGvcidAndBitMask(byte[] code) {
		this.code = code;
	}

	public void setTcGvcid(TcGvcid tcGvcid) {
		this.tcGvcid = tcGvcid;
	}

	public TcGvcid getTcGvcid() {
		return tcGvcid;
	}

	public void setAosGvcid(AosGvcid aosGvcid) {
		this.aosGvcid = aosGvcid;
	}

	public AosGvcid getAosGvcid() {
		return aosGvcid;
	}

	public void setUslpGvcid(UslpGvcid uslpGvcid) {
		this.uslpGvcid = uslpGvcid;
	}

	public UslpGvcid getUslpGvcid() {
		return uslpGvcid;
	}

	public void setCadu(BerNull cadu) {
		this.cadu = cadu;
	}

	public BerNull getCadu() {
		return cadu;
	}

	public void setOther(Other other) {
		this.other = other;
	}

	public Other getOther() {
		return other;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (other != null) {
			codeLength += other.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 4
			reverseOS.write(0xA4);
			codeLength += 1;
			return codeLength;
		}
		
		if (cadu != null) {
			codeLength += cadu.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			reverseOS.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (uslpGvcid != null) {
			codeLength += uslpGvcid.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 2
			reverseOS.write(0xA2);
			codeLength += 1;
			return codeLength;
		}
		
		if (aosGvcid != null) {
			codeLength += aosGvcid.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			reverseOS.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (tcGvcid != null) {
			codeLength += tcGvcid.encode(reverseOS, false);
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
			tcGvcid = new TcGvcid();
			codeLength += tcGvcid.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			aosGvcid = new AosGvcid();
			codeLength += aosGvcid.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 2)) {
			uslpGvcid = new UslpGvcid();
			codeLength += uslpGvcid.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			cadu = new BerNull();
			codeLength += cadu.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 4)) {
			other = new Other();
			codeLength += other.decode(is, false);
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

		if (tcGvcid != null) {
			sb.append("tcGvcid: ");
			tcGvcid.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (aosGvcid != null) {
			sb.append("aosGvcid: ");
			aosGvcid.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (uslpGvcid != null) {
			sb.append("uslpGvcid: ");
			uslpGvcid.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (cadu != null) {
			sb.append("cadu: ").append(cadu);
			return;
		}

		if (other != null) {
			sb.append("other: ");
			other.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

