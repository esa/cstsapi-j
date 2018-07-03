package esa.egos.proxy;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.proxy.del.ITranslator;

/**
 * The interface is provided to the Proxy for transfer of operation invocations
 * and returns on a single association. In addition, it provides a method to
 * signal transfer of a PDU if that has been requested via the complementary
 * interface ISLE_SrvProxyInitiate. The PDUs passed via this interface are
 * generally unchecked. The only checks performed by the Proxy are that the PDU
 * is supported by the service type and is properly coded. Reception of an
 * invalid PDU via this interface shall not cause the function to be rejected.
 * The provider of the interface must either generate the appropriate operation
 * return or abort the association. Calls to this interface shall only be
 * rejected when the client misbehaves. For instance, passing of an invocation
 * other than BIND in the state unbound is such an error.
 * 
 * @version: 1.0, October 2015
 */
public interface ISrvProxyInform
{
    /**
     * Inform operation invoke.
     * 
     * @param poperation
     * @param seqCount
     * @throws SleApiException
     */
    void informOpInvoke(IOperation poperation, long seqCount) throws ApiException;

    /**
     * Inform operation return.
     * 
     * @param poperation
     * @param seqCount
     * @throws SleApiException
     */
    void informOpReturn(IConfirmedOperation poperation, long seqCount) throws ApiException;

    /**
     * Pdu transmitted.
     * 
     * @param poperation
     * @throws SleApiException
     */
    void pduTransmitted(IOperation poperation) throws ApiException;

    /**
     * Protocol abort.
     * 
     * @param diagnostic
     * @throws SleApiException
     */
    void protocolAbort(byte[] diagnostic) throws ApiException;
    
	ITranslator getTranslator();
}
