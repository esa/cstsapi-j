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


public class FlfSyncEncCodingSelection implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class EncodingBypass implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private Randomization randomization = null;
		private ConvolutionalEncoding covolutionalEncoding = null;
		
		public EncodingBypass() {
		}

		public EncodingBypass(byte[] code) {
			this.code = code;
		}

		public void setRandomization(Randomization randomization) {
			this.randomization = randomization;
		}

		public Randomization getRandomization() {
			return randomization;
		}

		public void setCovolutionalEncoding(ConvolutionalEncoding covolutionalEncoding) {
			this.covolutionalEncoding = covolutionalEncoding;
		}

		public ConvolutionalEncoding getCovolutionalEncoding() {
			return covolutionalEncoding;
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
			codeLength += covolutionalEncoding.encode(reverseOS);
			
			codeLength += randomization.encode(reverseOS, true);
			
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
			if (berTag.equals(Randomization.tag)) {
				randomization = new Randomization();
				subCodeLength += randomization.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			covolutionalEncoding = new ConvolutionalEncoding();
			subCodeLength += covolutionalEncoding.decode(is, berTag);
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
			if (randomization != null) {
				sb.append("randomization: ").append(randomization);
			}
			else {
				sb.append("randomization: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (covolutionalEncoding != null) {
				sb.append("covolutionalEncoding: ");
				covolutionalEncoding.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("covolutionalEncoding: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class ReedSolomon implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private Randomization randomization = null;
		private ConvolutionalEncoding convolutionalEncoding = null;
		private BerEnum errorCorrectionCapability = null;
		private RsInterleavingDepth interleavingDepth = null;
		
		public ReedSolomon() {
		}

		public ReedSolomon(byte[] code) {
			this.code = code;
		}

		public void setRandomization(Randomization randomization) {
			this.randomization = randomization;
		}

		public Randomization getRandomization() {
			return randomization;
		}

		public void setConvolutionalEncoding(ConvolutionalEncoding convolutionalEncoding) {
			this.convolutionalEncoding = convolutionalEncoding;
		}

		public ConvolutionalEncoding getConvolutionalEncoding() {
			return convolutionalEncoding;
		}

		public void setErrorCorrectionCapability(BerEnum errorCorrectionCapability) {
			this.errorCorrectionCapability = errorCorrectionCapability;
		}

		public BerEnum getErrorCorrectionCapability() {
			return errorCorrectionCapability;
		}

		public void setInterleavingDepth(RsInterleavingDepth interleavingDepth) {
			this.interleavingDepth = interleavingDepth;
		}

		public RsInterleavingDepth getInterleavingDepth() {
			return interleavingDepth;
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
			codeLength += interleavingDepth.encode(reverseOS, true);
			
			codeLength += errorCorrectionCapability.encode(reverseOS, true);
			
			codeLength += convolutionalEncoding.encode(reverseOS);
			
			codeLength += randomization.encode(reverseOS, true);
			
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
			if (berTag.equals(Randomization.tag)) {
				randomization = new Randomization();
				subCodeLength += randomization.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			convolutionalEncoding = new ConvolutionalEncoding();
			subCodeLength += convolutionalEncoding.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			if (berTag.equals(BerEnum.tag)) {
				errorCorrectionCapability = new BerEnum();
				subCodeLength += errorCorrectionCapability.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(RsInterleavingDepth.tag)) {
				interleavingDepth = new RsInterleavingDepth();
				subCodeLength += interleavingDepth.decode(is, false);
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
			if (randomization != null) {
				sb.append("randomization: ").append(randomization);
			}
			else {
				sb.append("randomization: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (convolutionalEncoding != null) {
				sb.append("convolutionalEncoding: ");
				convolutionalEncoding.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("convolutionalEncoding: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (errorCorrectionCapability != null) {
				sb.append("errorCorrectionCapability: ").append(errorCorrectionCapability);
			}
			else {
				sb.append("errorCorrectionCapability: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (interleavingDepth != null) {
				sb.append("interleavingDepth: ").append(interleavingDepth);
			}
			else {
				sb.append("interleavingDepth: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class LdpcFrame implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private Randomization randomization = null;
		private LdpcCodeRateAndInfoBlockLength ldpcCodeRateAndInfoBlockLength = null;
		
		public LdpcFrame() {
		}

		public LdpcFrame(byte[] code) {
			this.code = code;
		}

		public void setRandomization(Randomization randomization) {
			this.randomization = randomization;
		}

		public Randomization getRandomization() {
			return randomization;
		}

		public void setLdpcCodeRateAndInfoBlockLength(LdpcCodeRateAndInfoBlockLength ldpcCodeRateAndInfoBlockLength) {
			this.ldpcCodeRateAndInfoBlockLength = ldpcCodeRateAndInfoBlockLength;
		}

		public LdpcCodeRateAndInfoBlockLength getLdpcCodeRateAndInfoBlockLength() {
			return ldpcCodeRateAndInfoBlockLength;
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
			codeLength += ldpcCodeRateAndInfoBlockLength.encode(reverseOS);
			
			codeLength += randomization.encode(reverseOS, true);
			
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
			if (berTag.equals(Randomization.tag)) {
				randomization = new Randomization();
				subCodeLength += randomization.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			ldpcCodeRateAndInfoBlockLength = new LdpcCodeRateAndInfoBlockLength();
			subCodeLength += ldpcCodeRateAndInfoBlockLength.decode(is, berTag);
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
			if (randomization != null) {
				sb.append("randomization: ").append(randomization);
			}
			else {
				sb.append("randomization: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (ldpcCodeRateAndInfoBlockLength != null) {
				sb.append("ldpcCodeRateAndInfoBlockLength: ");
				ldpcCodeRateAndInfoBlockLength.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("ldpcCodeRateAndInfoBlockLength: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class LdpcSlice implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private LdpcCodeRateAndInfoBlockLength ldpcCodeRateAndInfoBlockLength = null;
		private BerInteger ldpcCodeblockSize = null;
		
		public LdpcSlice() {
		}

		public LdpcSlice(byte[] code) {
			this.code = code;
		}

		public void setLdpcCodeRateAndInfoBlockLength(LdpcCodeRateAndInfoBlockLength ldpcCodeRateAndInfoBlockLength) {
			this.ldpcCodeRateAndInfoBlockLength = ldpcCodeRateAndInfoBlockLength;
		}

		public LdpcCodeRateAndInfoBlockLength getLdpcCodeRateAndInfoBlockLength() {
			return ldpcCodeRateAndInfoBlockLength;
		}

		public void setLdpcCodeblockSize(BerInteger ldpcCodeblockSize) {
			this.ldpcCodeblockSize = ldpcCodeblockSize;
		}

		public BerInteger getLdpcCodeblockSize() {
			return ldpcCodeblockSize;
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
			codeLength += ldpcCodeblockSize.encode(reverseOS, true);
			
			codeLength += ldpcCodeRateAndInfoBlockLength.encode(reverseOS);
			
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
			ldpcCodeRateAndInfoBlockLength = new LdpcCodeRateAndInfoBlockLength();
			subCodeLength += ldpcCodeRateAndInfoBlockLength.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			if (berTag.equals(BerInteger.tag)) {
				ldpcCodeblockSize = new BerInteger();
				subCodeLength += ldpcCodeblockSize.decode(is, false);
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
			if (ldpcCodeRateAndInfoBlockLength != null) {
				sb.append("ldpcCodeRateAndInfoBlockLength: ");
				ldpcCodeRateAndInfoBlockLength.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("ldpcCodeRateAndInfoBlockLength: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (ldpcCodeblockSize != null) {
				sb.append("ldpcCodeblockSize: ").append(ldpcCodeblockSize);
			}
			else {
				sb.append("ldpcCodeblockSize: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	private EncodingBypass encodingBypass = null;
	private ReedSolomon reedSolomon = null;
	private LdpcFrame ldpcFrame = null;
	private LdpcSlice ldpcSlice = null;
	private ConvolutionalEncoding cadu = null;
	
	public FlfSyncEncCodingSelection() {
	}

	public FlfSyncEncCodingSelection(byte[] code) {
		this.code = code;
	}

	public void setEncodingBypass(EncodingBypass encodingBypass) {
		this.encodingBypass = encodingBypass;
	}

	public EncodingBypass getEncodingBypass() {
		return encodingBypass;
	}

	public void setReedSolomon(ReedSolomon reedSolomon) {
		this.reedSolomon = reedSolomon;
	}

	public ReedSolomon getReedSolomon() {
		return reedSolomon;
	}

	public void setLdpcFrame(LdpcFrame ldpcFrame) {
		this.ldpcFrame = ldpcFrame;
	}

	public LdpcFrame getLdpcFrame() {
		return ldpcFrame;
	}

	public void setLdpcSlice(LdpcSlice ldpcSlice) {
		this.ldpcSlice = ldpcSlice;
	}

	public LdpcSlice getLdpcSlice() {
		return ldpcSlice;
	}

	public void setCadu(ConvolutionalEncoding cadu) {
		this.cadu = cadu;
	}

	public ConvolutionalEncoding getCadu() {
		return cadu;
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

		if (cadu != null) {
			sublength = cadu.encode(reverseOS);
			codeLength += sublength;
			codeLength += BerLength.encodeLength(reverseOS, sublength);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 4
			reverseOS.write(0xA4);
			codeLength += 1;
			return codeLength;
		}
		
		if (ldpcSlice != null) {
			codeLength += ldpcSlice.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 3
			reverseOS.write(0xA3);
			codeLength += 1;
			return codeLength;
		}
		
		if (ldpcFrame != null) {
			codeLength += ldpcFrame.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 2
			reverseOS.write(0xA2);
			codeLength += 1;
			return codeLength;
		}
		
		if (reedSolomon != null) {
			codeLength += reedSolomon.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			reverseOS.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (encodingBypass != null) {
			codeLength += encodingBypass.encode(reverseOS, false);
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
			encodingBypass = new EncodingBypass();
			codeLength += encodingBypass.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			reedSolomon = new ReedSolomon();
			codeLength += reedSolomon.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 2)) {
			ldpcFrame = new LdpcFrame();
			codeLength += ldpcFrame.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 3)) {
			ldpcSlice = new LdpcSlice();
			codeLength += ldpcSlice.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 4)) {
			codeLength += BerLength.skip(is);
			cadu = new ConvolutionalEncoding();
			codeLength += cadu.decode(is, null);
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

		if (encodingBypass != null) {
			sb.append("encodingBypass: ");
			encodingBypass.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (reedSolomon != null) {
			sb.append("reedSolomon: ");
			reedSolomon.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (ldpcFrame != null) {
			sb.append("ldpcFrame: ");
			ldpcFrame.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (ldpcSlice != null) {
			sb.append("ldpcSlice: ");
			ldpcSlice.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (cadu != null) {
			sb.append("cadu: ");
			cadu.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}
