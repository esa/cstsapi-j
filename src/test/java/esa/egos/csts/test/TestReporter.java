package esa.egos.csts.test;

import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.enums.AlarmLevel;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;

public class TestReporter implements IReporter {

	@Override
	public void notifyApplication(IServiceInstanceIdentifier sii,
			CstsLogMessageType type, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logRecord(IServiceInstanceIdentifier sii,
			ProcedureInstanceIdentifier procedureIdentifier,
			Component component, AlarmLevel alarm, CstsLogMessageType type,
			String message) {
		// TODO Auto-generated method stub
		
	}

}
