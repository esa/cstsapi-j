package esa.egos.proxy.logging;

import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.enums.AlarmLevel;
import esa.egos.proxy.enums.Component;

public interface IReporter {
	
	void logRecord(IServiceInstanceIdentifier sii, ProcedureInstanceIdentifier procedureIdentifier, Component component, AlarmLevel alarm, CstsLogMessageType type, String message);
	
	void notifyApplication(IServiceInstanceIdentifier sii, CstsLogMessageType type, String message);

}
