package esa.egos.csts.api.operations.impl.b2;

import java.util.Arrays;

import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerType;

import b2.ccsds.csts.common.operations.pdus.ProcessDataInvocation;
import b2.ccsds.csts.common.types.AbstractChoice;
import b2.ccsds.csts.common.types.DataUnitId;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.IProcessData;

/**
 * This class represents a PROCESS-DATA (unconfirmed) operation.
 */
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
	public BerType encodeProcessDataInvocation() {
		ProcessDataInvocation processDataInvocation = new ProcessDataInvocation();
		processDataInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		processDataInvocation.setDataUnitId(new DataUnitId(dataUnitID));
		AbstractChoice choice = new AbstractChoice();
		if (data != null) {
			choice.setOpaqueString(new BerOctetString(data));
		} else if (embeddedData != null) {
			choice.setExtendedData(embeddedData.encode(new b2.ccsds.csts.common.types.Embedded()));
		}
		processDataInvocation.setData(choice);
		processDataInvocation.setProcessDataInvocationExtension(invocationExtension.encode(
				new b2.ccsds.csts.common.types.Extended() ));
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
	public String toString() {
		return "ProcessData [dataUnitID=" + dataUnitID + ", data=" + Arrays.toString(data) + ", embeddedData="
				+ embeddedData + ", invocationExtension=" + invocationExtension + "]";
	}

}
