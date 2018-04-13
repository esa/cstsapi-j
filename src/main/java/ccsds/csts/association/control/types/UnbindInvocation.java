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

public class UnbindInvocation implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private StandardInvocationHeader standardInvocationHeader = null;
	private Extended unbindInvocationExtension = null;
	
	public UnbindInvocation() {
	}

	public UnbindInvocation(byte[] code) {
		this.code = code;
	}

	public void setStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		this.standardInvocationHeader = standardInvocationHeader;
	}

	public StandardInvocationHeader getStandardInvocationHeader() {
		return standardInvocationHeader;
	}

	public void setUnbindInvocationExtension(Extended unbindInvocationExtension) {
		this.unbindInvocationExtension = unbindInvocationExtension;
	}

	public Extended getUnbindInvocationExtension() {
		return unbindInvocationExtension;
	}

	public int encode(BerByteArrayOutputStream os) throws IOException {
		return encode(os, true);
	}

	public int encode(BerByteArrayOutputStream os, boolean withTag) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				os.write(code[i]);
			}
			if (withTag) {
				return tag.encode(os) + code.length;
			}
			return code.length;
		}

		int codeLength = 0;
		codeLength += unbindInvocationExtension.encode(os);
		
		codeLength += standardInvocationHeader.encode(os, true);
		
		codeLength += BerLength.encodeLength(os, codeLength);

		if (withTag) {
			codeLength += tag.encode(os);
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
		if (berTag.equals(StandardInvocationHeader.tag)) {
			standardInvocationHeader = new StandardInvocationHeader();
			subCodeLength += standardInvocationHeader.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		unbindInvocationExtension = new Extended();
		subCodeLength += unbindInvocationExtension.decode(is, berTag);
		if (subCodeLength == totalLength) {
			return codeLength;
		}
		throw new IOException("Unexpected end of sequence, length tag: " + totalLength + ", actual sequence length: " + subCodeLength);

		
	}

	public void encodeAndSave(int encodingSizeGuess) throws IOException {
		BerByteArrayOutputStream os = new BerByteArrayOutputStream(encodingSizeGuess);
		encode(os, false);
		code = os.getArray();
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
		if (standardInvocationHeader != null) {
			sb.append("standardInvocationHeader: ");
			standardInvocationHeader.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("standardInvocationHeader: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (unbindInvocationExtension != null) {
			sb.append("unbindInvocationExtension: ");
			unbindInvocationExtension.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("unbindInvocationExtension: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

