package esa.egos.csts.app.si.md;

import esa.egos.csts.api.events.EventData;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;

public interface IMonitoringDataEventListener {
	
	void event(ProcedureInstanceIdentifier procedureInstanceIdentifier, EventData event);


}
