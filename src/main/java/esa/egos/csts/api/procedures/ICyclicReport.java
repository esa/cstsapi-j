package esa.egos.csts.api.procedures;

import java.util.List;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostic;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;

public interface ICyclicReport extends IUnbufferedDataDelivery {

	ListOfParameters getListOfParameters();
	
	void setListOfParameters(ListOfParameters listOfParameters);

	long getDeliveryCycle();

	void setDeliveryCycle(long deliveryCycle);

	List<QualifiedParameter> getQualifiedParameters();

	CyclicReportStartDiagnostic getStartDiagnostic();

	IntegerConfigurationParameter getMinimumAllowedDeliveryCycle();

	LabelLists getLabelLists();

}
