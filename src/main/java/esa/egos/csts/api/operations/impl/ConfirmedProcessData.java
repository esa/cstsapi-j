package esa.egos.csts.api.operations.impl;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import ccsds.csts.common.operations.pdus.ProcessDataReturn;
import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.DataUnitId;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.util.impl.CSTSUtils;

public class ConfirmedProcessData extends AbstractConfirmedOperation implements IConfirmedProcessData {

	private final OperationType type = OperationType.CONFIRMED_PROCESS_DATA;
	
	private final static int versionNumber = 1;

	private long dataUnitID;
	private byte[] data;

	public ConfirmedProcessData() {
		super(versionNumber);
	}
	
	@Override
	public OperationType getType() {
		return type;
	}

	@Override
	public boolean isBlocking() {
		return true;
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
			throw new ApiException("Invalid PROCESS DATA invocation arguments.");
		}
		if (data == null) {
			throw new ApiException("Invalid PROCESS DATA invocation arguments.");
		}
	}

	@Override
	public String print(int i) {
		return "ConfirmedProcessData [dataUnitID=" + dataUnitID + ", data=" + Arrays.toString(data) + "]";
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation() {
		return encodeProcessDataInvocation(CSTSUtils.nonUsedExtension());
	}

	@Override
	public ProcessDataInvocation encodeProcessDataInvocation(Embedded extendedData) {
		return encodeProcessDataInvocation(extendedData, CSTSUtils.nonUsedExtension());
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

	@Override
	public ProcessDataReturn encodeProcessDataReturn() {
		return encodeStandardReturnHeader(ProcessDataReturn.class);
	}
	
	@Override
	public ProcessDataReturn encodeProcessDataReturn(Extended resultExtension) {
		return encodeStandardReturnHeader(ProcessDataReturn.class, resultExtension);
	}
	
	@Override
	public void decodeProcessDataReturn(ProcessDataReturn processDataReturn) {
		decodeStandardReturnHeader(processDataReturn);
	}

}
