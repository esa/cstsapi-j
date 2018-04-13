/**
 * This class file was automatically generated by jASN1 v1.8.1 (http://www.openmuc.org)
 */

package ccsds.csts.association.control.types;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import org.openmuc.jasn1.ber.*;
import org.openmuc.jasn1.ber.types.*;
import org.openmuc.jasn1.ber.types.string.*;

import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.AuthorityIdentifier;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.IntPos;
import ccsds.csts.common.types.PortId;
import ccsds.csts.common.types.PublishedIdentifier;
import ccsds.csts.common.types.StandardInvocationHeader;
import ccsds.csts.common.types.StandardReturnHeader;
import ccsds.csts.pdus.CstsFrameworkPdu;
import ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public class AssocBindDiagnosticExt implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	private AdditionalText accessDenied = null;
	private AdditionalText serviceTypeNotSupported = null;
	private AdditionalText versionNotSupported = null;
	private AdditionalText noSuchServiceInstance = null;
	private AdditionalText alreadyBound = null;
	private AdditionalText siNotAccessibleToThisInitiator = null;
	private AdditionalText inconsistentServiceType = null;
	private AdditionalText outOfService = null;
	private Embedded assocBindDiagnosticExtExtension = null;
	
	public AssocBindDiagnosticExt() {
	}

	public AssocBindDiagnosticExt(byte[] code) {
		this.code = code;
	}

	public void setAccessDenied(AdditionalText accessDenied) {
		this.accessDenied = accessDenied;
	}

	public AdditionalText getAccessDenied() {
		return accessDenied;
	}

	public void setServiceTypeNotSupported(AdditionalText serviceTypeNotSupported) {
		this.serviceTypeNotSupported = serviceTypeNotSupported;
	}

	public AdditionalText getServiceTypeNotSupported() {
		return serviceTypeNotSupported;
	}

	public void setVersionNotSupported(AdditionalText versionNotSupported) {
		this.versionNotSupported = versionNotSupported;
	}

	public AdditionalText getVersionNotSupported() {
		return versionNotSupported;
	}

	public void setNoSuchServiceInstance(AdditionalText noSuchServiceInstance) {
		this.noSuchServiceInstance = noSuchServiceInstance;
	}

	public AdditionalText getNoSuchServiceInstance() {
		return noSuchServiceInstance;
	}

	public void setAlreadyBound(AdditionalText alreadyBound) {
		this.alreadyBound = alreadyBound;
	}

	public AdditionalText getAlreadyBound() {
		return alreadyBound;
	}

	public void setSiNotAccessibleToThisInitiator(AdditionalText siNotAccessibleToThisInitiator) {
		this.siNotAccessibleToThisInitiator = siNotAccessibleToThisInitiator;
	}

	public AdditionalText getSiNotAccessibleToThisInitiator() {
		return siNotAccessibleToThisInitiator;
	}

	public void setInconsistentServiceType(AdditionalText inconsistentServiceType) {
		this.inconsistentServiceType = inconsistentServiceType;
	}

	public AdditionalText getInconsistentServiceType() {
		return inconsistentServiceType;
	}

	public void setOutOfService(AdditionalText outOfService) {
		this.outOfService = outOfService;
	}

	public AdditionalText getOutOfService() {
		return outOfService;
	}

	public void setAssocBindDiagnosticExtExtension(Embedded assocBindDiagnosticExtExtension) {
		this.assocBindDiagnosticExtExtension = assocBindDiagnosticExtExtension;
	}

	public Embedded getAssocBindDiagnosticExtExtension() {
		return assocBindDiagnosticExtExtension;
	}

	public int encode(BerByteArrayOutputStream os) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				os.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (assocBindDiagnosticExtExtension != null) {
			codeLength += assocBindDiagnosticExtExtension.encode(os, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 100
			os.write(0x64);
			os.write(0xBF);
			codeLength += 2;
			return codeLength;
		}
		
		if (outOfService != null) {
			codeLength += outOfService.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 8
			os.write(0x88);
			codeLength += 1;
			return codeLength;
		}
		
		if (inconsistentServiceType != null) {
			codeLength += inconsistentServiceType.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 7
			os.write(0x87);
			codeLength += 1;
			return codeLength;
		}
		
		if (siNotAccessibleToThisInitiator != null) {
			codeLength += siNotAccessibleToThisInitiator.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 6
			os.write(0x86);
			codeLength += 1;
			return codeLength;
		}
		
		if (alreadyBound != null) {
			codeLength += alreadyBound.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 5
			os.write(0x85);
			codeLength += 1;
			return codeLength;
		}
		
		if (noSuchServiceInstance != null) {
			codeLength += noSuchServiceInstance.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 4
			os.write(0x84);
			codeLength += 1;
			return codeLength;
		}
		
		if (versionNotSupported != null) {
			codeLength += versionNotSupported.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 3
			os.write(0x83);
			codeLength += 1;
			return codeLength;
		}
		
		if (serviceTypeNotSupported != null) {
			codeLength += serviceTypeNotSupported.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			os.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (accessDenied != null) {
			codeLength += accessDenied.encode(os, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 1
			os.write(0x81);
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

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
			accessDenied = new AdditionalText();
			codeLength += accessDenied.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			serviceTypeNotSupported = new AdditionalText();
			codeLength += serviceTypeNotSupported.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 3)) {
			versionNotSupported = new AdditionalText();
			codeLength += versionNotSupported.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 4)) {
			noSuchServiceInstance = new AdditionalText();
			codeLength += noSuchServiceInstance.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 5)) {
			alreadyBound = new AdditionalText();
			codeLength += alreadyBound.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 6)) {
			siNotAccessibleToThisInitiator = new AdditionalText();
			codeLength += siNotAccessibleToThisInitiator.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 7)) {
			inconsistentServiceType = new AdditionalText();
			codeLength += inconsistentServiceType.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 8)) {
			outOfService = new AdditionalText();
			codeLength += outOfService.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 100)) {
			assocBindDiagnosticExtExtension = new Embedded();
			codeLength += assocBindDiagnosticExtExtension.decode(is, false);
			return codeLength;
		}

		if (passedTag != null) {
			return 0;
		}

		throw new IOException("Error decoding CHOICE: Tag " + berTag + " matched to no item.");
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
		encode(os);
		code = os.getArray();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendAsString(sb, 0);
		return sb.toString();
	}

	public void appendAsString(StringBuilder sb, int indentLevel) {

		if (accessDenied != null) {
			sb.append("accessDenied: ").append(accessDenied);
			return;
		}

		if (serviceTypeNotSupported != null) {
			sb.append("serviceTypeNotSupported: ").append(serviceTypeNotSupported);
			return;
		}

		if (versionNotSupported != null) {
			sb.append("versionNotSupported: ").append(versionNotSupported);
			return;
		}

		if (noSuchServiceInstance != null) {
			sb.append("noSuchServiceInstance: ").append(noSuchServiceInstance);
			return;
		}

		if (alreadyBound != null) {
			sb.append("alreadyBound: ").append(alreadyBound);
			return;
		}

		if (siNotAccessibleToThisInitiator != null) {
			sb.append("siNotAccessibleToThisInitiator: ").append(siNotAccessibleToThisInitiator);
			return;
		}

		if (inconsistentServiceType != null) {
			sb.append("inconsistentServiceType: ").append(inconsistentServiceType);
			return;
		}

		if (outOfService != null) {
			sb.append("outOfService: ").append(outOfService);
			return;
		}

		if (assocBindDiagnosticExtExtension != null) {
			sb.append("assocBindDiagnosticExtExtension: ");
			assocBindDiagnosticExtExtension.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

