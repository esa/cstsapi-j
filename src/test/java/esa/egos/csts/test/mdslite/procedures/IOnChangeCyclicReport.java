package esa.egos.csts.test.mdslite.procedures;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.ICyclicReport;

public interface IOnChangeCyclicReport extends ICyclicReport {

	/**
	 * Creates a START operation and forwards it to the underlying communications
	 * service, requesting the start of the cyclic report.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestCyclicReport(long deliveryCycle, boolean onChange, ListOfParameters listOfParameters);	
	
}
