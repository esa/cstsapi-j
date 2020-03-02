/**
 * @(#) EE_APIPX_RespondingAssoc.java
 */

package esa.egos.proxy.spl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.BindDiagnostic;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.OperationResult;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.ILocator;
import esa.egos.proxy.ISrvProxyInform;
import esa.egos.proxy.ISrvProxyInitiate;
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
import esa.egos.proxy.xml.ConfigServiceType;


/**
 * The class EE_APIPX_RespondingAssoc implements those aspects of an association
 * that are specific to responding associations. It is responsible for : -
 * processing of BIND and UNBIND invocations received from the peer application.
 * - location of the service instance using ISLE_Locator after reception of a
 * BIND invocation. - access control for BIND PDU's as specified for responding
 * associations. - authentication if required. - generation of credentials and
 * version handling. - initiate TCP connection release after the transmission of
 * an UNBIND return. Responding asociations are created when a bind operation is
 * received from TML, and they are deleted when the UNBIND procedure is
 * complete, or when PEER-ABORT occurs. Only functionalities specific to
 * responding associations are implemented in this class. For common
 * functionalities, methods of the base class will be called.
 */
public class RespondingAssoc extends Association
{

    private static final Logger LOG = Logger.getLogger(RespondingAssoc.class.getName());

    /**
     * Creator of the Responding Association. The reference to the
     * ChannelInitiate, and the reference to the reporter and to the database
     * are given as parameter.
     */
    public RespondingAssoc(IChannelInitiate pChannelInitiate,
                                    ProxyConfig config,
                                    IReporter preporter,
                                    IApi api,
                                    ProxyAdmin pproxy)
    {
    	super();
        this.reporter = preporter;
        this.config = config;
        if (config != null)
        {
            this.queueSize = this.config.getTransmissionQueueSize();
        }
        setApi(api);
        this.channelInitiate = pChannelInitiate;
        this.role = BindRole.BR_responder;
        this.suspendXmit = false;

        if (this.channelInitiate != null)
        {
            // set the channel inform
            IChannelInform pChannelInform =(IChannelInform) this;
            this.channelInitiate.setChannelInform(pChannelInform);
            this.channelInitiate.configure(preporter, config);
        }
        this.proxy = pproxy;
    }

    /**
     * Reception of a BIND Invoke from the peer proxy. If the state is unbound :
     * - checks if the responder identifier is registered. - locates the
     * initiator. - assigns the authentication mode and security attributes. -
     * sets the receiving sequence counter to 1. - forwards the BIND pdu to the
     * ISLE_SrvProxyInform interface. Otherwise, aborts the association, and
     * deletes it. S_OK The processing is complete. SLE_E_PROTOCOL The operation
     * cannot be accepted in the current state. SLE_E_UNBINDING The pdu can no
     * onger be accepted because an unbinding operation has already been
     * initialized. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvBindInvoke(IOperation poperation)
    {
        if (getState() != AssocState.sleAST_unbound)
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvBindInvoke, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
            return Result.SLE_E_PROTOCOL;
        }

        // get a bind operation
        IBind pBind = null;
        BindDiagnostic diag = BindDiagnostic.INVALID;
        boolean error = false;
        boolean doAlarm = false;

        pBind = (IBind) poperation;
        if (pBind != null)
        {
        	ArrayList<RemotePeer> pPeerAppliDataList = getApi().getPeerList();
        	RemotePeer pPeerApplData = null;
        	for (RemotePeer peer : pPeerAppliDataList){
        		
        		if (peer.getId().equals(pBind.getInitiatorIdentifier()))
        			pPeerApplData = peer;
        		
        	}
            
            if (pPeerApplData == null)
            {
                diag = BindDiagnostic.ACCESS_DENIED;
                error = true;
                doAlarm = true;
            }
            else
            {
                // set the authentication mode and the security attributes
                setSecurityAttributes(pBind.getInitiatorIdentifier());
                // authenticate the pdu
                if (!authenticate(poperation, true))
                {
                    diag = BindDiagnostic.ACCESS_DENIED;
                    error = true;
                    doAlarm = true;
                    IServiceInstanceIdentifier psii = pBind.getServiceInstanceIdentifier();

                    // generate an authentication alarm
                    String tmp = pBind.print(512);
                    String mess = "Operation rejected. " + AlarmLevel.sleAL_authFailure.toString() + tmp;
                    notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_authFailure, 1002, mess, psii);

                    if (getState() == AssocState.sleAST_unbound)
                    {
                        // reset the connection
                        this.objMutex.unlock();
                        this.channelInitiate.sendReset();
                        this.objMutex.lock();
                    }
                    else
                    {
                        // abort the connection
                        doAbort(PeerAbortDiagnostics.OTHER_REASON, AbortOriginator.INTERNAL, true);
                    }

                    // cleanup
                    discardAllInvocationPdu();
                    clearAllPendingReturn();

                    changeState(SPLEvent.PXSPL_rcvBindInvoke, AssocState.sleAST_unbound);
                    this.unboundStateIsDisconnected = true;

                    // delete the association
                    releaseAssociation();

                    return Result.E_FAIL;
                }

                // check if the service type is supported
                ArrayList<ConfigServiceType> pSrvTypeList = this.config.getServiceTypeList();
                ConfigServiceType pSrvType = null;
                for(ConfigServiceType serverType: pSrvTypeList){
                	if(serverType.getServiceId().equals(Arrays.toString(pBind.getServiceType().getOid().toArray())))
                		pSrvType = serverType;
                }
                if (pSrvType == null)
                {
                    diag = BindDiagnostic.SERVICE_TYPE_NOT_SUPPORTED;
                    error = true;
                }
                else
                {
                    // set the version number
                    this.version = pBind.getVersionNumber();
                    // check if the version is supported
                    int indexMax = pSrvType.getServiceVersion().size();
                    int versionNumber = -1;
                    int versionNumberBind = pBind.getVersionNumber();
                    boolean found = false;

                    for (int index = 0; index < indexMax; index++)
                    {
                        versionNumber = pSrvType.getServiceVersion().get(index);
                        if (versionNumber != -1)
                        {
                            if (versionNumber == versionNumberBind)
                            {
                                found = true;
                                break;
                            }
                            else if (versionNumber > versionNumberBind)
                            {
                                break;
                            }
                        }
                    }

                    if (!found)
                    {
                        diag = BindDiagnostic.VERSION_NOT_SUPPORTED;
                        error = true;
                    }
                    else
                    {
                        ISrvProxyInform pSrvInform = null;
                        ISrvProxyInitiate pSrvInitiate = (ISrvProxyInitiate) this;
                        if (pSrvInitiate != null)
                        {
                            // try t locate the service instance
                            ILocator pLocator = this.proxy.getLocator();
                            long seqCount = incSeqCounter();
                            this.objMutex.unlock();
                            try
                            {
                                pSrvInform = pLocator.locateInstance(pSrvInitiate, pBind);
                                // set the proxy inform interface
                                setSrvProxyInform(pSrvInform);
                                // set the state to be pend
                                changeState(SPLEvent.PXSPL_rcvBindInvoke, AssocState.sleAST_bindPending);
                                // send the bind invoke to the service instance
                                try
                                {
                                    pSrvInform.informOpInvoke(pBind, seqCount);
                                }
                                catch (ApiException e)
                                {
                                    LOG.log(Level.FINE, "CstsApiException ", e);
                                }
                            }
                            catch (ApiException e)
                            {
                                // the bind diag is set by the locator
                                diag = pBind.getBindDiagnostic();
                                error = true;
                            }
                            finally
                            {
                                this.objMutex.lock();
                            }
                        }
                    }
                }
            }

            if (error)
            {
                String tmp = pBind.print(512);
                String mess = "Operation rejected. " + diag.toString() + " " + tmp;
                IServiceInstanceIdentifier psii = pBind.getServiceInstanceIdentifier();

                if (doAlarm)
                {
                    notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_authFailure, 1002, mess, psii);
                }
                else
                {
                    LOG.finer(mess);
                }

                // send a bind return operation without credentials
                pBind.setBindDiagnostic(diag);
                pBind.setResponderIdentifier(config.getLocalId());
                
                // send the unbind return to the network
                clientPostProcessing(pBind, false, false, true);

                tmp = poperation.print(512);
                mess = "Proxy sends a negative Bind Return. " + tmp;
                LOG.fine(mess);

                // don't disconnect the connection in the responder side
                // cleanup
                discardAllInvocationPdu();
                clearAllPendingReturn();
                changeState(SPLEvent.PXSPL_rcvBindInvoke, AssocState.sleAST_unbound);
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

        return Result.S_OK;
    }

    /**
     * Reception of a BIND Return from the peer proxy. Not authorized in the
     * responder role : calls the abort methods of the association, and deletes
     * it. S_OK The processing is complete. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvBindReturn(IOperation poperation)
    {
        if (getState() == AssocState.sleAST_unbound)
        {
            return Result.E_FAIL;
        }
        else
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvBindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
            return Result.SLE_E_PROTOCOL;
        }
    }

    /**
     * Receives a UNBIND Invoke pdu from the peer proxy. If the state is bound :
     * - discards all the operation of the sending queue. - forwards the UNBIND
     * operation to the local client. - increments the receiving sequence
     * counter. Otherwise, aborts the association, and deletes it. S_OK The
     * processing is complete. SLE_E_PROTOCOL The operation cannot be accepted
     * in the current state. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvUnbindInvoke(IOperation poperation)
    {
        if (getState() == AssocState.sleAST_unbound)
        {
            return Result.E_FAIL;
        }
        else if (getState() == AssocState.sleAST_bound)
        {
            // discard all operation in the sending queue
            discardAllInvocationPdu();
            // send the unbind invoke to the service instance
            changeState(SPLEvent.PXSPL_rcvUnbindInvoke, AssocState.sleAST_remoteUnbindPending);
            long seqCount = incSeqCounter();
            this.objMutex.unlock();
            try
            {
                getSrvProxyInform().informOpInvoke(poperation, seqCount);
            }
            catch (ApiException e)
            {
                LOG.log(Level.FINE, "CstsApiException ", e);
            }
            finally
            {
                this.objMutex.lock();
            }
        }
        else
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvUnbindInvoke, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
            return Result.SLE_E_PROTOCOL;
        }

        return Result.S_OK;
    }

    /**
     * Reception of a UNBIND Return from the peer proxy. Not authorized in the
     * responder role : calls the abort methods of the association, and deletes
     * it. S_OK The processing is complete. E_FAIL The processing has failed.
     */
    @Override
    public Result rcvUnbindReturn(IOperation poperation)
    {
        if (getState() == AssocState.sleAST_unbound)
        {
            return Result.E_FAIL;
        }
        else
        {
            doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
            changeState(SPLEvent.PXSPL_rcvUnbindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            releaseAssociation();
            return Result.SLE_E_PROTOCOL;
        }
    }

    /**
     * Receives a PEER_ABORT request from the Channel. If the originator is not
     * local : - calls the rcvPeer_Abort of the base class (Association). -
     * calls the releaseAssociation of the base class (Association).
     */
    @Override
    public void rcvPeerAbort(int diagnostic, boolean originatorIsLocal)
    {
        this.objMutex.lock();
        // cleanup
        discardAllInvocationPdu();
        clearAllPendingReturn();
        if (getState() != AssocState.sleAST_unbound)
        {
            super.rcvPeerAbort(diagnostic, originatorIsLocal);
        }
        changeState(SPLEvent.PXSPL_rcvPeerAbort, AssocState.sleAST_unbound);
        this.unboundStateIsDisconnected = true;
        releaseAssociation();
        this.objMutex.unlock();
    }

    /**
     * Receives a PROTOCOL_ABORT request from the Channel : - calls the
     * rcvProtocol_Abort of the base class (Association). - calls the
     * releaseAssociation of the base class (Association) .
     */
    @Override
    public void rcvProtocolAbort(ISP1ProtocolAbortDiagnostics diagnostic)
    {
        this.objMutex.lock();
        super.rcvProtocolAbort(diagnostic);
        releaseAssociation();
        this.objMutex.unlock();
    }

    /**
     * Receives a BIND request from the local client. For a responding
     * association, this is a protocol error. Returns the appropriate error
     * code. See specification of IServiceInitiate.
     */
    @Override
    public Result initiateBindInvoke(IOperation poperation, boolean reportTransmission)
    {
        return Result.SLE_E_PROTOCOL;
    }

    /**
     * Receives a UNBIND request from the local client. For a responding
     * association, this is a protocol error. Returns the appropriate error
     * code. See specification of IServiceInitiate.
     */
    @Override
    public Result initiateUnbindInvoke(IOperation poperation, boolean report)
    {
        return Result.SLE_E_PROTOCOL;
    }

    /**
     * Receives a BIND Return from the local client : - forwards the BIND Return
     * to the peer-proxy. - if the result of the Bind Return is negative,
     * terminates the connection, discards the PDU's and the pending return
     * operations, and deletes the association. See specification of
     * IServiceInitiate.
     */
    @Override
    public Result initiateBindReturn(IConfirmedOperation poperation, boolean report)
    {
        Result res = Result.E_FAIL;

        if (getState() != AssocState.sleAST_bindPending)
        {
            return Result.SLE_E_PROTOCOL;
        }

        OperationResult result = poperation.getResult();

        IBind pBind = (IBind) poperation;
        if (pBind != null)
        {
            // set the responder idenifier
            pBind.setResponderIdentifier(this.config.getLocalId());
        }

        if (result == OperationResult.POSITIVE)
        {
            // set the state to bound
            changeState(SPLEvent.PXSPL_initiateBindReturn, AssocState.sleAST_bound);
            // send the bind return to the network
            res = clientPostProcessing(poperation, report, false, false);
        }
        else if (result == OperationResult.NEGATIVE)
        {
            // send the bind return to the network
            res = clientPostProcessing(poperation, report, false, true);
            changeState(SPLEvent.PXSPL_initiateBindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;
            // cleanup
            discardAllInvocationPdu();
            clearAllPendingReturn();
            // delete the association
            releaseAssociation();
            return Result.S_OK;
        }

        if (res == Result.E_FAIL || res == Result.SLE_E_COMMS)
        {
            // terminate the connection
            this.objMutex.unlock();
            this.channelInitiate.sendDisconnect();
            this.objMutex.lock();

            changeState(SPLEvent.PXSPL_initiateBindReturn, AssocState.sleAST_unbound);
            this.unboundStateIsDisconnected = true;

            // cleanup
            discardAllInvocationPdu();
            clearAllPendingReturn();

            // delete the association
            releaseAssociation();
        }

        return res;
    }

    /**
     * Receives a UNBIND Return from the local client : - forwards the UNBIND
     * Return to the peer-proxy. - terminates the connection, discards the PDU's
     * and the pending return operations, and deletes the association. See
     * specification of IServiceInitiate.
     */
    @Override
    public Result initiateUnbindReturn(IConfirmedOperation poperation, boolean report)
    {
        if (getState() != AssocState.sleAST_remoteUnbindPending)
        {
            return Result.SLE_E_PROTOCOL;
        }

        // send the unbind return to the network
        Result res = clientPostProcessing(poperation, report, false, true);

        // don't terminate the connection, done by TML
        changeState(SPLEvent.PXSPL_initiateUnbindReturn, AssocState.sleAST_unbound);
        this.unboundStateIsDisconnected = true;

        this.objMutex.unlock();
        this.oPSequencer.reset(Result.SLE_E_UNBINDING);
        this.objMutex.lock();

        // cleanup
        discardAllInvocationPdu();
        clearAllPendingReturn();

        // delete the association
        releaseAssociation();

        return res;
    }

    /**
     * Receives a PEER_ABORT request from the local client, or the proxy invokes
     * the PEER-ABORT itself : - calls the initiatePeer_Abort of the base class
     * (Association). - calls the releaseAssociation of the base class
     * (Association). See specification of IServiceInitiate.
     */
    @Override
    public Result initiatePeerAbort(IPeerAbort pPeerAbort, boolean report)
    {
        super.initiatePeerAbort(pPeerAbort, report);

        changeState(SPLEvent.PXSPL_initiatePeerAbort, AssocState.sleAST_unbound);
        this.unboundStateIsDisconnected = true;
        releaseAssociation();
        return Result.S_OK;
    }

}
