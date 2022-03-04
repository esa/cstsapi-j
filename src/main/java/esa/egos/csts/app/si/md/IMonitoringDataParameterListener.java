package esa.egos.csts.app.si.md;

import java.util.List;


import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;

public interface IMonitoringDataParameterListener {
	
	void event(ProcedureInstanceIdentifier procedureInstanceIdentifier, List<QualifiedParameter> qualifiedParamters);


}
