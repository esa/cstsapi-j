package esa.egos.csts.rtn.cfdp.procedures;

import esa.egos.csts.api.exceptions.ApiException;

/**
 *  CFDP PDU PDU Delivery Procedure Provider Interface
 */
public interface ICfdpPduDeliveryProvider extends ICfdpPduDelivery {

	/**
	 * Provides the operation mode of that procedure
	 * @return the configured operation mode
	 * @throws ApiException 
	 */
	public CfdpOperationMode getOperationMode() throws ApiException;

	/**
	 * Configures the operation mode of the procedure
	 * @param mode The operation mode to use
	 * @throws ApiException 
	 */
	public void initializeOperationMode(CfdpOperationMode mode) throws ApiException;
	
	/**
	 * Provides the configured CFDP destination entities
	 * @return an integer array of configured CFDP destination entities
	 * @throws ApiException 
	 */
	public long[] getCfdpDestEntities() throws ApiException; 
	
	/**
	 * Configures the destination entities. Only CFDP PDUs
	 * contained in the provided array of CFDP entity IDs 
	 * will be forwarded.
	 * If no CFDP destination entities are provided, all
	 * CFDP PDUs will be forwarded
	 * @param cfdpDestEnties
	 * @throws ApiException 
	 */
	public void initializeCfdpDestEntities(long[] cfdpDestEnties) throws ApiException;
}
