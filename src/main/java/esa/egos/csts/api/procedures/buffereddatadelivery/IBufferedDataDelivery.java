package esa.egos.csts.api.procedures.buffereddatadelivery;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.DeliveryMode;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.IStatefulProcedure;
import esa.egos.csts.api.types.ConditionalTime;
import esa.egos.csts.api.types.Time;

/**
 * This interface represents the Buffered Data Delivery Procedure.
 */
public interface IBufferedDataDelivery extends IStatefulProcedure {

	/**
	 * Returns the delivery latency limit as a Configuration Parameter.
	 * 
	 * @return the delivery latency limit
	 */
	IntegerConfigurationParameter getDeliveryLatencyLimit();

	/**
	 * Returns the delivery return buffer size as a Configuration Parameter.
	 * 
	 * @return the return buffer size
	 */
	IntegerConfigurationParameter getReturnBufferSize();

	/**
	 * Returns the delivery mode as a Configuration Parameter.
	 * 
	 * @return the delivery mode
	 */
	IntegerConfigurationParameter getDeliveryModeParameter();

	/**
	 * Returns the delivery mode as its enumerated value.
	 * 
	 * @return the delivery mode
	 */
	DeliveryMode getDeliveryMode();

	/**
	 * Indicates end of data und triggers to notify the user.
	 * 
	 * This method is called by the provider.
	 * 
	 * @throws ApiException if end of data is already notified
	 */
	void notifyEndOfData() throws ApiException;

	/**
	 * Creates a START operation, sets the start and end generation time to unknown
	 * and forwards it to the underlying communications service, requesting the
	 * start of the data delivery.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestDataDelivery();

	/**
	 * Creates a START operation, extends it and forwards it to the underlying
	 * communications service, requesting the start of the data delivery.
	 * 
	 * This method is called by the user.
	 * 
	 * @param startGenerationTime the start generation time
	 * @param stopGenerationTime  the stop generation time
	 * @return the result of the request
	 */
	CstsResult requestDataDelivery(Time startGenerationTime, Time stopGenerationTime);
	
	/**
	 * Creates a START operation, extends it and forwards it to the underlying
	 * communications service, requesting the start of the data delivery.
	 * 
	 * This method is called by the user.
	 * 
	 * @param startGenerationTime the start generation time
	 * @param stopGenerationTime  the stop generation time
	 * @return the result of the request
	 */
	@Deprecated
	CstsResult requestDataDelivery(ConditionalTime startGenerationTime, ConditionalTime stopGenerationTime);

	/**
	 * Creates a STOP operation and forwards it to the underlying communications
	 * service, requesting the end of the data delivery.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult endDataDelivery();

	/**
	 * Creates a TRANSFER-DATA operation and forwards it to the underlying
	 * communications service.
	 * 
	 * This method is called by the provider.
	 * 
	 * @param data the data to be transferred
	 * @return the result of the transfer attempt
	 */
	CstsResult transferData(byte[] data);

	/**
	 * Creates a TRANSFER-DATA operation and forwards it to the underlying
	 * communications service.
	 * 
	 * This method is called by the provider.
	 * 
	 * @param embeddedData the embedded data to be transferred
	 * @return the result of the transfer attempt
	 */
	CstsResult transferData(EmbeddedData embeddedData);	
	
	/**
	 * Provides access to the start generation time
	 * @return the start generation time
	 */
	ConditionalTime getStartGenerationTime();
	
	/**
	 * Provides access to the stop generation time
	 * @return the stop generation time
	 */
	ConditionalTime getStopGenerationTime();

}
