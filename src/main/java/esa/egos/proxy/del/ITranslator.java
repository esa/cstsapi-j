package esa.egos.proxy.del;

import java.io.IOException;

import b1.ccsds.csts.association.control.types.PeerAbortDiagnostic;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.serviceinstance.IServiceInstanceInternal;
import esa.egos.proxy.enums.AbortOriginator;
import esa.egos.proxy.util.impl.Reference;

public interface ITranslator {

	void initialise(IServiceInstanceInternal serviceInstance) throws ApiException;
	
	IOperation decode(byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException;
	
	/**
	 * Only for decoding of the PeerAbort.
	 * @param abortOriginator
	 * @return
	 */
	
	IOperation decode(int diagnostic, AbortOriginator abortOriginator) throws ApiException;

	byte[] encode(IOperation operation, boolean invoke) throws ApiException, IOException;

	/**
	 * Only for the encoding of the PeerAbort. 
	 * @param operation
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	int encode(IOperation operation) throws ApiException, IOException;

	void removeAllPendingReturns();



}
