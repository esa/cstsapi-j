/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b1.ccsds.csts.common.types;

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


public class ListOfParamEventsDiagnostics implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] code = null;
	public static class UnknownParamEventIdentifier implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static class CHOICE implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public byte[] code = null;
			private Label paramEventLabel = null;
			private Name paramEventName = null;
			
			public CHOICE() {
			}

			public CHOICE(byte[] code) {
				this.code = code;
			}

			public void setParamEventLabel(Label paramEventLabel) {
				this.paramEventLabel = paramEventLabel;
			}

			public Label getParamEventLabel() {
				return paramEventLabel;
			}

			public void setParamEventName(Name paramEventName) {
				this.paramEventName = paramEventName;
			}

			public Name getParamEventName() {
				return paramEventName;
			}

			public int encode(OutputStream reverseOS) throws IOException {

				if (code != null) {
					for (int i = code.length - 1; i >= 0; i--) {
						reverseOS.write(code[i]);
					}
					return code.length;
				}

				int codeLength = 0;
				if (paramEventName != null) {
					codeLength += paramEventName.encode(reverseOS, false);
					// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
					reverseOS.write(0xA1);
					codeLength += 1;
					return codeLength;
				}
				
				if (paramEventLabel != null) {
					codeLength += paramEventLabel.encode(reverseOS, false);
					// write tag: CONTEXT_CLASS, CONSTRUCTED, 2
					reverseOS.write(0xA2);
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

				if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 2)) {
					paramEventLabel = new Label();
					codeLength += paramEventLabel.decode(is, false);
					return codeLength;
				}

				if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
					paramEventName = new Name();
					codeLength += paramEventName.decode(is, false);
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

				if (paramEventLabel != null) {
					sb.append("paramEventLabel: ");
					paramEventLabel.appendAsString(sb, indentLevel + 1);
					return;
				}

				if (paramEventName != null) {
					sb.append("paramEventName: ");
					paramEventName.appendAsString(sb, indentLevel + 1);
					return;
				}

				sb.append("<none>");
			}

		}

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);
		public byte[] code = null;
		private List<CHOICE> seqOf = null;

		public UnknownParamEventIdentifier() {
			seqOf = new ArrayList<CHOICE>();
		}

		public UnknownParamEventIdentifier(byte[] code) {
			this.code = code;
		}

		public List<CHOICE> getCHOICE() {
			if (seqOf == null) {
				seqOf = new ArrayList<CHOICE>();
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
				codeLength += seqOf.get(i).encode(reverseOS);
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
				CHOICE element = new CHOICE();
				subCodeLength += element.decode(is, null);
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
				Iterator<CHOICE> it = seqOf.iterator();
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

	private AdditionalText undefinedDefault = null;
	private BerVisibleString unknownListName = null;
	private FunctionalResourceType unknownFunctionalResourceType = null;
	private FunctionalResourceName unknownFunctionalResourceName = null;
	private ProcedureType unknownProcedureType = null;
	private ProcedureInstanceId unknownProcedureInstanceId = null;
	private UnknownParamEventIdentifier unknownParamEventIdentifier = null;
	
	public ListOfParamEventsDiagnostics() {
	}

	public ListOfParamEventsDiagnostics(byte[] code) {
		this.code = code;
	}

	public void setUndefinedDefault(AdditionalText undefinedDefault) {
		this.undefinedDefault = undefinedDefault;
	}

	public AdditionalText getUndefinedDefault() {
		return undefinedDefault;
	}

	public void setUnknownListName(BerVisibleString unknownListName) {
		this.unknownListName = unknownListName;
	}

	public BerVisibleString getUnknownListName() {
		return unknownListName;
	}

	public void setUnknownFunctionalResourceType(FunctionalResourceType unknownFunctionalResourceType) {
		this.unknownFunctionalResourceType = unknownFunctionalResourceType;
	}

	public FunctionalResourceType getUnknownFunctionalResourceType() {
		return unknownFunctionalResourceType;
	}

	public void setUnknownFunctionalResourceName(FunctionalResourceName unknownFunctionalResourceName) {
		this.unknownFunctionalResourceName = unknownFunctionalResourceName;
	}

	public FunctionalResourceName getUnknownFunctionalResourceName() {
		return unknownFunctionalResourceName;
	}

	public void setUnknownProcedureType(ProcedureType unknownProcedureType) {
		this.unknownProcedureType = unknownProcedureType;
	}

	public ProcedureType getUnknownProcedureType() {
		return unknownProcedureType;
	}

	public void setUnknownProcedureInstanceId(ProcedureInstanceId unknownProcedureInstanceId) {
		this.unknownProcedureInstanceId = unknownProcedureInstanceId;
	}

	public ProcedureInstanceId getUnknownProcedureInstanceId() {
		return unknownProcedureInstanceId;
	}

	public void setUnknownParamEventIdentifier(UnknownParamEventIdentifier unknownParamEventIdentifier) {
		this.unknownParamEventIdentifier = unknownParamEventIdentifier;
	}

	public UnknownParamEventIdentifier getUnknownParamEventIdentifier() {
		return unknownParamEventIdentifier;
	}

	public int encode(OutputStream reverseOS) throws IOException {

		if (code != null) {
			for (int i = code.length - 1; i >= 0; i--) {
				reverseOS.write(code[i]);
			}
			return code.length;
		}

		int codeLength = 0;
		if (unknownParamEventIdentifier != null) {
			codeLength += unknownParamEventIdentifier.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 3
			reverseOS.write(0xA3);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownProcedureInstanceId != null) {
			codeLength += unknownProcedureInstanceId.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 7
			reverseOS.write(0xA7);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownProcedureType != null) {
			codeLength += unknownProcedureType.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 6
			reverseOS.write(0x86);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownFunctionalResourceName != null) {
			codeLength += unknownFunctionalResourceName.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
			reverseOS.write(0xA1);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownFunctionalResourceType != null) {
			codeLength += unknownFunctionalResourceType.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 2
			reverseOS.write(0x82);
			codeLength += 1;
			return codeLength;
		}
		
		if (unknownListName != null) {
			codeLength += unknownListName.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 4
			reverseOS.write(0x84);
			codeLength += 1;
			return codeLength;
		}
		
		if (undefinedDefault != null) {
			codeLength += undefinedDefault.encode(reverseOS, false);
			// write tag: CONTEXT_CLASS, PRIMITIVE, 5
			reverseOS.write(0x85);
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

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 5)) {
			undefinedDefault = new AdditionalText();
			codeLength += undefinedDefault.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 4)) {
			unknownListName = new BerVisibleString();
			codeLength += unknownListName.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 2)) {
			unknownFunctionalResourceType = new FunctionalResourceType();
			codeLength += unknownFunctionalResourceType.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
			unknownFunctionalResourceName = new FunctionalResourceName();
			codeLength += unknownFunctionalResourceName.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 6)) {
			unknownProcedureType = new ProcedureType();
			codeLength += unknownProcedureType.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 7)) {
			unknownProcedureInstanceId = new ProcedureInstanceId();
			codeLength += unknownProcedureInstanceId.decode(is, false);
			return codeLength;
		}

		if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 3)) {
			unknownParamEventIdentifier = new UnknownParamEventIdentifier();
			codeLength += unknownParamEventIdentifier.decode(is, false);
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

		if (undefinedDefault != null) {
			sb.append("undefinedDefault: ").append(undefinedDefault);
			return;
		}

		if (unknownListName != null) {
			sb.append("unknownListName: ").append(unknownListName);
			return;
		}

		if (unknownFunctionalResourceType != null) {
			sb.append("unknownFunctionalResourceType: ").append(unknownFunctionalResourceType);
			return;
		}

		if (unknownFunctionalResourceName != null) {
			sb.append("unknownFunctionalResourceName: ");
			unknownFunctionalResourceName.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (unknownProcedureType != null) {
			sb.append("unknownProcedureType: ").append(unknownProcedureType);
			return;
		}

		if (unknownProcedureInstanceId != null) {
			sb.append("unknownProcedureInstanceId: ");
			unknownProcedureInstanceId.appendAsString(sb, indentLevel + 1);
			return;
		}

		if (unknownParamEventIdentifier != null) {
			sb.append("unknownParamEventIdentifier: ");
			unknownParamEventIdentifier.appendAsString(sb, indentLevel + 1);
			return;
		}

		sb.append("<none>");
	}

}

