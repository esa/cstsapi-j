package esa.egos.csts.api.operations.impl;

import b1.ccsds.csts.common.operations.pdus.StopInvocation;
import b1.ccsds.csts.common.operations.pdus.StopReturn;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IStop;

/**
 * This class represents a STOP operation.
 */
public class Stop extends AbstractConfirmedOperation implements IStop {

	private static final OperationType TYPE = OperationType.STOP;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of a STOP operation.
	 */
	public Stop() {
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
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		sb.append("\nOperation                      : STOP\n");
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : true\n");
		sb.append("Diagnostic Type                : no diagnostic\n");
		sb.append("Common Diagnostics             : Invalid\n");

		return sb.toString();
	}

	@Override
	public StopInvocation encodeStopInvocation() {
		StopInvocation stopInvocation = new StopInvocation();
		stopInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		stopInvocation.setStopInvocationExtension(invocationExtension.encode());
		return stopInvocation;
	}

	@Override
	public StopReturn encodeStopReturn() {
		return encodeStandardReturnHeader(StopReturn.class);
	}

	@Override
	public void decodeStopInvocation(StopInvocation stopInvocation) {
		decodeStandardInvocationHeader(stopInvocation.getStandardInvocationHeader());
		invocationExtension = Extension.decode(stopInvocation.getStopInvocationExtension());
	}

	@Override
	public void decodeStopReturn(StopReturn stopReturn) {
		decodeStandardReturnHeader(stopReturn);
	}

	@Override
	public String toString() {
		return "Stop [invocationExtension=" + invocationExtension + "]";
	}

}
