package esa.egos.csts.api.procedures.unbuffereddatadelivery;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.procedures.IStatefulProcedure;

/**
 * This interface represents the Unbuffered Data Delivery Procedure.
 */
public interface IUnbufferedDataDelivery extends IStatefulProcedure {

	/**
	 * Creates a START operation and forwards it to the underlying communications
	 * service, requesting the start of the data delivery.
	 * 
	 * This method is called by the user.
	 * 
	 * @return the result of the request
	 */
	CstsResult requestDataDelivery();

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

}
