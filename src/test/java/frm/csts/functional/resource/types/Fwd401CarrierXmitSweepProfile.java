/**
 * This class file was automatically generated by jASN1 v1.11.2 (http://www.beanit.com)
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


public class Fwd401CarrierXmitSweepProfile implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class SweepLegs implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static class SEQUENCE implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

			public byte[] code = null;
			private BerInteger dwellTime = null;
			private BerInteger sweepRate = null;
			private BerInteger endFreq = null;
			
			public SEQUENCE() {
			}

			public SEQUENCE(byte[] code) {
				this.code = code;
			}

			public void setDwellTime(BerInteger dwellTime) {
				this.dwellTime = dwellTime;
			}

			public BerInteger getDwellTime() {
				return dwellTime;
			}

			public void setSweepRate(BerInteger sweepRate) {
				this.sweepRate = sweepRate;
			}

			public BerInteger getSweepRate() {
				return sweepRate;
			}

			public void setEndFreq(BerInteger endFreq) {
				this.endFreq = endFreq;
			}

			public BerInteger getEndFreq() {
				return endFreq;
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
				codeLength += endFreq.encode(reverseOS, true);
				
				codeLength += sweepRate.encode(reverseOS, true);
				
				codeLength += dwellTime.encode(reverseOS, true);
				
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
					dwellTime = new BerInteger();
					subCodeLength += dwellTime.decode(is, false);
					subCodeLength += berTag.decode(is);
				}
				else {
					throw new IOException("Tag does not match the mandatory sequence element tag.");
				}
				
				if (berTag.equals(BerInteger.tag)) {
					sweepRate = new BerInteger();
					subCodeLength += sweepRate.decode(is, false);
					subCodeLength += berTag.decode(is);
				}
				else {
					throw new IOException("Tag does not match the mandatory sequence element tag.");
				}
				
				if (berTag.equals(BerInteger.tag)) {
					endFreq = new BerInteger();
					subCodeLength += endFreq.decode(is, false);
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
				if (dwellTime != null) {
					sb.append("dwellTime: ").append(dwellTime);
				}
				else {
					sb.append("dwellTime: <empty-required-field>");
				}
				
				sb.append(",\n");
				for (int i = 0; i < indentLevel + 1; i++) {
					sb.append("\t");
				}
				if (sweepRate != null) {
					sb.append("sweepRate: ").append(sweepRate);
				}
				else {
					sb.append("sweepRate: <empty-required-field>");
				}
				
				sb.append(",\n");
				for (int i = 0; i < indentLevel + 1; i++) {
					sb.append("\t");
				}
				if (endFreq != null) {
					sb.append("endFreq: ").append(endFreq);
				}
				else {
					sb.append("endFreq: <empty-required-field>");
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
		private List<SEQUENCE> seqOf = null;

		public SweepLegs() {
			seqOf = new ArrayList<SEQUENCE>();
		}

		public SweepLegs(byte[] code) {
			this.code = code;
		}

		public List<SEQUENCE> getSEQUENCE() {
			if (seqOf == null) {
				seqOf = new ArrayList<SEQUENCE>();
			}
			return seqOf;
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
			for (int i = (seqOf.size() - 1); i >= 0; i--) {
				codeLength += seqOf.get(i).encode(reverseOS, true);
			}

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
			if (withTag) {
				codeLength += tag.decodeAndCheck(is);
			}

			BerLength length = new BerLength();
			codeLength += length.decode(is);
			int totalLength = length.val;

			while (subCodeLength < totalLength) {
				SEQUENCE element = new SEQUENCE();
				subCodeLength += element.decode(is, true);
				seqOf.add(element);
			}
			if (subCodeLength != totalLength) {
				throw new IOException("Decoded SequenceOf or SetOf has wrong length. Expected " + totalLength + " but has " + subCodeLength);

			}
			codeLength += subCodeLength;

			return codeLength;
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

			sb.append("{\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (seqOf == null) {
				sb.append("null");
			}
			else {
				Iterator<SEQUENCE> it = seqOf.iterator();
				if (it.hasNext()) {
					it.next().appendAsString(sb, indentLevel + 1);
					while (it.hasNext()) {
						sb.append(",\n");
						for (int i = 0; i < indentLevel + 1; i++) {
							sb.append("\t");
						}
						it.next().appendAsString(sb, indentLevel + 1);
					}
				}
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
	private BerInteger startfreq = null;
	private SweepLegs sweepLegs = null;
	
	public Fwd401CarrierXmitSweepProfile() {
	}

	public Fwd401CarrierXmitSweepProfile(byte[] code) {
		this.code = code;
	}

	public void setStartfreq(BerInteger startfreq) {
		this.startfreq = startfreq;
	}

	public BerInteger getStartfreq() {
		return startfreq;
	}

	public void setSweepLegs(SweepLegs sweepLegs) {
		this.sweepLegs = sweepLegs;
	}

	public SweepLegs getSweepLegs() {
		return sweepLegs;
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
		codeLength += sweepLegs.encode(reverseOS, true);
		
		codeLength += startfreq.encode(reverseOS, true);
		
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
			startfreq = new BerInteger();
			subCodeLength += startfreq.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		if (berTag.equals(SweepLegs.tag)) {
			sweepLegs = new SweepLegs();
			subCodeLength += sweepLegs.decode(is, false);
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
		if (startfreq != null) {
			sb.append("startfreq: ").append(startfreq);
		}
		else {
			sb.append("startfreq: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (sweepLegs != null) {
			sb.append("sweepLegs: ");
			sweepLegs.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("sweepLegs: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

