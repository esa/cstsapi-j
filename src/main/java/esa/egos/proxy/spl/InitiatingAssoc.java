/**
 * @(#) EE_APIPX_InitiatingAssoc.java
 */

package esa.egos.proxy.spl;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.enums.AbortOriginator;
import esa.egos.proxy.enums.AlarmLevel;
import esa.egos.proxy.enums.AssocState;
import esa.egos.proxy.enums.BindRole;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.spl.types.SPLEvent;
import esa.egos.proxy.tml.ISP1ProtocolAbortDiagnostics;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.RemotePeer;


/**
 * The class EE_APIPX_InitiatingAssoc implements those aspects of an association
 * that are specific to initiating associations. It is responsible for: -
 * establishment/release of an association to its peer application when a
 * BIND/UNBIND invocation is received from the local application, which includes
 * connect/disconnect call to the TML. - access control for BIND PDU's as
 * specified for initiating associations. - authentication if required. -
 * generation of credentials and version handling. The initiating asociation is
 * created and deleted by the Proxy. Only functionalities specific to initiating
 * associations are implemented in this class. For common functionalities,
 * methods of the base class will be called.
 */
public class InitiatingAssoc extends Association
{
    private static final Logger LOG = Logger.getLogger(InitiatingAssoc.class.getName());

    /**
     * The responder identifier. This attribute is set when a bind invoke is
     * received from the client, and checked when the bind return pdu is
     * received on the network interface.
     */
    private String responderIdentifier;

    /**
     * Constructor of the class.
     */
    public InitiatingAssoc(	IReporter preporter,
							IApi api,
                            ProxyAdmin pproxy)
    {
    	super();
        this.responderIdentifier = null;
        this.reporter = preporter;
        setApi(api);
        if (getApi() != null)
        {
            ProxyConfig pProxySettings = getApi().getProxySettings();
            this.queueSize = pProxySettings.getTransmissionQueueSize();
        }
        this.proxy = pproxy;        		
        this.role = BindRole.BR_initiator;
    }


    /**
     * Reception of a BIND from the peer proxy. Not authorized in the initiator
     * role : calls the abort methods of the association. S_OK The processing is
     * complete. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvBindInvoke(IOperation poperation)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("BIND received: " + poperation);
        }

        doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
        changeState(SPLEvent.PXSPL_rcvBindInvoke, AssocState.sleAST_unbound);
        this.unboundStateIsDisconnected = true;
        releaseAssociation();
        return Result.SLE_E_PROTOCOL;
    }

    /**
     * Reception of a BIND Return from the peer proxy. If the state is
     * "bind pend": - checks if the id is registered. - checks if the responder
     * is the expected one. - sets the receiving sequence counter to 1. -
     * forwards the BIND Return pdu to the local client. Otherwise, aborts the
     * association. S_OK The processing is complete. SLE_E_PROTOCOL The
     * operation cannot be accepted in the current state. SLE_E_UNBINDING The
     * pdu can no onger be accepted because an unbinding operation has already
     * been initialised. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvBindReturn(IOperation poperation)
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("BIND-RETURN received: " + poperation);
        }

        if (getState() == AssocState.sleAST_unbound)
        {
            return Result.E_FAIL;
        }
        else if (getState() == AssocState.sleAST_bindPending)
        {
            // get a bind operation
            PeerAbortDiagnostics diag = PeerAbortDiagnostics.INVALID;
            boolean error = false;

            IBind pBind = (IBind) poperation;
            if (pBind != null)
            {
                // check if the responder identifier is registered in the peer
                // application list
            	ArrayList<RemotePeer> pPeerAppliDataList = getApi().getPeerList();
            	RemotePeer pPeerApplData = null;
                String rspid = pBind.getResponderIdentifier();

                if (rspid != null)
                {
                	for (RemotePeer peer : pPeerAppliDataList){
                		
                		if (peer.getId().equals(rspid))
                			pPeerApplData = peer;
                		
                	}
                }

                if (pPeerApplData == null)
                {
                    diag = PeerAbortDiagnostics.ACCESS_DENIED;
                    error = true;
                }
                else
                {
                    // check if the responder is the expected one
                    if (rspid.compareTo(this.responderIdentifier) != 0)
                    {
                        diag = PeerAbortDiagnostics.UNEXPECTED_RESPONDER_ID;
                        error = true;
                    }
                    else
                    {
                        // send the bind return to the service instance
                        if (pBind.getResult() == OperationResult.POSITIVE)
                        {
                            // set the state to bound
                            changeState(SPLEvent.PXSPL_rcvBindReturn, AssocState.sleAST_bound);
                        }
                        else
                        {
                            // set the state to unbound
                            changeState(SPLEvent.PXSPL_rcvBindReturn, AssocState.sleAST_unbound);
                            this.unboundStateIsDisconnected = true;
                            // abort the connection
                            this.objMutex.unlock();
                            this.channelInitiate.sendDisconnect();
                            this.objMutex.lock();
                        }

                        long oldSeq = getSequenceCounter();
                        incSeqCounter();

                        this.objMutex.unlock();
                        try
                        {
                            if (LOG.isLoggable(Level.FINE))
                            {
                                LOG.fine("seqCount : " + oldSeq + "   " + poperation.toString());
                            }
                            getSrvProxyInform().informOpReturn(pBind, oldSeq);
                        }
                        catch (ApiException e)
                        {
                            LOG.log(Level.FINE, "SleApiException ", e);
                            return Result.E_FAIL;
                        }
                        finally
                        {
                            this.objMutex.lock();
                        }
                    }
                }

                if (error)
                {
                    // generate an alarm
                    IServiceInstanceIdentifier psii = pBind.getServiceInstanceIdentifier();
                    String tmp = pBind.print(512);
                    String mess = "Operation rejected. " + diag.toString() + tmp;
                    notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_authFailure, 1002, mess, psii);
                    // abort the association
                    doAbort(diag, AbortOriginator.INTERNAL, true);
                    changeState(SPLEvent.PXSPL_rcvBindReturn, AssocState.sleAST_unbound);
                    this.unboundStateIsDisconnected = true;
                    // delete the association
                    releaseAssociation();
                    return Result.E_FAIL;
                }
            }
            else
            {
                // query interface for bind fail!
                return Result.E_FAIL;
            }
        }
        else
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvBindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
            return Result.SLE_E_PROTOCOL;
        }

        return Result.S_OK;
    }

    /**
     * Reception of a UNBIND Invoke from the peer proxy. Not authorized in the
     * initiator role : calls the abort methods of the association. S_OK The
     * processing is complete. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvUnbindInvoke(IOperation poperation)
    {
        if (getState() == AssocState.sleAST_unbound)
        {
            return Result.E_FAIL;
        }
        else
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvUnbindInvoke, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
            return Result.SLE_E_PROTOCOL;
        }
    }

    /**
     * Reception of a UNBIND Return from the peer proxy. If the state is
     * "loc unbind pend": - discards all the operation of the sending queue. -
     * increments the receiving sequence counter. - forwards the UNBIND
     * operation to the local client. Otherwise, aborts the association. S_OK
     * The processing is complete. SLE_E_PROTOCOL The operation cannot be
     * accepted in the current state. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvUnbindReturn(IOperation poperation)
    {
        if (getState() == AssocState.sleAST_unbound)
        {
            return Result.E_FAIL;
        }
        else if (getState() == AssocState.sleAST_localUnbindPending)
        {
            // send the unbind return to the service instance
            // set the state to unbound
            changeState(SPLEvent.PXSPL_rcvUnbindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;

            this.objMutex.unlock();
            this.oPSequencer.reset(Result.SLE_E_UNBINDING);
            this.objMutex.lock();

            // cleanup
            discardAllInvocationPdu();
            clearAllPendingReturn();

            // terminate the connection
            if (this.channelInitiate != null)
            {
                this.objMutex.unlock();
                this.channelInitiate.sendDisconnect();
                this.objMutex.lock();
            }

            // send the operation to the service element after disconnect
            // otherwise, the service element can do a destroy association
            // which release the channel before the disconnect is sent
            IConfirmedOperation pConfOp = (IConfirmedOperation) poperation;
            if (pConfOp != null)
            {
                long oldSeq = getSequenceCounter();
                incSeqCounter();
                this.objMutex.unlock();
                try
                {
                    getSrvProxyInform().informOpReturn(pConfOp, oldSeq);
                }
                catch (ApiException e)
                {
                    if (LOG.isLoggable(Level.FINEST))
                    {
                        LOG.finest("received Result code: " );
                    }

                    return Result.E_FAIL;
                }
                finally
                {
                    this.objMutex.lock();
                }
            }
        }
        else
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvUnbindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
        }

        return Result.S_OK;
    }

    /**
     * Receives a PEER_ABORT request. Lock and calls the base class.
     */
    @Override
    public void rcvPeerAbort(int diagnostic, boolean originatorIsLocal)
    {

        this.innerLock.lock();
        this.objMutex.lock();

        try
        {
            super.rcvPeerAbort(diagnostic, originatorIsLocal);
        }
        finally
        {

            this.objMutex.unlock();
            this.innerLock.unlock();

        }
    }

    /**
     * Receives a PROTOCOL_ABORT request. Lock and calls the base class.
     */
    @Override
    public void rcvProtocolAbort(ISP1ProtocolAbortDiagnostics diagnostic)
    {
        this.innerLock.lock();
        this.objMutex.lock();
        try
        {
            super.rcvProtocolAbort(diagnostic);
        }
        finally
        {
            this.objMutex.unlock();
            this.innerLock.unlock();
        }
    }

    /**
     * Receives a BIND request from the local client : - checks if the responder
     * identifier is valid. - assigns the authentication mode in the
     * association. - assigns the security attributes in the association, if
     * applicable. - inserts the local application identifier in the BIND pdu. -
     * inserts the highest version number in the BIND pdu. - creates the
     * Channel, and connects it. - transmits the BIND pdu to the peer proxy. See
     * specification of IServiceInitiate.
     */
    @Override
    public Result initiateBindInvoke(IOperation poperation, boolean reportTransmission)
    {
        if (getState() != AssocState.sleAST_unbound)
        {
            return Result.SLE_E_PROTOCOL;
        }
        else
        {
            if (!this.unboundStateIsDisconnected)
            {
                // a connect has already been sent to TML
                return Result.SLE_E_PROTOCOL;
            }
        }

        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("TH" + Thread.currentThread().getId() + " is calling initAssoc()");
        }
        initAssoc();
        Result res = Result.S_OK;
        IBind pBind = (IBind) poperation;
        if (pBind != null)
        {
            String rspid = pBind.getResponderIdentifier();
            if (rspid == null)
            {
                return Result.SLE_E_INVALIDID;
            }

            // check if the responder identifier of the bind invoke is
            // registered in the peer application list
        	ArrayList<RemotePeer> pPeerAppliDataList = getApi().getPeerList();
        	RemotePeer pPeerApplData = null;
        	for (RemotePeer peer : pPeerAppliDataList){
        		
        		if (peer.getId().equals(rspid))
        			pPeerApplData = peer;
        		
        	}
            if (pPeerApplData == null)
            {
                return Result.SLE_E_INVALIDID;
            }
            else
            {
                // set the version number
                this.version = pBind.getVersionNumber();
                // assign authentication mode and security attributes to the
                // association
                setSecurityAttributes(rspid);
                // assign the responder identifier
                this.responderIdentifier = rspid;
                // insert the local application identifier in the bind operation
                ProxyConfig pLocalApplData = getApi().getProxySettings();
                pBind.setInitiatorIdentifier(pLocalApplData.getLocalId());
                this.unboundStateIsDisconnected = false;
                // connect the association
                String port = pBind.getResponderPortIdentifier();
                // release the previous channel if necessary
                releaseChannel();
                // instantiate a channel through the channel factory
                IChannelInitiate pChannelInitiate = ChannelFactory.createChannel(true, this.reporter, null);
                if (pChannelInitiate != null)
                {
                    // set the channel initiate interface in the association
                    this.channelInitiate = pChannelInitiate;
                    pChannelInitiate.configure(this.reporter, pLocalApplData);

                    // set the channelInform interface in the channel
                    IChannelInform pChannelInform = (IChannelInform) this;
                    if (pChannelInform != null)
                    {
                        pChannelInitiate.setChannelInform(pChannelInform);
                    }

                    // trace
                    String mess = "Association sends a Connect request to TML on port " + port;
                    LOG.finer(mess);

                    this.objMutex.unlock();
                    pChannelInitiate.sendConnect(port);
                    this.objMutex.lock();

                    // TODO test
                    setChannelInitiate(pChannelInitiate);
                    
                    // send the bind invoke to the network
                    // the operation is encoded and sent in the post-processing
                    res = clientPostProcessing(poperation, reportTransmission, true, false);
                }
                else
                {
                    // cannot create a channel!
                    return Result.E_FAIL;
                }
            }

        }
        else
        {
            // query interface failed!
            return Result.E_FAIL;
        }
        return res;
    }

    /**
     * Receives a UNBIND request from the local client. If the state is bound,
     * forward the UNBIND to the peer-proxy. See specification of
     * IServiceInitiate.
     */
    @Override
    public Result initiateUnbindInvoke(IOperation poperation, boolean report)
    {
        if (getState() != AssocState.sleAST_bound)
        {
            return Result.SLE_E_PROTOCOL;
        }

        // set the state to local unbind pend
        changeState(SPLEvent.PXSPL_initiateUnbindInvoke, AssocState.sleAST_localUnbindPending);

        // send the unbind invoke to the network
        Result res = clientPostProcessing(poperation, report, true, false);
        return res;
    }

    /**
     * Receives a BIND Return from the local client. For a initiating
     * association, this is a protocol error. Returns the appropriate error
     * code. See specification of IServiceInitiate.
     */
    @Override
    public Result initiateBindReturn(IConfirmedOperation poperation, boolean report)
    {
        return Result.SLE_E_PROTOCOL;
    }

    /**
     * Receives an UNBIND Return from the local client. For a initiating
     * association, this is a protocol error. Returns the appropriate error
     * code. See specification of IServiceInitiate.
     */
    @Override
    public Result initiateUnbindReturn(IConfirmedOperation poperation, boolean report)
    {
        return Result.SLE_E_PROTOCOL;
    }
}
