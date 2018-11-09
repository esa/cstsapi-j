package esa.egos.csts.test.mdslite;

import java.util.Arrays;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.serviceinstance.IServiceInform;
import esa.egos.proxy.del.ITranslator;

public abstract class AbstractMonitoredDataServiceLite implements IServiceInform {

	@Override
	public void pduTransmitted(IOperation poperation) throws ApiException {
		// ignore
	}

	@Override
	public void protocolAbort(byte[] diagnostic) throws ApiException {
		System.out.println(Arrays.toString(diagnostic));
	}

	@Override
	public ITranslator getTranslator() {
		// ignore
		return null;
	}

	@Override
	public void resumeDataTransfer() {
		// ignore
	}

	@Override
	public void provisionPeriodEnds() {
		// ignore
	}

}