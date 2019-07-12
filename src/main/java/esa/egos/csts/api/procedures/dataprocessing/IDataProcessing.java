package esa.egos.csts.api.procedures.dataprocessing;

import esa.egos.csts.api.enumerations.ProcessingStatus;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.IStatefulProcedure;
import esa.egos.csts.api.types.Time;

/**
 * This interface represents the Data Processing Procedure.
 */
public interface IDataProcessing extends IStatefulProcedure {

	/**
	 * Returns the input queue size configuration parameter.
	 * @return the input queue size configuration parameter
	 */
	IntegerConfigurationParameter getInputQueueSize();
	
	/**
	 * Fetches a PROCESS-DATA operation from the queue and returns it.
	 * 
	 * This method is called by the provider.
	 * 
	 * @return the fetches PROCESS-DATA operation
	 */
	IProcessData fetchAndProcess();

	/**
	 * Tries to fetch a PROCESS-DATA operation from the queue, blocks until one is
	 * available and returns it.
	 * 
	 * This method is called by the provider.
	 * 
	 * @return the fetches PROCESS-DATA operation
	 */
	IProcessData fetchAndProcessBlocking() throws InterruptedException;

	/**
	 * Indicates that the PROCESS-DATA operation has been processed. If a report was
	 * requested, it is generated and passed to the underlying state machine.
	 * 
	 * This method is called by the provider.
	 * 
	 * @param processData the PROCESS-DATA operation which has been processed
	 */
	void completeProcessing(IProcessData processData);

	Time getLastSuccessfullyProcessDataUnitTime();

	long getLastSuccessfullyProcessDataUnitId();

	ProcessingStatus getLastProcessedDataUnitStatus();

	long getLastProcessedDataUnitId();

	Time getLastProcessedDataUnitTime();

}
