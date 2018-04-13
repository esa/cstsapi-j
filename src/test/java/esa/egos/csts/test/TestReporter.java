package esa.egos.csts.test;

import esa.egos.csts.api.enums.AlarmLevel;
import esa.egos.csts.api.enums.Component;
import esa.egos.csts.api.logging.CstsLogMessageType;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public class TestReporter implements IReporter {

	@Override
	public void notifyApplication(IServiceInstanceIdentifier sii,
			CstsLogMessageType type, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logRecord(IServiceInstanceIdentifier sii,
			IProcedureInstanceIdentifier procedureIdentifier,
			Component component, AlarmLevel alarm, CstsLogMessageType type,
			String message) {
		// TODO Auto-generated method stub
		
	}

}
