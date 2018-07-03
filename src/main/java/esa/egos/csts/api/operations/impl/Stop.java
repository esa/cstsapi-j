package esa.egos.csts.api.operations.impl;

import ccsds.csts.common.operations.pdus.StopInvocation;
import ccsds.csts.common.operations.pdus.StopReturn;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.util.impl.CSTSUtils;

public class Stop extends AbstractConfirmedOperation implements IStop {

	private final OperationType type = OperationType.STOP;
	
	private final static int versionNumber = 1;

	public Stop() {
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
	public String print(int i) {
		return "Stop []";
	}

	@Override
	public StopInvocation encodeStopInvocation() {
		return encodeStopInvocation(CSTSUtils.nonUsedExtension());
	}
	
	@Override
	public StopInvocation encodeStopInvocation(Extended extension) {
		StopInvocation stopInvocation = new StopInvocation();
		stopInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		stopInvocation.setStopInvocationExtension(extension);
		return stopInvocation;
	}

	@Override
	public StopReturn encodeStopReturn() {
		return encodeStandardReturnHeader(StopReturn.class);
	}
	
	@Override
	public StopReturn encodeStopReturn(Extended extended) {
		return encodeStandardReturnHeader(StopReturn.class, extended);
	}
	
	@Override
	public void decodeStopInvocation(StopInvocation stopInvocation) {
		decodeStandardInvocationHeader(stopInvocation.getStandardInvocationHeader());
	}

	@Override
	public void decodeStopReturn(StopReturn stopReturn) {
		decodeStandardReturnHeader(stopReturn);
	}

}
