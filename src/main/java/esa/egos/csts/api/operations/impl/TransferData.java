package esa.egos.csts.api.operations.impl;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.TransferDataInvocation;
import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.IntUnsigned;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.impl.CSTSUtils;

public class TransferData extends AbstractOperation implements ITransferData {

	private final OperationType type = OperationType.TRANSFER_DATA;
	
	private final static int versionNumber = 1;

	private Time generationTime;
	private long sequenceCounter;
	private byte[] data;

	public TransferData() {
		super(versionNumber, false);
	}
	
	@Override
	public OperationType getType() {
		return type;
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
	public void verifyInvocationArguments() throws ApiException {
		if (generationTime == null) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
		if (sequenceCounter < 0) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
		if (data == null) {
			throw new ApiException("Invalid TRANSFER-DATA invocation arguments.");
		}
	}

	@Override
	public String print(int i) {
		return "TransferData [generationTime=" + generationTime + ", sequenceCounter=" + sequenceCounter + ", data="
				+ Arrays.toString(data) + "]";
	}

	@Override
	public TransferDataInvocation encodeTransferDataInvocation() {
		return encodeTransferDataInvocation(CSTSUtils.nonUsedExtension());
	}

	@Override
	public TransferDataInvocation encodeTransferDataInvocation(Embedded extendedData) {
		return encodeTransferDataInvocation(extendedData, CSTSUtils.nonUsedExtension());
	}

	@Override
	public TransferDataInvocation encodeTransferDataInvocation(Extended extension) {
		TransferDataInvocation transferDataInvocation = new TransferDataInvocation();
		transferDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		transferDataInvocation.setGenerationTime(generationTime.encode());
		transferDataInvocation.setSequenceCounter(new IntUnsigned(sequenceCounter));
		AbstractChoice choice = new AbstractChoice();
		choice.setOpaqueString(new BerOctetString(data));
		transferDataInvocation.setData(choice);
		transferDataInvocation.setTransferDataInvocationExtension(extension);
		return transferDataInvocation;
	}

	@Override
	public TransferDataInvocation encodeTransferDataInvocation(Embedded extendedData, Extended extension) {
		TransferDataInvocation transferDataInvocation = new TransferDataInvocation();
		transferDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		transferDataInvocation.setGenerationTime(generationTime.encode());
		transferDataInvocation.setSequenceCounter(new IntUnsigned(sequenceCounter));
		AbstractChoice choice = new AbstractChoice();
		choice.setExtendedData(extendedData);
		transferDataInvocation.setData(choice);
		transferDataInvocation.setTransferDataInvocationExtension(extension);
		return transferDataInvocation;
	}

	@Override
	public void decodeTransferDataInvocation(TransferDataInvocation transferDataInvocation) {
		decodeStandardInvocationHeader(transferDataInvocation.getStandardInvocationHeader());
		generationTime = Time.decode(transferDataInvocation.getGenerationTime());
		sequenceCounter = transferDataInvocation.getSequenceCounter().longValue();
		if (transferDataInvocation.getData().getOpaqueString() != null) {
			data = transferDataInvocation.getData().getOpaqueString().value;
		}
	}

}
