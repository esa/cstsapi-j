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


public class Ccsds401CarrierRcptTrackingLoopBwdth implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class LoopBwdthChangeDuration implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public byte[] code = null;
		private BerNull auto = null;
		private BerInteger bwdthChangeDuration = null;
		
		public LoopBwdthChangeDuration() {
		}

		public LoopBwdthChangeDuration(byte[] code) {
			this.code = code;
		}

		public void setAuto(BerNull auto) {
			this.auto = auto;
		}

		public BerNull getAuto() {
			return auto;
		}

		public void setBwdthChangeDuration(BerInteger bwdthChangeDuration) {
			this.bwdthChangeDuration = bwdthChangeDuration;
		}

		public BerInteger getBwdthChangeDuration() {
			return bwdthChangeDuration;
		}

		public int encode(OutputStream reverseOS) throws IOException {

			if (code != null) {
				for (int i = code.length - 1; i >= 0; i--) {
					reverseOS.write(code[i]);
				}
				return code.length;
			}

			int codeLength = 0;
			if (bwdthChangeDuration != null) {
				codeLength += bwdthChangeDuration.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, PRIMITIVE, 1
				reverseOS.write(0x81);
				codeLength += 1;
				return codeLength;
			}
			
			if (auto != null) {
				codeLength += auto.encode(reverseOS, false);
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
				auto = new BerNull();
				codeLength += auto.decode(is, false);
				return codeLength;
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
				bwdthChangeDuration = new BerInteger();
				codeLength += bwdthChangeDuration.decode(is, false);
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

			if (auto != null) {
				sb.append("auto: ").append(auto);
				return;
			}

			if (bwdthChangeDuration != null) {
				sb.append("bwdthChangeDuration: ").append(bwdthChangeDuration);
				return;
			}

			sb.append("<none>");
		}

	}

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private BerInteger trackingLoopBwdth = null;
	private LoopBwdthChangeDuration loopBwdthChangeDuration = null;
	
	public Ccsds401CarrierRcptTrackingLoopBwdth() {
	}

	public Ccsds401CarrierRcptTrackingLoopBwdth(byte[] code) {
		this.code = code;
	}

	public void setTrackingLoopBwdth(BerInteger trackingLoopBwdth) {
		this.trackingLoopBwdth = trackingLoopBwdth;
	}

	public BerInteger getTrackingLoopBwdth() {
		return trackingLoopBwdth;
	}

	public void setLoopBwdthChangeDuration(LoopBwdthChangeDuration loopBwdthChangeDuration) {
		this.loopBwdthChangeDuration = loopBwdthChangeDuration;
	}

	public LoopBwdthChangeDuration getLoopBwdthChangeDuration() {
		return loopBwdthChangeDuration;
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
		codeLength += loopBwdthChangeDuration.encode(reverseOS);
		
		codeLength += trackingLoopBwdth.encode(reverseOS, true);
		
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
			trackingLoopBwdth = new BerInteger();
			subCodeLength += trackingLoopBwdth.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		loopBwdthChangeDuration = new LoopBwdthChangeDuration();
		subCodeLength += loopBwdthChangeDuration.decode(is, berTag);
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
		if (trackingLoopBwdth != null) {
			sb.append("trackingLoopBwdth: ").append(trackingLoopBwdth);
		}
		else {
			sb.append("trackingLoopBwdth: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (loopBwdthChangeDuration != null) {
			sb.append("loopBwdthChangeDuration: ");
			loopBwdthChangeDuration.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("loopBwdthChangeDuration: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}
