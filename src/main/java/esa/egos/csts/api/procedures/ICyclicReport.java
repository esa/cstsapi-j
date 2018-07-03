package esa.egos.csts.api.procedures;

import java.util.List;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostic;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.ListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;

public interface ICyclicReport extends IUnbufferedDataDelivery {

	ListOfParameters getListOfParameters();
	
	void setListOfParameters(ListOfParameters listOfParameters);

	long getDeliveryCycle();

	void setDeliveryCycle(long deliveryCycle);

	List<QualifiedParameter> getQualifiedParameters();

	ListOfParametersDiagnostics getListOfParametersDiagnostics();

	void setListOfParametersDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics);

	CyclicReportStartDiagnostic getStartDiagnostic();

	void setStartDiagnostic(CyclicReportStartDiagnostic cyclicReportStartDiagnostic);

}
