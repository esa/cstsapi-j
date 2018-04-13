package esa.egos.csts.api.operations.impl;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.util.impl.ExtensionUtils;

public class ProcessData extends AbstractOperation implements IProcessData {

	private final static int versionNumber = 1;

	private long dataUnitID;
	private byte[] data;

	public ProcessData() {
		super(versionNumber, false);
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
	public void verifyInvocationArguments() throws ApiException {
		if (dataUnitID < 0) {
			throw new ApiException("Invalid PROCESS-DATA invocation arguments.");
		}
		if (data == null) {
			throw new ApiException("Invalid PROCESS-DATA invocation arguments.");
		}
	}

	@Override
	public String print(int i) {
		return "ProcessData [dataUnitID=" + dataUnitID + ", data=" + Arrays.toString(data) + "]";
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation() {
		return encodeProcessDataInvocation(ExtensionUtils.nonUsedExtension());
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation(Embedded extendedData) {
		return encodeProcessDataInvocation(extendedData, ExtensionUtils.nonUsedExtension());
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation(Extended extension) {
		ProcessDataInvocation processDataInvocation = new ProcessDataInvocation();
		processDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		processDataInvocation.setDataUnitId(new DataUnitId(dataUnitID));
		AbstractChoice choice = new AbstractChoice();
		choice.setOpaqueString(new BerOctetString(data));
		processDataInvocation.setData(choice);
		processDataInvocation.setProcessDataInvocationExtension(extension);
		return processDataInvocation;
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation(Embedded extendedData, Extended extension) {
		ProcessDataInvocation processDataInvocation = new ProcessDataInvocation();
		processDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		processDataInvocation.setDataUnitId(new DataUnitId(dataUnitID));
		AbstractChoice choice = new AbstractChoice();
		choice.setExtendedData(extendedData);
		processDataInvocation.setData(choice);
		processDataInvocation.setProcessDataInvocationExtension(extension);
		return processDataInvocation;
	}

	@Override
	public void decodeProcessDataInvocation(ProcessDataInvocation processDataInvocation) {
		decodeStandardInvocationHeader(processDataInvocation.getStandardInvocationHeader());
		dataUnitID = processDataInvocation.getDataUnitId().longValue();
		if (processDataInvocation.getData().getOpaqueString() != null) {
			data = processDataInvocation.getData().getOpaqueString().value;
		}
	}

}
