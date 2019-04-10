package esa.egos.csts.api.procedures.buffereddataprocessing;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DataTransferMode;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.dataprocessing.IDataProcessing;

/**
 * This interface represents the Buffered Data Processing Procedure.
 */
public interface IBufferedDataProcessing extends IDataProcessing {

	/**
	 * Returns the processing latency limit configuration parameter.
	 * 
	 * @return the processing latency limit configuration parameter
	 */
	IntegerConfigurationParameter getProcessingLatencyLimit();

	/**
	 * Returns the maximum forward buffer size configuration parameter.
	 * 
	 * @return the maximum forward buffer size configuration parameter
	 */
	IntegerConfigurationParameter getMaximumForwardBufferSize();

	/**
	 * Returns the data transfer mode configuration parameter.
	 * 
	 * @return the data transfer mode configuration parameter
	 */
	IntegerConfigurationParameter getDataTransferModeParameter();
	
	/**
	 * Returns the data transfer mode configuration parameter.
	 * 
	 * @return the data transfer mode configuration parameter
	 */
	DataTransferMode getDataTransferMode();
	
	/**
	 * Creates a START operation and forwards it to the underlying communications
	 * service, requesting the start of the data processing.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestDataProcessing();

	/**
	 * Creates a STOP operation and forwards it to the underlying communications
	 * service, requesting the end of the data processing.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult endDataProcessing();

	/**
	 * Creates a PROCESS-DATA operation forwards it to the underlying communications
	 * service, requesting the processing of the data.
	 * 
	 * This method is called by the user.
	 * 
	 * @param dataUnitId the data unit ID
	 * @param data       the data
	 * @return the result of the request
	 */
	CstsResult processData(long dataUnitId, byte[] data);

	/**
	 * Creates a PROCESS-DATA operation forwards it to the underlying communications
	 * service, requesting the processing of the data.
	 * 
	 * This method is called by the user.
	 * 
	 * @param dataUnitId   the data unit ID
	 * @param embeddedData the embedded data
	 * @return the result of the request
	 */
	CstsResult processData(long dataUnitId, EmbeddedData embeddedData);

}
