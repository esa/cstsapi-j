package esa.egos.csts.api.operations.impl;

import ccsds.csts.common.operations.pdus.StopInvocation;
import ccsds.csts.common.operations.pdus.StopReturn;
import ccsds.csts.common.types.Extended;
import ccsds.csts.common.types.InvokeId;
import ccsds.csts.common.types.StandardReturnHeader.Result;
import ccsds.csts.common.types.StandardReturnHeader.Result.Negative;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.util.impl.ExtensionUtils;

public class Stop extends AbstractConfirmedOperation implements IStop {

	private final static int versionNumber = 1;

	public Stop() {
		super(versionNumber);
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
		return encodeStopInvocation(ExtensionUtils.nonUsedExtension());
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
	public StopReturn encodePositiveStopReturn(Extended positiveResultExtension) {

		StopReturn header = new StopReturn();
		header.setInvokeId(new InvokeId(getInvokeId()));

		if (getPerformerCredentials() != null) {
			header.setPerformerCredentials(getPerformerCredentials().asCredentials());
		} else {
			header.setPerformerCredentials(getEmptyCredentials());
		}

		Result result = new Result();
		result.setPositive(positiveResultExtension);
		header.setResult(result);

		return header;
	}
	
	@Override
	public StopReturn encodeNegativeStopReturn(Extended negativeResultExtension) {

		StopReturn header = new StopReturn();
		header.setInvokeId(new InvokeId(getInvokeId()));

		if (getPerformerCredentials() != null) {
			header.setPerformerCredentials(getPerformerCredentials().asCredentials());
		} else {
			header.setPerformerCredentials(getEmptyCredentials());
		}

		Result result = new Result();
		Negative negative = new Negative();
		encodeDiagnosticExtension();
		negative.setDiagnostic(getDiagnostic().encode());
		negative.setNegExtension(negativeResultExtension);
		result.setNegative(negative);
		header.setResult(result);

		return header;
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
