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


public class TdmSegGenDoppInstPathTable implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class SEQUENCE implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private TdmPathTableCommonElements pathTableCommonElements = null;
		private BerVisibleString threeWayRcvAperture = null;
		private LabelListSetV1 rcvFreqBand = null;
		private LabelListSetV1 xmitFreqBand = null;
		
		public SEQUENCE() {
		}

		public SEQUENCE(byte[] code) {
			this.code = code;
		}

		public void setPathTableCommonElements(TdmPathTableCommonElements pathTableCommonElements) {
			this.pathTableCommonElements = pathTableCommonElements;
		}

		public TdmPathTableCommonElements getPathTableCommonElements() {
			return pathTableCommonElements;
		}

		public void setThreeWayRcvAperture(BerVisibleString threeWayRcvAperture) {
			this.threeWayRcvAperture = threeWayRcvAperture;
		}

		public BerVisibleString getThreeWayRcvAperture() {
			return threeWayRcvAperture;
		}

		public void setRcvFreqBand(LabelListSetV1 rcvFreqBand) {
			this.rcvFreqBand = rcvFreqBand;
		}

		public LabelListSetV1 getRcvFreqBand() {
			return rcvFreqBand;
		}

		public void setXmitFreqBand(LabelListSetV1 xmitFreqBand) {
			this.xmitFreqBand = xmitFreqBand;
		}

		public LabelListSetV1 getXmitFreqBand() {
			return xmitFreqBand;
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
			if (xmitFreqBand != null) {
				codeLength += xmitFreqBand.encode(reverseOS, true);
			}
			
			codeLength += rcvFreqBand.encode(reverseOS, true);
			
			if (threeWayRcvAperture != null) {
				codeLength += threeWayRcvAperture.encode(reverseOS, true);
			}
			
			codeLength += pathTableCommonElements.encode(reverseOS, true);
			
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
			if (berTag.equals(TdmPathTableCommonElements.tag)) {
				pathTableCommonElements = new TdmPathTableCommonElements();
				subCodeLength += pathTableCommonElements.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerVisibleString.tag)) {
				threeWayRcvAperture = new BerVisibleString();
				subCodeLength += threeWayRcvAperture.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			
			if (berTag.equals(LabelListSetV1.tag)) {
				rcvFreqBand = new LabelListSetV1();
				subCodeLength += rcvFreqBand.decode(is, false);
				if (subCodeLength == totalLength) {
					return codeLength;
				}
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(LabelListSetV1.tag)) {
				xmitFreqBand = new LabelListSetV1();
				subCodeLength += xmitFreqBand.decode(is, false);
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
			if (pathTableCommonElements != null) {
				sb.append("pathTableCommonElements: ");
				pathTableCommonElements.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("pathTableCommonElements: <empty-required-field>");
			}
			
			if (threeWayRcvAperture != null) {
				sb.append(",\n");
				for (int i = 0; i < indentLevel + 1; i++) {
					sb.append("\t");
				}
				sb.append("threeWayRcvAperture: ").append(threeWayRcvAperture);
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (rcvFreqBand != null) {
				sb.append("rcvFreqBand: ");
				rcvFreqBand.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("rcvFreqBand: <empty-required-field>");
			}
			
			if (xmitFreqBand != null) {
				sb.append(",\n");
				for (int i = 0; i < indentLevel + 1; i++) {
					sb.append("\t");
				}
				sb.append("xmitFreqBand: ");
				xmitFreqBand.appendAsString(sb, indentLevel + 1);
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

	public TdmSegGenDoppInstPathTable() {
		seqOf = new ArrayList<SEQUENCE>();
	}

	public TdmSegGenDoppInstPathTable(byte[] code) {
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

