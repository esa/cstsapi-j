package esa.egos.csts.api.operations.impl.b1;

import java.util.Arrays;

import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerType;

import b1.ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import b1.ccsds.csts.common.operations.pdus.ProcessDataReturn;
import b1.ccsds.csts.common.types.AbstractChoice;
import b1.ccsds.csts.common.types.DataUnitId;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IConfirmedProcessData;

/**
 * This class represents a PROCESS-DATA (confirmed) operation.
 */
public class ConfirmedProcessData extends AbstractConfirmedOperation implements IConfirmedProcessData {

	private static final OperationType TYPE = OperationType.CONFIRMED_PROCESS_DATA;

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
	public long getDataUnitId() {
		return dataUnitID;
	}

	@Override
	public void setDataUnitId(long dataUnitID) {
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
	public EmbeddedData getEmbeddedData() {
		return embeddedData;
	}

	@Override
	public void setEmbeddedData(EmbeddedData embeddedData) {
		this.embeddedData = embeddedData;
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
			choice.setExtendedData(embeddedData.encode(new b1.ccsds.csts.common.types.Embedded()));
		}
		processDataInvocation.setData(choice);
		processDataInvocation.setProcessDataInvocationExtension(invocationExtension.encode(
				new b1.ccsds.csts.common.types.Extended()));
		return processDataInvocation;
	}

	@Override
	public void decodeProcessDataInvocation(BerType processDataInvocation) {
		ProcessDataInvocation processDataInvocation1 = (ProcessDataInvocation) processDataInvocation;
		decodeStandardInvocationHeader(processDataInvocation1.getStandardInvocationHeader());
		dataUnitID = processDataInvocation1.getDataUnitId().longValue();
		if (processDataInvocation1.getData().getOpaqueString() != null) {
			data = processDataInvocation1.getData().getOpaqueString().value;
		}
		invocationExtension = Extension.decode(processDataInvocation1.getProcessDataInvocationExtension());
	}

	@Override
	public BerType encodeProcessDataReturn() {
		return encodeStandardReturnHeader(ProcessDataReturn.class);
	}

	@Override
	public void decodeProcessDataReturn(BerType processDataReturn) {
		ProcessDataReturn processDataReturn1 = (ProcessDataReturn) processDataReturn;
		decodeStandardReturnHeader(processDataReturn1);
	}

	@Override
	public String toString() {
		return "ConfirmedProcessData [dataUnitID=" + dataUnitID + ", data=" + Arrays.toString(data) + ", embeddedData="
				+ embeddedData + ", invocationExtension=" + invocationExtension + "]";
	}

}
