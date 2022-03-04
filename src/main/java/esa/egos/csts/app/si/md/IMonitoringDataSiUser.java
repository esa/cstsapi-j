package esa.egos.csts.app.si.md;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.app.si.IAppSiUser;

public interface IMonitoringDataSiUser extends IAppSiUser {

	public CstsResult startCyclicReport(ProcedureInstanceIdentifier identifier, long deliveryCycle );
	
	public CstsResult startCyclicReport(ProcedureInstanceIdentifier identifier, long deliveryCycle, boolean onChange );
	
	public CstsResult startNotification(ProcedureInstanceIdentifier identifier);
	
	public CstsResult queryInformation(ProcedureInstanceIdentifier identifier, ListOfParameters listOfParameters);
	
	public CstsResult stopCyclicReport(ProcedureInstanceIdentifier identifier);
	
	public CstsResult stopNotification(ProcedureInstanceIdentifier identifier);
	
}
