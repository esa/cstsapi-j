package esa.egos.csts.api.proxy.del;

import java.io.IOException;

import ccsds.csts.association.control.types.PeerAbortDiagnostic;
import esa.egos.csts.api.enums.AbortOriginator;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.util.impl.Reference;

public interface ITranslator {

	void initialise(IServiceInstance serviceInstance) throws ApiException;
	
	IOperation decode(byte[] data, Reference<Boolean> isInvoke) throws ApiException, IOException;
	
	/**
	 * Only for decoding of the PeerAbort.
	 * @param peerabortDiag
	 * @param abortOriginator
	 * @return
	 */
	IOperation decode(int diagnostic, PeerAbortDiagnostic peerAbortDiagnostic, AbortOriginator abortOriginator) throws ApiException;

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
