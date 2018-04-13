package esa.egos.csts.api.procedures;

import java.util.List;

import esa.egos.csts.api.enums.CyclicReportStartDiagnostic;
import esa.egos.csts.api.parameters.IListOfParameters;
import esa.egos.csts.api.parameters.IListOfParametersDiagnostics;
import esa.egos.csts.api.parameters.IQualifiedParameter;

public interface ICyclicReport extends IUnbufferedDataDelivery {

	IListOfParameters getListOfParameters();
	
	void setListOfParameters(IListOfParameters listOfParameters);

	void setDeliveryCycle(long deliveryCycle);

	long getDeliveryCycle();

	List<IQualifiedParameter> getQualifiedParameters();

	void setListOfParametersDiagnostics(IListOfParametersDiagnostics listOfParametersDiagnostics);

	IListOfParametersDiagnostics getListOfParametersDiagnostics();

	void setCyclicReportStartDiagnostic(CyclicReportStartDiagnostic cyclicReportStartDiagnostic);

	CyclicReportStartDiagnostic getCyclicReportStartDiagnostic();

}
