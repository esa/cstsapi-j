package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.procedures.dataprocessing.IDataProcessing;
import esa.egos.csts.api.types.ConditionalTime;
import esa.egos.csts.api.types.Time;

/**
 * This interface represents the Sequence-Controlled Data Processing Procedure.
 */
public interface ISequenceControlledDataProcessing extends IDataProcessing {

	/**
	 * Creates a START operation, extends it by the first data unit ID and forwards
	 * it to the underlying communications service, requesting the start of the data
	 * processing.
	 * 
	 * This method is called by the user.
	 * 
	 * @param firstDataUnitId the first data unit ID to extend
	 * @return the result of the request
	 */
	CstsResult requestDataProcessing(long firstDataUnitId);

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
	CstsResult processData(long dataUnitId, byte[] data, boolean produceReport);

	/**
	 * Creates a PROCESS-DATA operation forwards it to the underlying communications
	 * service, requesting the processing of the data.
	 * 
	 * This method is called by the user.
	 * 
	 * @param dataUnitId                 the data unit ID
	 * @param data                       the data
	 * @param earliestDataProcessingTime the earliest data processing time
	 * @param latestDataProcessingTime   the latest data processing time
	 * @return the result of the request
	 */
	CstsResult processData(long dataUnitId, byte[] data, Time earliestDataProcessingTime, Time latestDataProcessingTime, boolean produceReport);
	
	/**
	 * Creates a PROCESS-DATA operation forwards it to the underlying communications
	 * service, requesting the processing of the data.
	 * 
	 * This method is called by the user.
	 * 
	 * @param dataUnitId                 the data unit ID
	 * @param data                       the data
	 * @param earliestDataProcessingTime the earliest data processing time
	 * @param latestDataProcessingTime   the latest data processing time
	 * @return the result of the request
	 */
	@Deprecated
	CstsResult processData(long dataUnitId, byte[] data, ConditionalTime earliestDataProcessingTime, ConditionalTime latestDataProcessingTime, boolean produceReport);

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
	CstsResult processData(long dataUnitId, EmbeddedData embeddedData, boolean produceReport);

	/**
	 * Creates a PROCESS-DATA operation forwards it to the underlying communications
	 * service, requesting the processing of the data.
	 * 
	 * This method is called by the user.
	 * 
	 * @param dataUnitId                 the data unit ID
	 * @param embeddedData               the embedded data
	 * @param earliestDataProcessingTime the earliest data processing time
	 * @param latestDataProcessingTime   the latest data processing time
	 * @return the result of the request
	 */
	CstsResult processData(long dataUnitId, EmbeddedData embeddedData, Time earliestDataProcessingTime, Time latestDataProcessingTime, boolean produceReport);
	
	/**
	 * Creates a PROCESS-DATA operation forwards it to the underlying communications
	 * service, requesting the processing of the data.
	 * 
	 * This method is called by the user.
	 * 
	 * @param dataUnitId                 the data unit ID
	 * @param embeddedData               the embedded data
	 * @param earliestDataProcessingTime the earliest data processing time
	 * @param latestDataProcessingTime   the latest data processing time
	 * @return the result of the request
	 */
	@Deprecated
	CstsResult processData(long dataUnitId, EmbeddedData embeddedData, ConditionalTime earliestDataProcessingTime, ConditionalTime latestDataProcessingTime, boolean produceReport);

}
