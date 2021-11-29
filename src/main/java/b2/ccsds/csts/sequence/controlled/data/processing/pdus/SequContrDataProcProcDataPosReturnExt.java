/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.sequence.controlled.data.processing.pdus;

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

import b2.ccsds.csts.common.types.ConditionalTime;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.pdus.CstsFrameworkPdu;

public class SequContrDataProcProcDataPosReturnExt implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private DataUnitId dataUnitIdPosRtn = null;
	private Extended sequContrDataProcProcDataPosReturnExtExtension = null;
	
	public SequContrDataProcProcDataPosReturnExt() {
	}

	public SequContrDataProcProcDataPosReturnExt(byte[] code) {
		this.code = code;
	}

	public void setDataUnitIdPosRtn(DataUnitId dataUnitIdPosRtn) {
		this.dataUnitIdPosRtn = dataUnitIdPosRtn;
	}

	public DataUnitId getDataUnitIdPosRtn() {
		return dataUnitIdPosRtn;
	}

	public void setSequContrDataProcProcDataPosReturnExtExtension(Extended sequContrDataProcProcDataPosReturnExtExtension) {
		this.sequContrDataProcProcDataPosReturnExtExtension = sequContrDataProcProcDataPosReturnExtExtension;
	}

	public Extended getSequContrDataProcProcDataPosReturnExtExtension() {
		return sequContrDataProcProcDataPosReturnExtExtension;
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
		codeLength += sequContrDataProcProcDataPosReturnExtExtension.encode(reverseOS);
		
		codeLength += dataUnitIdPosRtn.encode(reverseOS, true);
		
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
		if (berTag.equals(DataUnitId.tag)) {
			dataUnitIdPosRtn = new DataUnitId();
			subCodeLength += dataUnitIdPosRtn.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		sequContrDataProcProcDataPosReturnExtExtension = new Extended();
		subCodeLength += sequContrDataProcProcDataPosReturnExtExtension.decode(is, berTag);
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
		if (dataUnitIdPosRtn != null) {
			sb.append("dataUnitIdPosRtn: ").append(dataUnitIdPosRtn);
		}
		else {
			sb.append("dataUnitIdPosRtn: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (sequContrDataProcProcDataPosReturnExtExtension != null) {
			sb.append("sequContrDataProcProcDataPosReturnExtExtension: ");
			sequContrDataProcProcDataPosReturnExtExtension.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("sequContrDataProcProcDataPosReturnExtExtension: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

