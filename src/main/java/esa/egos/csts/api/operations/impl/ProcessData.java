package esa.egos.csts.api.operations.impl;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.DataUnitId;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.IProcessData;

public class ProcessData extends AbstractOperation implements IProcessData {

	private static final OperationType TYPE = OperationType.PROCESS_DATA;
	
	/**
	 * The data unit ID
	 */
	private long dataUnitID;
	
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
	 * The constructor of a PROCESS-DATA operation
	 */
	public ProcessData() {
		invocationExtension = Extension.notUsed();
	}
	
	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public long getDataUnitID() {
		return dataUnitID;
	}

	@Override
	public void setDataUnitID(long dataUnitID) {
		this.dataUnitID = dataUnitID;
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
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}
	
	@Override
	public void verifyInvocationArguments() throws ApiException {
		if (dataUnitID < 0) {
			throw new ApiException("Invalid PROCESS-DATA invocation arguments.");
		}
		if (data == null && embeddedData == null) {
			throw new ApiException("Invalid PROCESS DATA invocation arguments.");
		}
	}

	@Override
	public String print(int i) {
		return "ProcessData [dataUnitID=" + dataUnitID + ", data=" + Arrays.toString(data) + "]";
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation() {
		ProcessDataInvocation processDataInvocation = new ProcessDataInvocation();
		processDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		processDataInvocation.setDataUnitId(new DataUnitId(dataUnitID));
		AbstractChoice choice = new AbstractChoice();
		if (data != null) {
			choice.setOpaqueString(new BerOctetString(data));
		} else if (embeddedData != null) {
			choice.setExtendedData(embeddedData.encode());
		}
		processDataInvocation.setData(choice);
		processDataInvocation.setProcessDataInvocationExtension(invocationExtension.encode());
		return processDataInvocation;
	}

	@Override
	public void decodeProcessDataInvocation(ProcessDataInvocation processDataInvocation) {
		decodeStandardInvocationHeader(processDataInvocation.getStandardInvocationHeader());
		dataUnitID = processDataInvocation.getDataUnitId().longValue();
		if (processDataInvocation.getData().getOpaqueString() != null) {
			data = processDataInvocation.getData().getOpaqueString().value;
		}
		invocationExtension = Extension.decode(processDataInvocation.getProcessDataInvocationExtension());
	}

}
