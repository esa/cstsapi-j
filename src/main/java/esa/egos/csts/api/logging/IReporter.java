package esa.egos.csts.api.logging;

import esa.egos.csts.api.enums.Component;
import esa.egos.csts.api.enums.AlarmLevel;
import esa.egos.csts.api.procedures.IProcedureInstanceIdentifier;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;

public interface IReporter {
	
	void logRecord(IServiceInstanceIdentifier sii, IProcedureInstanceIdentifier procedureIdentifier, Component component, AlarmLevel alarm, CstsLogMessageType type, String message);
	
	void notifyApplication(IServiceInstanceIdentifier sii, CstsLogMessageType type, String message);

}
