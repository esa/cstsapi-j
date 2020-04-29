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


public class RcfRequestedGvcid implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class Tm implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static class Vcid implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public byte[] code = null;
			private BerNull masterChannel = null;
			private BerInteger vcid = null;
			
			public Vcid() {
			}

			public Vcid(byte[] code) {
				this.code = code;
			}

			public void setMasterChannel(BerNull masterChannel) {
				this.masterChannel = masterChannel;
			}

			public BerNull getMasterChannel() {
				return masterChannel;
			}

			public void setVcid(BerInteger vcid) {
				this.vcid = vcid;
			}

			public BerInteger getVcid() {
				return vcid;
			}

			public int encode(OutputStream reverseOS) throws IOException {

				if (code != null) {
					for (int i = code.length - 1; i >= 0; i--) {
						reverseOS.write(code[i]);
					}
					return code.length;
				}

				int codeLength = 0;
				if (vcid != null) {
					codeLength += vcid.encode(reverseOS, false);
					// write tag: CONTEXT_CLASS, PRIMITIVE, 1
					reverseOS.write(0x81);
					codeLength += 1;
					return codeLength;
				}
				
				if (masterChannel != null) {
					codeLength += masterChannel.encode(reverseOS, false);
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
					masterChannel = new BerNull();
					codeLength += masterChannel.decode(is, false);
					return codeLength;
				}

				if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
					vcid = new BerInteger();
					codeLength += vcid.decode(is, false);
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

				if (masterChannel != null) {
					sb.append("masterChannel: ").append(masterChannel);
					return;
				}

				if (vcid != null) {
					sb.append("vcid: ").append(vcid);
					return;
				}

				sb.append("<none>");
			}

		}

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerInteger tfvn = null;
		private BerInteger scid = null;
		private Vcid vcid = null;
		
		public Tm() {
		}

		public Tm(byte[] code) {
			this.code = code;
		}

		public void setTfvn(BerInteger tfvn) {
			this.tfvn = tfvn;
		}

		public BerInteger getTfvn() {
			return tfvn;
		}

		public void setScid(BerInteger scid) {
			this.scid = scid;
		}

		public BerInteger getScid() {
			return scid;
		}

		public void setVcid(Vcid vcid) {
			this.vcid = vcid;
		}

		public Vcid getVcid() {
			return vcid;
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
			codeLength += vcid.encode(reverseOS);
			
			codeLength += scid.encode(reverseOS, true);
			
			codeLength += tfvn.encode(reverseOS, true);
			
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
				tfvn = new BerInteger();
				subCodeLength += tfvn.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				scid = new BerInteger();
				subCodeLength += scid.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			vcid = new Vcid();
			subCodeLength += vcid.decode(is, berTag);
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
			if (tfvn != null) {
				sb.append("tfvn: ").append(tfvn);
			}
			else {
				sb.append("tfvn: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (scid != null) {
				sb.append("scid: ").append(scid);
			}
			else {
				sb.append("scid: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (vcid != null) {
				sb.append("vcid: ");
				vcid.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("vcid: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	public static class Aos implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static class Vcid implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public byte[] code = null;
			private BerNull masterChannel = null;
			private BerInteger virtualChannel = null;
			
			public Vcid() {
			}

			public Vcid(byte[] code) {
				this.code = code;
			}

			public void setMasterChannel(BerNull masterChannel) {
				this.masterChannel = masterChannel;
			}

			public BerNull getMasterChannel() {
				return masterChannel;
			}

			public void setVirtualChannel(BerInteger virtualChannel) {
				this.virtualChannel = virtualChannel;
			}

			public BerInteger getVirtualChannel() {
				return virtualChannel;
			}

			public int encode(OutputStream reverseOS) throws IOException {

				if (code != null) {
					for (int i = code.length - 1; i >= 0; i--) {
						reverseOS.write(code[i]);
					}
					return code.length;
				}

				int codeLength = 0;
				if (virtualChannel != null) {
					codeLength += virtualChannel.encode(reverseOS, false);
					// write tag: CONTEXT_CLASS, PRIMITIVE, 1
					reverseOS.write(0x81);
					codeLength += 1;
					return codeLength;
				}
				
				if (masterChannel != null) {
					codeLength += masterChannel.encode(reverseOS, false);
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
					masterChannel = new BerNull();
					codeLength += masterChannel.decode(is, false);
					return codeLength;
				}

				if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
					virtualChannel = new BerInteger();
					codeLength += virtualChannel.decode(is, false);
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

				if (masterChannel != null) {
					sb.append("masterChannel: ").append(masterChannel);
					return;
				}

				if (virtualChannel != null) {
					sb.append("virtualChannel: ").append(virtualChannel);
					return;
				}

				sb.append("<none>");
			}

		}

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerInteger tfvn = null;
		private BerInteger scid = null;
		private Vcid vcid = null;
		
		public Aos() {
		}

		public Aos(byte[] code) {
			this.code = code;
		}

		public void setTfvn(BerInteger tfvn) {
			this.tfvn = tfvn;
		}

		public BerInteger getTfvn() {
			return tfvn;
		}

		public void setScid(BerInteger scid) {
			this.scid = scid;
		}

		public BerInteger getScid() {
			return scid;
		}

		public void setVcid(Vcid vcid) {
			this.vcid = vcid;
		}

		public Vcid getVcid() {
			return vcid;
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
			codeLength += vcid.encode(reverseOS);
			
			codeLength += scid.encode(reverseOS, true);
			
			codeLength += tfvn.encode(reverseOS, true);
			
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
				tfvn = new BerInteger();
				subCodeLength += tfvn.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				scid = new BerInteger();
				subCodeLength += scid.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			vcid = new Vcid();
			subCodeLength += vcid.decode(is, berTag);
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
			if (tfvn != null) {
				sb.append("tfvn: ").append(tfvn);
			}
			else {
				sb.append("tfvn: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (scid != null) {
				sb.append("scid: ").append(scid);
			}
			else {
				sb.append("scid: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (vcid != null) {
				sb.append("vcid: ");
				vcid.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("vcid: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	private Tm tm = null;
	private Aos aos = null;
	
	public RcfRequestedGvcid() {
	}

	public RcfRequestedGvcid(byte[] code) {
		this.code = code;
	}

	public void setTm(Tm tm) {
		this.tm = tm;
	}

	public Tm getTm() {
		return tm;
	}

	public void setAos(Aos aos) {
		this.aos = aos;
	}

	public Aos getAos() {
		return aos;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (aos != null) {
			codeLength += aos.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			reverseOS.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (tm != null) {
			codeLength += tm.encode(reverseOS, false);
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
			tm = new Tm();
			codeLength += tm.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			aos = new Aos();
			codeLength += aos.decode(is, false);
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

		if (tm != null) {
			sb.append("tm: ");
			tm.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (aos != null) {
			sb.append("aos: ");
			aos.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

