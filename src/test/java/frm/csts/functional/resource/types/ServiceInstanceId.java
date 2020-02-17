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


public class ServiceInstanceId implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class CstsServiceInstanceId implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private BerObjectIdentifier spacecraftId = null;
		private BerObjectIdentifier facilityId = null;
		private BerObjectIdentifier serviceType = null;
		private BerInteger serviceInstanceNumber = null;
		
		public CstsServiceInstanceId() {
		}

		public CstsServiceInstanceId(byte[] code) {
			this.code = code;
		}

		public void setSpacecraftId(BerObjectIdentifier spacecraftId) {
			this.spacecraftId = spacecraftId;
		}

		public BerObjectIdentifier getSpacecraftId() {
			return spacecraftId;
		}

		public void setFacilityId(BerObjectIdentifier facilityId) {
			this.facilityId = facilityId;
		}

		public BerObjectIdentifier getFacilityId() {
			return facilityId;
		}

		public void setServiceType(BerObjectIdentifier serviceType) {
			this.serviceType = serviceType;
		}

		public BerObjectIdentifier getServiceType() {
			return serviceType;
		}

		public void setServiceInstanceNumber(BerInteger serviceInstanceNumber) {
			this.serviceInstanceNumber = serviceInstanceNumber;
		}

		public BerInteger getServiceInstanceNumber() {
			return serviceInstanceNumber;
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
			codeLength += serviceInstanceNumber.encode(reverseOS, true);
			
			codeLength += serviceType.encode(reverseOS, true);
			
			codeLength += facilityId.encode(reverseOS, true);
			
			codeLength += spacecraftId.encode(reverseOS, true);
			
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
			if (berTag.equals(BerObjectIdentifier.tag)) {
				spacecraftId = new BerObjectIdentifier();
				subCodeLength += spacecraftId.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerObjectIdentifier.tag)) {
				facilityId = new BerObjectIdentifier();
				subCodeLength += facilityId.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerObjectIdentifier.tag)) {
				serviceType = new BerObjectIdentifier();
				subCodeLength += serviceType.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				serviceInstanceNumber = new BerInteger();
				subCodeLength += serviceInstanceNumber.decode(is, false);
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
			if (spacecraftId != null) {
				sb.append("spacecraftId: ").append(spacecraftId);
			}
			else {
				sb.append("spacecraftId: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (facilityId != null) {
				sb.append("facilityId: ").append(facilityId);
			}
			else {
				sb.append("facilityId: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (serviceType != null) {
				sb.append("serviceType: ").append(serviceType);
			}
			else {
				sb.append("serviceType: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (serviceInstanceNumber != null) {
				sb.append("serviceInstanceNumber: ").append(serviceInstanceNumber);
			}
			else {
				sb.append("serviceInstanceNumber: <empty-required-field>");
			}
			
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("\t");
			}
			sb.append("}");
		}

	}

	private BerOctetString sleServiceInstanceId = null;
	private CstsServiceInstanceId cstsServiceInstanceId = null;
	
	public ServiceInstanceId() {
	}

	public ServiceInstanceId(byte[] code) {
		this.code = code;
	}

	public void setSleServiceInstanceId(BerOctetString sleServiceInstanceId) {
		this.sleServiceInstanceId = sleServiceInstanceId;
	}

	public BerOctetString getSleServiceInstanceId() {
		return sleServiceInstanceId;
	}

	public void setCstsServiceInstanceId(CstsServiceInstanceId cstsServiceInstanceId) {
		this.cstsServiceInstanceId = cstsServiceInstanceId;
	}

	public CstsServiceInstanceId getCstsServiceInstanceId() {
		return cstsServiceInstanceId;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (cstsServiceInstanceId != null) {
			codeLength += cstsServiceInstanceId.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			reverseOS.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (sleServiceInstanceId != null) {
			codeLength += sleServiceInstanceId.encode(reverseOS, false);
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
			sleServiceInstanceId = new BerOctetString();
			codeLength += sleServiceInstanceId.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			cstsServiceInstanceId = new CstsServiceInstanceId();
			codeLength += cstsServiceInstanceId.decode(is, false);
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

		if (sleServiceInstanceId != null) {
			sb.append("sleServiceInstanceId: ").append(sleServiceInstanceId);
			return;
		}

		if (cstsServiceInstanceId != null) {
			sb.append("cstsServiceInstanceId: ");
			cstsServiceInstanceId.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

