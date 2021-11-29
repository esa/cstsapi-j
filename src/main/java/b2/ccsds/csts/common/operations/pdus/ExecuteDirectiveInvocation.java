/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.common.operations.pdus;

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

import b2.ccsds.csts.common.types.AbstractChoice;
import b2.ccsds.csts.common.types.AdditionalText;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.Embedded;
import b2.ccsds.csts.common.types.EventValue;
import b2.ccsds.csts.common.types.Extended;
import b2.ccsds.csts.common.types.FunctionalResourceInstanceNumber;
import b2.ccsds.csts.common.types.FunctionalResourceName;
import b2.ccsds.csts.common.types.IntUnsigned;
import b2.ccsds.csts.common.types.ListOfParamEventsDiagnostics;
import b2.ccsds.csts.common.types.ListOfParametersEvents;
import b2.ccsds.csts.common.types.Name;
import b2.ccsds.csts.common.types.ProcedureName;
import b2.ccsds.csts.common.types.PublishedIdentifier;
import b2.ccsds.csts.common.types.QualifiedParameter;
import b2.ccsds.csts.common.types.StandardAcknowledgeHeader;
import b2.ccsds.csts.common.types.StandardInvocationHeader;
import b2.ccsds.csts.common.types.StandardReturnHeader;
import b2.ccsds.csts.common.types.Time;
import b2.ccsds.csts.common.types.TypeAndValue;

public class ExecuteDirectiveInvocation implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class DirectiveQualifier implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public byte[] code = null;
		public static class ServiceProcDirQualifier implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

			public byte[] code = null;
			private ProcedureName targetProcedureName = null;
			private DirectiveQualifierValues serviceProcDirQualifierValues = null;
			
			public ServiceProcDirQualifier() {
			}

			public ServiceProcDirQualifier(byte[] code) {
				this.code = code;
			}

			public void setTargetProcedureName(ProcedureName targetProcedureName) {
				this.targetProcedureName = targetProcedureName;
			}

			public ProcedureName getTargetProcedureName() {
				return targetProcedureName;
			}

			public void setServiceProcDirQualifierValues(DirectiveQualifierValues serviceProcDirQualifierValues) {
				this.serviceProcDirQualifierValues = serviceProcDirQualifierValues;
			}

			public DirectiveQualifierValues getServiceProcDirQualifierValues() {
				return serviceProcDirQualifierValues;
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
				codeLength += serviceProcDirQualifierValues.encode(reverseOS);
				
				codeLength += targetProcedureName.encode(reverseOS, true);
				
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
				if (berTag.equals(ProcedureName.tag)) {
					targetProcedureName = new ProcedureName();
					subCodeLength += targetProcedureName.decode(is, false);
					subCodeLength += berTag.decode(is);
				}
				else {
					throw new IOException("Tag does not match the mandatory sequence element tag.");
				}
				
				serviceProcDirQualifierValues = new DirectiveQualifierValues();
				subCodeLength += serviceProcDirQualifierValues.decode(is, berTag);
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
				if (targetProcedureName != null) {
					sb.append("targetProcedureName: ");
					targetProcedureName.appendAsString(sb, indentLevel + 1);
				}
				else {
					sb.append("targetProcedureName: <empty-required-field>");
				}
				
				sb.append(",\n");
				for (int i = 0; i < indentLevel + 1; i++) {
					sb.append("\t");
				}
				if (serviceProcDirQualifierValues != null) {
					sb.append("serviceProcDirQualifierValues: ");
					serviceProcDirQualifierValues.appendAsString(sb, indentLevel + 1);
				}
				else {
					sb.append("serviceProcDirQualifierValues: <empty-required-field>");
				}
				
				sb.append("\n");
				for (int i = 0; i < indentLevel; i++) {
					sb.append("\t");
				}
				sb.append("}");
			}

		}

		public static class FunctResourceDirQualifier implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

			public byte[] code = null;
			private FunctionalResourceName functResourceName = null;
			private DirectiveQualifierValues functionalResourceQualifiers = null;
			
			public FunctResourceDirQualifier() {
			}

			public FunctResourceDirQualifier(byte[] code) {
				this.code = code;
			}

			public void setFunctResourceName(FunctionalResourceName functResourceName) {
				this.functResourceName = functResourceName;
			}

			public FunctionalResourceName getFunctResourceName() {
				return functResourceName;
			}

			public void setFunctionalResourceQualifiers(DirectiveQualifierValues functionalResourceQualifiers) {
				this.functionalResourceQualifiers = functionalResourceQualifiers;
			}

			public DirectiveQualifierValues getFunctionalResourceQualifiers() {
				return functionalResourceQualifiers;
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
				codeLength += functionalResourceQualifiers.encode(reverseOS);
				
				codeLength += functResourceName.encode(reverseOS, true);
				
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
				if (berTag.equals(FunctionalResourceName.tag)) {
					functResourceName = new FunctionalResourceName();
					subCodeLength += functResourceName.decode(is, false);
					subCodeLength += berTag.decode(is);
				}
				else {
					throw new IOException("Tag does not match the mandatory sequence element tag.");
				}
				
				functionalResourceQualifiers = new DirectiveQualifierValues();
				subCodeLength += functionalResourceQualifiers.decode(is, berTag);
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
				if (functResourceName != null) {
					sb.append("functResourceName: ");
					functResourceName.appendAsString(sb, indentLevel + 1);
				}
				else {
					sb.append("functResourceName: <empty-required-field>");
				}
				
				sb.append(",\n");
				for (int i = 0; i < indentLevel + 1; i++) {
					sb.append("\t");
				}
				if (functionalResourceQualifiers != null) {
					sb.append("functionalResourceQualifiers: ");
					functionalResourceQualifiers.appendAsString(sb, indentLevel + 1);
				}
				else {
					sb.append("functionalResourceQualifiers: <empty-required-field>");
				}
				
				sb.append("\n");
				for (int i = 0; i < indentLevel; i++) {
					sb.append("\t");
				}
				sb.append("}");
			}

		}

		private DirectiveQualifierValues localProcDirQualifier = null;
		private ServiceProcDirQualifier serviceProcDirQualifier = null;
		private FunctResourceDirQualifier functResourceDirQualifier = null;
		private Embedded directiveQualifierExtension = null;
		
		public DirectiveQualifier() {
		}

		public DirectiveQualifier(byte[] code) {
			this.code = code;
		}

		public void setLocalProcDirQualifier(DirectiveQualifierValues localProcDirQualifier) {
			this.localProcDirQualifier = localProcDirQualifier;
		}

		public DirectiveQualifierValues getLocalProcDirQualifier() {
			return localProcDirQualifier;
		}

		public void setServiceProcDirQualifier(ServiceProcDirQualifier serviceProcDirQualifier) {
			this.serviceProcDirQualifier = serviceProcDirQualifier;
		}

		public ServiceProcDirQualifier getServiceProcDirQualifier() {
			return serviceProcDirQualifier;
		}

		public void setFunctResourceDirQualifier(FunctResourceDirQualifier functResourceDirQualifier) {
			this.functResourceDirQualifier = functResourceDirQualifier;
		}

		public FunctResourceDirQualifier getFunctResourceDirQualifier() {
			return functResourceDirQualifier;
		}

		public void setDirectiveQualifierExtension(Embedded directiveQualifierExtension) {
			this.directiveQualifierExtension = directiveQualifierExtension;
		}

		public Embedded getDirectiveQualifierExtension() {
			return directiveQualifierExtension;
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

			if (directiveQualifierExtension != null) {
				codeLength += directiveQualifierExtension.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, CONSTRUCTED, 4
				reverseOS.write(0xA4);
				codeLength += 1;
				return codeLength;
			}
			
			if (functResourceDirQualifier != null) {
				codeLength += functResourceDirQualifier.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, CONSTRUCTED, 3
				reverseOS.write(0xA3);
				codeLength += 1;
				return codeLength;
			}
			
			if (serviceProcDirQualifier != null) {
				codeLength += serviceProcDirQualifier.encode(reverseOS, false);
				// write tag: CONTEXT_CLASS, CONSTRUCTED, 2
				reverseOS.write(0xA2);
				codeLength += 1;
				return codeLength;
			}
			
			if (localProcDirQualifier != null) {
				sublength = localProcDirQualifier.encode(reverseOS);
				codeLength += sublength;
				codeLength += BerLength.encodeLength(reverseOS, sublength);
				// write tag: CONTEXT_CLASS, CONSTRUCTED, 1
				reverseOS.write(0xA1);
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

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 1)) {
				codeLength += BerLength.skip(is);
				localProcDirQualifier = new DirectiveQualifierValues();
				codeLength += localProcDirQualifier.decode(is, null);
				return codeLength;
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 2)) {
				serviceProcDirQualifier = new ServiceProcDirQualifier();
				codeLength += serviceProcDirQualifier.decode(is, false);
				return codeLength;
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 3)) {
				functResourceDirQualifier = new FunctResourceDirQualifier();
				codeLength += functResourceDirQualifier.decode(is, false);
				return codeLength;
			}

			if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.CONSTRUCTED, 4)) {
				directiveQualifierExtension = new Embedded();
				codeLength += directiveQualifierExtension.decode(is, false);
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

			if (localProcDirQualifier != null) {
				sb.append("localProcDirQualifier: ");
				localProcDirQualifier.appendAsString(sb, indentLevel + 1);
				return;
			}

			if (serviceProcDirQualifier != null) {
				sb.append("serviceProcDirQualifier: ");
				serviceProcDirQualifier.appendAsString(sb, indentLevel + 1);
				return;
			}

			if (functResourceDirQualifier != null) {
				sb.append("functResourceDirQualifier: ");
				functResourceDirQualifier.appendAsString(sb, indentLevel + 1);
				return;
			}

			if (directiveQualifierExtension != null) {
				sb.append("directiveQualifierExtension: ");
				directiveQualifierExtension.appendAsString(sb, indentLevel + 1);
				return;
			}

			sb.append("<none>");
		}

	}

	public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

	public byte[] code = null;
	private StandardInvocationHeader standardInvocationHeader = null;
	private PublishedIdentifier directiveIdentifier = null;
	private DirectiveQualifier directiveQualifier = null;
	private Extended executeDirectiveInvocationExtension = null;
	
	public ExecuteDirectiveInvocation() {
	}

	public ExecuteDirectiveInvocation(byte[] code) {
		this.code = code;
	}

	public void setStandardInvocationHeader(StandardInvocationHeader standardInvocationHeader) {
		this.standardInvocationHeader = standardInvocationHeader;
	}

	public StandardInvocationHeader getStandardInvocationHeader() {
		return standardInvocationHeader;
	}

	public void setDirectiveIdentifier(PublishedIdentifier directiveIdentifier) {
		this.directiveIdentifier = directiveIdentifier;
	}

	public PublishedIdentifier getDirectiveIdentifier() {
		return directiveIdentifier;
	}

	public void setDirectiveQualifier(DirectiveQualifier directiveQualifier) {
		this.directiveQualifier = directiveQualifier;
	}

	public DirectiveQualifier getDirectiveQualifier() {
		return directiveQualifier;
	}

	public void setExecuteDirectiveInvocationExtension(Extended executeDirectiveInvocationExtension) {
		this.executeDirectiveInvocationExtension = executeDirectiveInvocationExtension;
	}

	public Extended getExecuteDirectiveInvocationExtension() {
		return executeDirectiveInvocationExtension;
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
		codeLength += executeDirectiveInvocationExtension.encode(reverseOS);
		
		codeLength += directiveQualifier.encode(reverseOS);
		
		codeLength += directiveIdentifier.encode(reverseOS, true);
		
		codeLength += standardInvocationHeader.encode(reverseOS, true);
		
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
		if (berTag.equals(StandardInvocationHeader.tag)) {
			standardInvocationHeader = new StandardInvocationHeader();
			subCodeLength += standardInvocationHeader.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		if (berTag.equals(PublishedIdentifier.tag)) {
			directiveIdentifier = new PublishedIdentifier();
			subCodeLength += directiveIdentifier.decode(is, false);
			subCodeLength += berTag.decode(is);
		}
		else {
			throw new IOException("Tag does not match the mandatory sequence element tag.");
		}
		
		directiveQualifier = new DirectiveQualifier();
		subCodeLength += directiveQualifier.decode(is, berTag);
		subCodeLength += berTag.decode(is);
		
		executeDirectiveInvocationExtension = new Extended();
		subCodeLength += executeDirectiveInvocationExtension.decode(is, berTag);
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
		if (directiveIdentifier != null) {
			sb.append("directiveIdentifier: ").append(directiveIdentifier);
		}
		else {
			sb.append("directiveIdentifier: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (directiveQualifier != null) {
			sb.append("directiveQualifier: ");
			directiveQualifier.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("directiveQualifier: <empty-required-field>");
		}
		
		sb.append(",\n");
		for (int i = 0; i < indentLevel + 1; i++) {
			sb.append("\t");
		}
		if (executeDirectiveInvocationExtension != null) {
			sb.append("executeDirectiveInvocationExtension: ");
			executeDirectiveInvocationExtension.appendAsString(sb, indentLevel + 1);
		}
		else {
			sb.append("executeDirectiveInvocationExtension: <empty-required-field>");
		}
		
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("\t");
		}
		sb.append("}");
	}

}

