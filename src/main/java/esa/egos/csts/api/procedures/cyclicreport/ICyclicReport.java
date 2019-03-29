package esa.egos.csts.api.procedures.cyclicreport;

import java.util.List;

import esa.egos.csts.api.diagnostics.CyclicReportStartDiagnostics;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.parameters.impl.LabelLists;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.unbuffereddatadelivery.IUnbufferedDataDelivery;

/**
 * This interface represents the Cyclic Report Procedure.
 */
public interface ICyclicReport extends IUnbufferedDataDelivery {

	/**
	 * Returns the list of Parameters.
	 * 
	 * This method returns a valid list of Parameters only after the provider has
	 * received a valid request from the user.
	 * 
	 * @return the list of Parameters
	 */
	ListOfParameters getListOfParameters();

	/**
	 * Sets the list of Parameters.
	 * 
	 * @param listOfParameters the list of Parameters
	 */
	void setListOfParameters(ListOfParameters listOfParameters);

	/**
	 * Returns the delivery cycle.
	 * 
	 * This method returns a valid delivery cycle only after the provider has
	 * received a valid request from the user.
	 * 
	 * @return the delivery cycle
	 */
	long getDeliveryCycle();

	/**
	 * Sets the delivery cycle.
	 * 
	 * @param deliveryCycle the delivery cycle
	 */
	void setDeliveryCycle(long deliveryCycle);

	/**
	 * Returns the list of Qualified Parameters.
	 * 
	 * This method returns a valid list of Qualified Parameters only after the user
	 * has received a positive return from the provider.
	 * 
	 * @return the list of Qualified Parameters
	 */
	List<QualifiedParameter> getQualifiedParameters();

	/**
	 * Returns the Cyclic Report Start Diagnostics.
	 * 
	 * This method returns valid Cyclic Report Start Diagnostics only after the user
	 * has received a negative return from the provider.
	 * 
	 * @return the list of Qualified Parameters
	 */
	CyclicReportStartDiagnostics getStartDiagnostic();

	/**
	 * Returns the minimum allowed delivery cycle.
	 * 
	 * This method is only suitable for the provider.
	 * 
	 * @return the current minimum allowed delivery cycle
	 */
	IntegerConfigurationParameter getMinimumAllowedDeliveryCycle();

	/**
	 * Returns the current Label lists.
	 * 
	 * This method is only suitable for the provider.
	 * 
	 * @return the current Label lists
	 */
	LabelLists getLabelLists();

	/**
	 * Creates a START operation and forwards it to the underlying communications
	 * service, requesting the start of the cyclic report.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestCyclicReport(long deliveryCycle, ListOfParameters listOfParameters);

	/**
	 * Creates a STOP operation and forwards it to the underlying communications
	 * service, requesting the end of the cyclic report.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult endCyclicReport();

}
