package esa.egos.csts.test.bdd;

import esa.egos.csts.api.operations.INotify;

/**
 * Interface to receive data  
 */
public interface DataReceiver {
	
	/**
	 * Called to process received data 
	 */
	public void dataReceived(byte[] data);
	
	/**
	 * Called to pass INotify operations 
	 */
	public void notifyReceived(INotify notify);
}
