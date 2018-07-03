package esa.egos.proxy;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.proxy.enums.AssocState;

/**
 * The interface allows a client to pass SLE Operation Invocations and Returns
 * to an association in the Proxy for transmission to the peer system. The
 * association accepts any operation that is valid for the given service type -
 * independent of the service instance state and whether the clients acts as a
 * SLE service user or provider. The only checks applied are related to the
 * state of the association. For a description of the associated state table of
 * an association see chapter 5.
 * 
 * @version: 1.0, October 2015
 */
public interface ISrvProxyInitiate
{
    /**
     * Initiates operation invoke.
     * 
     * @param poperation operation as parameter
     * @param reportTransmission report transmission
     * @param seqCount sequence counter
     * @throws SleApiException
     */
    void initiateOpInvoke(IOperation poperation, boolean reportTransmission, long seqCount) throws ApiException;

    /**
     * Initiates operation return.
     * 
     * @param poperation operation as parameter
     * @param report report
     * @param seqCount sequence counter
     * @throws SleApiException
     */
    void initiateOpReturn(IConfirmedOperation poperation, boolean report, long seqCount) throws ApiException;

    /**
     * Discards the buffer.
     * 
     * @throws SleApiException
     */
    void discardBuffer() throws ApiException;

    /**
     * Get the State.
     * 
     * @return
     */
    AssocState getAssocState();
}
