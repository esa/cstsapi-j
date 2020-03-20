package esa.egos.csts.api.operations.impl;

import java.util.Arrays;
import java.util.logging.Level;

import com.beanit.jasn1.ber.types.BerOctetString;

import b1.ccsds.csts.common.operations.pdus.TransferDataInvocation;
import b1.ccsds.csts.common.types.AbstractChoice;
import b1.ccsds.csts.common.types.IntUnsigned;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.CSTS_LOG;

/**
 * This class represents a TRANSFER-DATA operation.
 */
public class TransferData extends AbstractOperation implements ITransferData {

	private static final OperationType TYPE = OperationType.TRANSFER_DATA;

	/**
	 * The generation time
	 */
	private Time generationTime;

	/**
	 * The sequence counter
	 */
	private long sequenceCounter;

	/**
	 * The encoded data
	 */
	private byte[] data;

	/**
	 * The embedded data
	 */
	private EmbeddedData embeddedData;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of a TRANSFER-DATA operation.
	 */
	public TransferData() {
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public Time getGenerationTime() {
		return generationTime;
	}

	@Override
	public void setGenerationTime(Time generationTime) {
		this.generationTime = generationTime;
	}

	@Override
	public long getSequenceCounter() {
		return sequenceCounter;
	}

	@Override
	public void setSequenceCounter(long sequenceCounter) {
		this.sequenceCounter = sequenceCounter;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public EmbeddedData getEmbeddedData() {
		return embeddedData;
	}

	@Override
	public void setEmbeddedData(EmbeddedData embeddedData) {
		this.embeddedData = embeddedData;
	}

	@Override
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public void verifyInvocationArguments() throws ApiException {
		if (generationTime == null) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
		if (sequenceCounter < 0) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
		if (data == null && embeddedData == null) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
	}

	/**
	 * Return a String w/ CSTS Transfer-Data operation parameters
	 * @param i capacity
	 * @return String w/ CSTS Transfer-Data parameters
	 */
	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		String dataString = "<logged for csts.api.operations.level = FINEST>";
		
		if(CSTS_LOG.CSTS_OP_LOGGER.isLoggable(Level.FINEST)) {
			StringBuilder hexData = new StringBuilder(System.lineSeparator());
			CSTS_LOG.dumpHex(data, data.length, hexData);
			dataString = hexData.toString();
		}

		sb.append("\nOperation                      : TRANSFER-DATA").append('\n');
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : ").append(this.isConfirmed()).append('\n');
		sb.append("Operation Result               : positive\n");
		sb.append("Diagnostic Type                : no diagnostics\n");
		sb.append("Common Diagnostics             : invalid\n");
		sb.append("Generation time                : ").append(this.generationTime.toInstant()).append('\n');
		sb.append("Sequence counter               : ").append(this.sequenceCounter).append('\n');
		sb.append("Data                           : ").append(dataString).append('\n');

		return sb.toString();
	}

	@Override
	public TransferDataInvocation encodeTransferDataInvocation() {
		TransferDataInvocation transferDataInvocation = new TransferDataInvocation();
		transferDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		transferDataInvocation.setGenerationTime(generationTime.encode());
		transferDataInvocation.setSequenceCounter(new IntUnsigned(sequenceCounter));
		AbstractChoice choice = new AbstractChoice();
		if (data != null) {
			choice.setOpaqueString(new BerOctetString(data));
		} else if (embeddedData != null) {
			choice.setExtendedData(embeddedData.encode());
		}
		transferDataInvocation.setData(choice);
		transferDataInvocation.setTransferDataInvocationExtension(invocationExtension.encode());
		return transferDataInvocation;
	}

	@Override
	public void decodeTransferDataInvocation(TransferDataInvocation transferDataInvocation) {
		decodeStandardInvocationHeader(transferDataInvocation.getStandardInvocationHeader());
		generationTime = Time.decode(transferDataInvocation.getGenerationTime());
		sequenceCounter = transferDataInvocation.getSequenceCounter().longValue();
		if (transferDataInvocation.getData().getOpaqueString() != null) {
			data = transferDataInvocation.getData().getOpaqueString().value;
		} else if (transferDataInvocation.getData().getExtendedData() != null) {
			embeddedData = EmbeddedData.decode(transferDataInvocation.getData().getExtendedData());
		}
		invocationExtension = Extension.decode(transferDataInvocation.getTransferDataInvocationExtension());
	}

	@Override
	public String toString() {
		return "TransferData [generationTime=" + generationTime + ", sequenceCounter=" + sequenceCounter + ", data="
				+ Arrays.toString(data) + ", embeddedData=" + embeddedData + ", invocationExtension="
				+ invocationExtension + "]";
	}

}
