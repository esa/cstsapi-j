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


public class CfdpRemoteEntityConfiguration implements BerType, Serializable {

	private static final long serialVersionUID = 1L;

	public static class SEQUENCE implements BerType, Serializable {

		private static final long serialVersionUID = 1L;

		public static class CfdpRemoteKeepAliveDiscrepancyLimit implements BerType, Serializable {

			private static final long serialVersionUID = 1L;

			public byte[] code = null;
			private BerInteger keepAliveDiscrepancyLimit = null;
			private BerNull notApplicable = null;
			
			public CfdpRemoteKeepAliveDiscrepancyLimit() {
			}

			public CfdpRemoteKeepAliveDiscrepancyLimit(byte[] code) {
				this.code = code;
			}

			public void setKeepAliveDiscrepancyLimit(BerInteger keepAliveDiscrepancyLimit) {
				this.keepAliveDiscrepancyLimit = keepAliveDiscrepancyLimit;
			}

			public BerInteger getKeepAliveDiscrepancyLimit() {
				return keepAliveDiscrepancyLimit;
			}

			public void setNotApplicable(BerNull notApplicable) {
				this.notApplicable = notApplicable;
			}

			public BerNull getNotApplicable() {
				return notApplicable;
			}

			public int encode(OutputStream reverseOS) throws IOException {

				if (code != null) {
					for (int i = code.length - 1; i >= 0; i--) {
						reverseOS.write(code[i]);
					}
					return code.length;
				}

				int codeLength = 0;
				if (notApplicable != null) {
					codeLength += notApplicable.encode(reverseOS, false);
					// write tag: CONTEXT_CLASS, PRIMITIVE, 1
					reverseOS.write(0x81);
					codeLength += 1;
					return codeLength;
				}
				
				if (keepAliveDiscrepancyLimit != null) {
					codeLength += keepAliveDiscrepancyLimit.encode(reverseOS, false);
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
					keepAliveDiscrepancyLimit = new BerInteger();
					codeLength += keepAliveDiscrepancyLimit.decode(is, false);
					return codeLength;
				}

				if (berTag.equals(BerTag.CONTEXT_CLASS, BerTag.PRIMITIVE, 1)) {
					notApplicable = new BerNull();
					codeLength += notApplicable.decode(is, false);
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

				if (keepAliveDiscrepancyLimit != null) {
					sb.append("keepAliveDiscrepancyLimit: ").append(keepAliveDiscrepancyLimit);
					return;
				}

				if (notApplicable != null) {
					sb.append("notApplicable: ").append(notApplicable);
					return;
				}

				sb.append("<none>");
			}

		}

		public static final BerTag tag = new BerTag(BerTag.UNIVERSAL_CLASS, BerTag.CONSTRUCTED, 16);

		public byte[] code = null;
		private CfdpEntityId cfdpRemoteEntityId = null;
		private BerVisibleString cfdpRemoteUtAddress = null;
		private BerInteger cfdpRemoteOneWayLightTime = null;
		private BerInteger cfdpRemoteTotalRoundTripAllowanceForQueuingDelay = null;
		private CfdpOptionalTimer cfdpRemoteAsynchronousNakInterval = null;
		private CfdpOptionalTimer cfdpRemoteAsynchronousKeepAliveInterval = null;
		private CfdpOptionalTimer cfdpRemoteAsynchronousReportInterval = null;
		private TcLinkStat cfdpRemoteImmediateNakModeEnabled = null;
		private CfdpOptionalTimer cfdpRemotePromptTransmissionInterval = null;
		private CfdpTransmissionModeType cfdpRemoteDefaultTransmissionMode = null;
		private BerEnum cfdpRemoteDispositionOfIncompleteReceivedFile = null;
		private TcLinkStat cfdpRemoteCrcRequiredOnTransmission = null;
		private BerInteger cfdpRemoteMaximumFileSegmentLength = null;
		private CfdpRemoteKeepAliveDiscrepancyLimit cfdpRemoteKeepAliveDiscrepancyLimit = null;
		private BerInteger cfdpRemotePositiveAckTimerExpirationLimit = null;
		private BerInteger cfdpRemoteNakTimerExpirationLimit = null;
		private CfdpTimer cfdpRemoteTransactionInactivityLimit = null;
		private BerEnum cfdpRemoteTransmissionWindow = null;
		private BerEnum cfdpRemoteReceptionWindow = null;
		
		public SEQUENCE() {
		}

		public SEQUENCE(byte[] code) {
			this.code = code;
		}

		public void setCfdpRemoteEntityId(CfdpEntityId cfdpRemoteEntityId) {
			this.cfdpRemoteEntityId = cfdpRemoteEntityId;
		}

		public CfdpEntityId getCfdpRemoteEntityId() {
			return cfdpRemoteEntityId;
		}

		public void setCfdpRemoteUtAddress(BerVisibleString cfdpRemoteUtAddress) {
			this.cfdpRemoteUtAddress = cfdpRemoteUtAddress;
		}

		public BerVisibleString getCfdpRemoteUtAddress() {
			return cfdpRemoteUtAddress;
		}

		public void setCfdpRemoteOneWayLightTime(BerInteger cfdpRemoteOneWayLightTime) {
			this.cfdpRemoteOneWayLightTime = cfdpRemoteOneWayLightTime;
		}

		public BerInteger getCfdpRemoteOneWayLightTime() {
			return cfdpRemoteOneWayLightTime;
		}

		public void setCfdpRemoteTotalRoundTripAllowanceForQueuingDelay(BerInteger cfdpRemoteTotalRoundTripAllowanceForQueuingDelay) {
			this.cfdpRemoteTotalRoundTripAllowanceForQueuingDelay = cfdpRemoteTotalRoundTripAllowanceForQueuingDelay;
		}

		public BerInteger getCfdpRemoteTotalRoundTripAllowanceForQueuingDelay() {
			return cfdpRemoteTotalRoundTripAllowanceForQueuingDelay;
		}

		public void setCfdpRemoteAsynchronousNakInterval(CfdpOptionalTimer cfdpRemoteAsynchronousNakInterval) {
			this.cfdpRemoteAsynchronousNakInterval = cfdpRemoteAsynchronousNakInterval;
		}

		public CfdpOptionalTimer getCfdpRemoteAsynchronousNakInterval() {
			return cfdpRemoteAsynchronousNakInterval;
		}

		public void setCfdpRemoteAsynchronousKeepAliveInterval(CfdpOptionalTimer cfdpRemoteAsynchronousKeepAliveInterval) {
			this.cfdpRemoteAsynchronousKeepAliveInterval = cfdpRemoteAsynchronousKeepAliveInterval;
		}

		public CfdpOptionalTimer getCfdpRemoteAsynchronousKeepAliveInterval() {
			return cfdpRemoteAsynchronousKeepAliveInterval;
		}

		public void setCfdpRemoteAsynchronousReportInterval(CfdpOptionalTimer cfdpRemoteAsynchronousReportInterval) {
			this.cfdpRemoteAsynchronousReportInterval = cfdpRemoteAsynchronousReportInterval;
		}

		public CfdpOptionalTimer getCfdpRemoteAsynchronousReportInterval() {
			return cfdpRemoteAsynchronousReportInterval;
		}

		public void setCfdpRemoteImmediateNakModeEnabled(TcLinkStat cfdpRemoteImmediateNakModeEnabled) {
			this.cfdpRemoteImmediateNakModeEnabled = cfdpRemoteImmediateNakModeEnabled;
		}

		public TcLinkStat getCfdpRemoteImmediateNakModeEnabled() {
			return cfdpRemoteImmediateNakModeEnabled;
		}

		public void setCfdpRemotePromptTransmissionInterval(CfdpOptionalTimer cfdpRemotePromptTransmissionInterval) {
			this.cfdpRemotePromptTransmissionInterval = cfdpRemotePromptTransmissionInterval;
		}

		public CfdpOptionalTimer getCfdpRemotePromptTransmissionInterval() {
			return cfdpRemotePromptTransmissionInterval;
		}

		public void setCfdpRemoteDefaultTransmissionMode(CfdpTransmissionModeType cfdpRemoteDefaultTransmissionMode) {
			this.cfdpRemoteDefaultTransmissionMode = cfdpRemoteDefaultTransmissionMode;
		}

		public CfdpTransmissionModeType getCfdpRemoteDefaultTransmissionMode() {
			return cfdpRemoteDefaultTransmissionMode;
		}

		public void setCfdpRemoteDispositionOfIncompleteReceivedFile(BerEnum cfdpRemoteDispositionOfIncompleteReceivedFile) {
			this.cfdpRemoteDispositionOfIncompleteReceivedFile = cfdpRemoteDispositionOfIncompleteReceivedFile;
		}

		public BerEnum getCfdpRemoteDispositionOfIncompleteReceivedFile() {
			return cfdpRemoteDispositionOfIncompleteReceivedFile;
		}

		public void setCfdpRemoteCrcRequiredOnTransmission(TcLinkStat cfdpRemoteCrcRequiredOnTransmission) {
			this.cfdpRemoteCrcRequiredOnTransmission = cfdpRemoteCrcRequiredOnTransmission;
		}

		public TcLinkStat getCfdpRemoteCrcRequiredOnTransmission() {
			return cfdpRemoteCrcRequiredOnTransmission;
		}

		public void setCfdpRemoteMaximumFileSegmentLength(BerInteger cfdpRemoteMaximumFileSegmentLength) {
			this.cfdpRemoteMaximumFileSegmentLength = cfdpRemoteMaximumFileSegmentLength;
		}

		public BerInteger getCfdpRemoteMaximumFileSegmentLength() {
			return cfdpRemoteMaximumFileSegmentLength;
		}

		public void setCfdpRemoteKeepAliveDiscrepancyLimit(CfdpRemoteKeepAliveDiscrepancyLimit cfdpRemoteKeepAliveDiscrepancyLimit) {
			this.cfdpRemoteKeepAliveDiscrepancyLimit = cfdpRemoteKeepAliveDiscrepancyLimit;
		}

		public CfdpRemoteKeepAliveDiscrepancyLimit getCfdpRemoteKeepAliveDiscrepancyLimit() {
			return cfdpRemoteKeepAliveDiscrepancyLimit;
		}

		public void setCfdpRemotePositiveAckTimerExpirationLimit(BerInteger cfdpRemotePositiveAckTimerExpirationLimit) {
			this.cfdpRemotePositiveAckTimerExpirationLimit = cfdpRemotePositiveAckTimerExpirationLimit;
		}

		public BerInteger getCfdpRemotePositiveAckTimerExpirationLimit() {
			return cfdpRemotePositiveAckTimerExpirationLimit;
		}

		public void setCfdpRemoteNakTimerExpirationLimit(BerInteger cfdpRemoteNakTimerExpirationLimit) {
			this.cfdpRemoteNakTimerExpirationLimit = cfdpRemoteNakTimerExpirationLimit;
		}

		public BerInteger getCfdpRemoteNakTimerExpirationLimit() {
			return cfdpRemoteNakTimerExpirationLimit;
		}

		public void setCfdpRemoteTransactionInactivityLimit(CfdpTimer cfdpRemoteTransactionInactivityLimit) {
			this.cfdpRemoteTransactionInactivityLimit = cfdpRemoteTransactionInactivityLimit;
		}

		public CfdpTimer getCfdpRemoteTransactionInactivityLimit() {
			return cfdpRemoteTransactionInactivityLimit;
		}

		public void setCfdpRemoteTransmissionWindow(BerEnum cfdpRemoteTransmissionWindow) {
			this.cfdpRemoteTransmissionWindow = cfdpRemoteTransmissionWindow;
		}

		public BerEnum getCfdpRemoteTransmissionWindow() {
			return cfdpRemoteTransmissionWindow;
		}

		public void setCfdpRemoteReceptionWindow(BerEnum cfdpRemoteReceptionWindow) {
			this.cfdpRemoteReceptionWindow = cfdpRemoteReceptionWindow;
		}

		public BerEnum getCfdpRemoteReceptionWindow() {
			return cfdpRemoteReceptionWindow;
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
			codeLength += cfdpRemoteReceptionWindow.encode(reverseOS, true);
			
			codeLength += cfdpRemoteTransmissionWindow.encode(reverseOS, true);
			
			codeLength += cfdpRemoteTransactionInactivityLimit.encode(reverseOS, true);
			
			codeLength += cfdpRemoteNakTimerExpirationLimit.encode(reverseOS, true);
			
			codeLength += cfdpRemotePositiveAckTimerExpirationLimit.encode(reverseOS, true);
			
			codeLength += cfdpRemoteKeepAliveDiscrepancyLimit.encode(reverseOS);
			
			codeLength += cfdpRemoteMaximumFileSegmentLength.encode(reverseOS, true);
			
			codeLength += cfdpRemoteCrcRequiredOnTransmission.encode(reverseOS, true);
			
			codeLength += cfdpRemoteDispositionOfIncompleteReceivedFile.encode(reverseOS, true);
			
			codeLength += cfdpRemoteDefaultTransmissionMode.encode(reverseOS, true);
			
			codeLength += cfdpRemotePromptTransmissionInterval.encode(reverseOS);
			
			codeLength += cfdpRemoteImmediateNakModeEnabled.encode(reverseOS, true);
			
			codeLength += cfdpRemoteAsynchronousReportInterval.encode(reverseOS);
			
			codeLength += cfdpRemoteAsynchronousKeepAliveInterval.encode(reverseOS);
			
			codeLength += cfdpRemoteAsynchronousNakInterval.encode(reverseOS);
			
			codeLength += cfdpRemoteTotalRoundTripAllowanceForQueuingDelay.encode(reverseOS, true);
			
			codeLength += cfdpRemoteOneWayLightTime.encode(reverseOS, true);
			
			codeLength += cfdpRemoteUtAddress.encode(reverseOS, true);
			
			codeLength += cfdpRemoteEntityId.encode(reverseOS, true);
			
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
			if (berTag.equals(CfdpEntityId.tag)) {
				cfdpRemoteEntityId = new CfdpEntityId();
				subCodeLength += cfdpRemoteEntityId.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerVisibleString.tag)) {
				cfdpRemoteUtAddress = new BerVisibleString();
				subCodeLength += cfdpRemoteUtAddress.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				cfdpRemoteOneWayLightTime = new BerInteger();
				subCodeLength += cfdpRemoteOneWayLightTime.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				cfdpRemoteTotalRoundTripAllowanceForQueuingDelay = new BerInteger();
				subCodeLength += cfdpRemoteTotalRoundTripAllowanceForQueuingDelay.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			cfdpRemoteAsynchronousNakInterval = new CfdpOptionalTimer();
			subCodeLength += cfdpRemoteAsynchronousNakInterval.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			cfdpRemoteAsynchronousKeepAliveInterval = new CfdpOptionalTimer();
			subCodeLength += cfdpRemoteAsynchronousKeepAliveInterval.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			cfdpRemoteAsynchronousReportInterval = new CfdpOptionalTimer();
			subCodeLength += cfdpRemoteAsynchronousReportInterval.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			if (berTag.equals(TcLinkStat.tag)) {
				cfdpRemoteImmediateNakModeEnabled = new TcLinkStat();
				subCodeLength += cfdpRemoteImmediateNakModeEnabled.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			cfdpRemotePromptTransmissionInterval = new CfdpOptionalTimer();
			subCodeLength += cfdpRemotePromptTransmissionInterval.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			if (berTag.equals(CfdpTransmissionModeType.tag)) {
				cfdpRemoteDefaultTransmissionMode = new CfdpTransmissionModeType();
				subCodeLength += cfdpRemoteDefaultTransmissionMode.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerEnum.tag)) {
				cfdpRemoteDispositionOfIncompleteReceivedFile = new BerEnum();
				subCodeLength += cfdpRemoteDispositionOfIncompleteReceivedFile.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(TcLinkStat.tag)) {
				cfdpRemoteCrcRequiredOnTransmission = new TcLinkStat();
				subCodeLength += cfdpRemoteCrcRequiredOnTransmission.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				cfdpRemoteMaximumFileSegmentLength = new BerInteger();
				subCodeLength += cfdpRemoteMaximumFileSegmentLength.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			cfdpRemoteKeepAliveDiscrepancyLimit = new CfdpRemoteKeepAliveDiscrepancyLimit();
			subCodeLength += cfdpRemoteKeepAliveDiscrepancyLimit.decode(is, berTag);
			subCodeLength += berTag.decode(is);
			
			if (berTag.equals(BerInteger.tag)) {
				cfdpRemotePositiveAckTimerExpirationLimit = new BerInteger();
				subCodeLength += cfdpRemotePositiveAckTimerExpirationLimit.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerInteger.tag)) {
				cfdpRemoteNakTimerExpirationLimit = new BerInteger();
				subCodeLength += cfdpRemoteNakTimerExpirationLimit.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(CfdpTimer.tag)) {
				cfdpRemoteTransactionInactivityLimit = new CfdpTimer();
				subCodeLength += cfdpRemoteTransactionInactivityLimit.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerEnum.tag)) {
				cfdpRemoteTransmissionWindow = new BerEnum();
				subCodeLength += cfdpRemoteTransmissionWindow.decode(is, false);
				subCodeLength += berTag.decode(is);
			}
			else {
				throw new IOException("Tag does not match the mandatory sequence element tag.");
			}
			
			if (berTag.equals(BerEnum.tag)) {
				cfdpRemoteReceptionWindow = new BerEnum();
				subCodeLength += cfdpRemoteReceptionWindow.decode(is, false);
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
			if (cfdpRemoteEntityId != null) {
				sb.append("cfdpRemoteEntityId: ").append(cfdpRemoteEntityId);
			}
			else {
				sb.append("cfdpRemoteEntityId: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteUtAddress != null) {
				sb.append("cfdpRemoteUtAddress: ").append(cfdpRemoteUtAddress);
			}
			else {
				sb.append("cfdpRemoteUtAddress: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteOneWayLightTime != null) {
				sb.append("cfdpRemoteOneWayLightTime: ").append(cfdpRemoteOneWayLightTime);
			}
			else {
				sb.append("cfdpRemoteOneWayLightTime: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteTotalRoundTripAllowanceForQueuingDelay != null) {
				sb.append("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay: ").append(cfdpRemoteTotalRoundTripAllowanceForQueuingDelay);
			}
			else {
				sb.append("cfdpRemoteTotalRoundTripAllowanceForQueuingDelay: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteAsynchronousNakInterval != null) {
				sb.append("cfdpRemoteAsynchronousNakInterval: ");
				cfdpRemoteAsynchronousNakInterval.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("cfdpRemoteAsynchronousNakInterval: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteAsynchronousKeepAliveInterval != null) {
				sb.append("cfdpRemoteAsynchronousKeepAliveInterval: ");
				cfdpRemoteAsynchronousKeepAliveInterval.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("cfdpRemoteAsynchronousKeepAliveInterval: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteAsynchronousReportInterval != null) {
				sb.append("cfdpRemoteAsynchronousReportInterval: ");
				cfdpRemoteAsynchronousReportInterval.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("cfdpRemoteAsynchronousReportInterval: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteImmediateNakModeEnabled != null) {
				sb.append("cfdpRemoteImmediateNakModeEnabled: ").append(cfdpRemoteImmediateNakModeEnabled);
			}
			else {
				sb.append("cfdpRemoteImmediateNakModeEnabled: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemotePromptTransmissionInterval != null) {
				sb.append("cfdpRemotePromptTransmissionInterval: ");
				cfdpRemotePromptTransmissionInterval.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("cfdpRemotePromptTransmissionInterval: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteDefaultTransmissionMode != null) {
				sb.append("cfdpRemoteDefaultTransmissionMode: ").append(cfdpRemoteDefaultTransmissionMode);
			}
			else {
				sb.append("cfdpRemoteDefaultTransmissionMode: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteDispositionOfIncompleteReceivedFile != null) {
				sb.append("cfdpRemoteDispositionOfIncompleteReceivedFile: ").append(cfdpRemoteDispositionOfIncompleteReceivedFile);
			}
			else {
				sb.append("cfdpRemoteDispositionOfIncompleteReceivedFile: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteCrcRequiredOnTransmission != null) {
				sb.append("cfdpRemoteCrcRequiredOnTransmission: ").append(cfdpRemoteCrcRequiredOnTransmission);
			}
			else {
				sb.append("cfdpRemoteCrcRequiredOnTransmission: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteMaximumFileSegmentLength != null) {
				sb.append("cfdpRemoteMaximumFileSegmentLength: ").append(cfdpRemoteMaximumFileSegmentLength);
			}
			else {
				sb.append("cfdpRemoteMaximumFileSegmentLength: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteKeepAliveDiscrepancyLimit != null) {
				sb.append("cfdpRemoteKeepAliveDiscrepancyLimit: ");
				cfdpRemoteKeepAliveDiscrepancyLimit.appendAsString(sb, indentLevel + 1);
			}
			else {
				sb.append("cfdpRemoteKeepAliveDiscrepancyLimit: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemotePositiveAckTimerExpirationLimit != null) {
				sb.append("cfdpRemotePositiveAckTimerExpirationLimit: ").append(cfdpRemotePositiveAckTimerExpirationLimit);
			}
			else {
				sb.append("cfdpRemotePositiveAckTimerExpirationLimit: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteNakTimerExpirationLimit != null) {
				sb.append("cfdpRemoteNakTimerExpirationLimit: ").append(cfdpRemoteNakTimerExpirationLimit);
			}
			else {
				sb.append("cfdpRemoteNakTimerExpirationLimit: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteTransactionInactivityLimit != null) {
				sb.append("cfdpRemoteTransactionInactivityLimit: ").append(cfdpRemoteTransactionInactivityLimit);
			}
			else {
				sb.append("cfdpRemoteTransactionInactivityLimit: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteTransmissionWindow != null) {
				sb.append("cfdpRemoteTransmissionWindow: ").append(cfdpRemoteTransmissionWindow);
			}
			else {
				sb.append("cfdpRemoteTransmissionWindow: <empty-required-field>");
			}
			
			sb.append(",\n");
			for (int i = 0; i < indentLevel + 1; i++) {
				sb.append("\t");
			}
			if (cfdpRemoteReceptionWindow != null) {
				sb.append("cfdpRemoteReceptionWindow: ").append(cfdpRemoteReceptionWindow);
			}
			else {
				sb.append("cfdpRemoteReceptionWindow: <empty-required-field>");
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

	public CfdpRemoteEntityConfiguration() {
		seqOf = new ArrayList<SEQUENCE>();
	}

	public CfdpRemoteEntityConfiguration(byte[] code) {
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
