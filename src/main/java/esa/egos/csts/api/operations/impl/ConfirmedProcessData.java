package esa.egos.csts.api.operations.impl;

import java.util.Arrays;

import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import ccsds.csts.common.operations.pdus.ProcessDataReturn;
import ccsds.csts.common.types.AbstractChoice;
import ccsds.csts.common.types.DataUnitId;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IConfirmedProcessData;

public class ConfirmedProcessData extends AbstractConfirmedOperation implements IConfirmedProcessData {

	private static final OperationType TYPE = OperationType.CONFIRMED_PROCESS_DATA;
	
	/**
	 * The datat unit ID
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
	
	private Extension invocationExtension;

	public ConfirmedProcessData() {
		invocationExtension = Extension.notUsed();
	}
	
	@Override
	public OperationType getType() {
		return TYPE;
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

	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}
	
	@Override
	public void verifyInvocationArguments() throws ApiException {
		if (dataUnitID < 0) {
			throw new ApiException("Invalid PROCESS DATA invocation arguments.");
		}
		if (data == null && embeddedData == null) {
			throw new ApiException("Invalid PROCESS DATA invocation arguments.");
		}
	}

	@Override
	public String print(int i) {
		return "ConfirmedProcessData [dataUnitID=" + dataUnitID + ", data=" + Arrays.toString(data) + "]";
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

	@Override
	public ProcessDataReturn encodeProcessDataReturn() {
		return encodeStandardReturnHeader(ProcessDataReturn.class);
	}
	
	@Override
	public void decodeProcessDataReturn(ProcessDataReturn processDataReturn) {
		decodeStandardReturnHeader(processDataReturn);
	}

}
