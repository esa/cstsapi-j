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


public class FlfSyncAsmConfig implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class AsmPattern implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public byte[] code = null;
		private BerNull ccsdsPattern = null;
		private BerOctetString nonCcsdsPattern = null;
		
		public AsmPattern() {
		}

		public AsmPattern(byte[] code) {
			this.code = code;
		}

		public void setCcsdsPattern(BerNull ccsdsPattern) {
			this.ccsdsPattern = ccsdsPattern;
		}

		public BerNull getCcsdsPattern() {
			return ccsdsPattern;
		}

		public void setNonCcsdsPattern(BerOctetString nonCcsdsPattern) {
			this.nonCcsdsPattern = nonCcsdsPattern;
		}

		public BerOctetString getNonCcsdsPattern() {
			return nonCcsdsPattern;
		}

		public int encode(OutputStream reverseOS) throws IOException {

			if (code != null) {
				for (int i = code.length - 1; i >= 0; i--) {
					reverseOS.write(code[i]);
				}
				return code.length;
			}

			int codeLength = 0;
			if (nonCcsdsPattern != null) {
				codeLength += nonCcsdsPattern.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, PRIMITIVE, 1
				reverseOS.write(0x81);
				codeLength += 1;
				return codeLength;
			}
			
			if (ccsdsPattern != null) {
				codeLength += ccsdsPattern.encode(reverseOS, false);
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
				ccsdsPattern = new BerNull();
				codeLength += ccsdsPattern.decode(is, false);
				return codeLength;
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
				nonCcsdsPattern = new BerOctetString();
				codeLength += nonCcsdsPattern.decode(is, false);
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

			if (ccsdsPattern != null) {
				sb.append("ccsdsPattern: ").append(ccsdsPattern);
				return;
			}

			if (nonCcsdsPattern != null) {
				sb.append("nonCcsdsPattern: ").append(nonCcsdsPattern);
				return;
			}

			sb.append("<none>");
		}

	}

	public static class AsmThresholds implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerInteger asmCorrelationLockThreshold = null;
		private BerInteger asmCorrelationOutOfLockThreshold = null;
		private BerInteger verifyThreshold = null;
		private BerInteger lockedThreshold = null;
		private BerInteger notLockedThreshold = null;
		private BerInteger frameLengthErrorThreshold = null;
		
		public AsmThresholds() {
		}

		public AsmThresholds(byte[] code) {
			this.code = code;
		}

		public void setAsmCorrelationLockThreshold(BerInteger asmCorrelationLockThreshold) {
			this.asmCorrelationLockThreshold = asmCorrelationLockThreshold;
		}

		public BerInteger getAsmCorrelationLockThreshold() {
			return asmCorrelationLockThreshold;
		}

		public void setAsmCorrelationOutOfLockThreshold(BerInteger asmCorrelationOutOfLockThreshold) {
			this.asmCorrelationOutOfLockThreshold = asmCorrelationOutOfLockThreshold;
		}

		public BerInteger getAsmCorrelationOutOfLockThreshold() {
			return asmCorrelationOutOfLockThreshold;
		}

		public void setVerifyThreshold(BerInteger verifyThreshold) {
			this.verifyThreshold = verifyThreshold;
		}

		public BerInteger getVerifyThreshold() {
			return verifyThreshold;
		}

		public void setLockedThreshold(BerInteger lockedThreshold) {
			this.lockedThreshold = lockedThreshold;
		}

		public BerInteger getLockedThreshold() {
			return lockedThreshold;
		}

		public void setNotLockedThreshold(BerInteger notLockedThreshold) {
			this.notLockedThreshold = notLockedThreshold;
		}

		public BerInteger getNotLockedThreshold() {
			return notLockedThreshold;
		}

		public void setFrameLengthErrorThreshold(BerInteger frameLengthErrorThreshold) {
			this.frameLengthErrorThreshold = frameLengthErrorThreshold;
		}

		public BerInteger getFrameLengthErrorThreshold() {
			return frameLengthErrorThreshold;
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
			codeLength += frameLengthErrorThreshold.encode(reverseOS, true);
			
			codeLength += notLockedThreshold.encode(reverseOS, true);
			
			codeLength += lockedThreshold.encode(reverseOS, true);
			
			codeLength += verifyThreshold.encode(reverseOS, true);
			
			codeLength += asmCorrelationOutOfLockThreshold.encode(reverseOS, true);
			
			codeLength += asmCorrelationLockThreshold.encode(reverseOS, true);
			
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
				asmCorrelationLockThreshold = new BerInteger();
				subCodeLength += asmCorrelationLockThreshold.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				asmCorrelationOutOfLockThreshold = new BerInteger();
				subCodeLength += asmCorrelationOutOfLockThreshold.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				verifyThreshold = new BerInteger();
				subCodeLength += verifyThreshold.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				lockedThreshold = new BerInteger();
				subCodeLength += lockedThreshold.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				notLockedThreshold = new BerInteger();
				subCodeLength += notLockedThreshold.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				frameLengthErrorThreshold = new BerInteger();
				subCodeLength += frameLengthErrorThreshold.decode(is, false);
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
			if (asmCorrelationLockThreshold != null) {
				sb.append("asmCorrelationLockThreshold: ").append(asmCorrelationLockThreshold);
			}
			else {
				sb.append("asmCorrelationLockThreshold: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (asmCorrelationOutOfLockThreshold != null) {
				sb.append("asmCorrelationOutOfLockThreshold: ").append(asmCorrelationOutOfLockThreshold);
			}
			else {
				sb.append("asmCorrelationOutOfLockThreshold: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (verifyThreshold != null) {
				sb.append("verifyThreshold: ").append(verifyThreshold);
			}
			else {
				sb.append("verifyThreshold: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (lockedThreshold != null) {
				sb.append("lockedThreshold: ").append(lockedThreshold);
			}
			else {
				sb.append("lockedThreshold: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (notLockedThreshold != null) {
				sb.append("notLockedThreshold: ").append(notLockedThreshold);
			}
			else {
				sb.append("notLockedThreshold: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (frameLengthErrorThreshold != null) {
				sb.append("frameLengthErrorThreshold: ").append(frameLengthErrorThreshold);
			}
			else {
				sb.append("frameLengthErrorThreshold: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private AsmPattern asmPattern = null;
	private AsmThresholds asmThresholds = null;
	
	public FlfSyncAsmConfig() {
	}

	public FlfSyncAsmConfig(byte[] code) {
		this.code = code;
	}

	public void setAsmPattern(AsmPattern asmPattern) {
		this.asmPattern = asmPattern;
	}

	public AsmPattern getAsmPattern() {
		return asmPattern;
	}

	public void setAsmThresholds(AsmThresholds asmThresholds) {
		this.asmThresholds = asmThresholds;
	}

	public AsmThresholds getAsmThresholds() {
		return asmThresholds;
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
		codeLength += asmThresholds.encode(reverseOS, true);
		
		codeLength += asmPattern.encode(reverseOS);
		
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
		asmPattern = new AsmPattern();
		subCodeLength += asmPattern.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		if (berTag.equals(AsmThresholds.tag)) {
			asmThresholds = new AsmThresholds();
			subCodeLength += asmThresholds.decode(is, false);
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
		if (asmPattern != null) {
			sb.append("asmPattern: ");
			asmPattern.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("asmPattern: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (asmThresholds != null) {
			sb.append("asmThresholds: ");
			asmThresholds.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("asmThresholds: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

