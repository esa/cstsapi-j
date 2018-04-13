/**
 * @(#) EE_APIPX_AssocPxy.java
 */

package esa.egos.csts.api.proxy.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enums.AlarmLevel;
import esa.egos.csts.api.enums.BindDiagnostics;
import esa.egos.csts.api.enums.Component;
import esa.egos.csts.api.enums.PeerAbortDiagnostics;
import esa.egos.csts.api.enums.Result;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.exception.ApiResultException;
import esa.egos.csts.api.logging.CstsLogMessageType;
import esa.egos.csts.api.logging.IReporter;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.proxy.del.ITranslator;
import esa.egos.csts.api.proxy.del.PDUTranslator;
import esa.egos.csts.api.proxy.spl.IChannelInform;
import esa.egos.csts.api.proxy.spl.IChannelInitiate;
import esa.egos.csts.api.proxy.tml.ISP1ProtocolAbortDiagnostics;
import esa.egos.csts.api.proxy.xml.AuthenticationMode;
import esa.egos.csts.api.proxy.xml.ProxyConfig;
import esa.egos.csts.api.proxy.xml.ProxyRoleEnum;
import esa.egos.csts.api.proxy.xml.RemotePeer;
import esa.egos.csts.api.proxy.xml.ServerType;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.csts.api.util.ISecAttributes;
import esa.egos.csts.api.util.IUtil;
import esa.egos.csts.api.util.impl.Reference;
import esa.egos.csts.api.util.impl.Util;


/**
 * The class is the proxy of the class EE_APISE_Association in the communication
 * server process. It forwards encoded PDU's to the association object residing
 * in the SLE application process. For incoming BIND requests, the class is
 * responsible to obtain the service instance identifier. If the identifier is
 * registered, it sends the encoded PDU to the application process, which
 * performs access control and authentication. If the service instance is not
 * registered, access control and authentication is performed (if applicable)
 * before the BIND return PDU can be sent to the initiator. The AssocPxy object
 * creates a thread in order to send a received PDU to the link object. When the
 * AssocPxy receives a PDU (rcvSLE_PDU), it returns immediately. The additional
 * thread has to manage the sending of the PDU on the IPC link through the link
 * object. To be able to synchronize the thread with the link object, a
 * condition variable is needed.
 */
public class EE_APIPX_AssocPxy extends EE_APIPX_LinkAdapter implements IChannelInform
{
    private static final Logger LOG = Logger.getLogger(EE_APIPX_AssocPxy.class.getName());

    private IChannelInitiate ieeChannelInitiate;

    /**
     * Set when the Channel object has complete.
     */
    private boolean channelComplete;

    /**
     * Indicates if it is a normal close of the IPC link or not.
     */
    public boolean normalStop;

    /**
     * Pointer to the database.
     */
    private final ProxyConfig config;

    private EE_APIPX_Link eeAPIPXLink;

    private WritingThread writingTh;
    
    private IUtil util;

    private ITranslator translator;

    /**
     * Constructor of the class which takes the operation and utility factory as
     * parameter.
     */
    public EE_APIPX_AssocPxy(ProxyConfig config)
    {
        this.ieeChannelInitiate = null;
        this.channelComplete = false;
        this.normalStop = false;
        this.config = config;
        this.writingTh = null;
        
        this.util = new Util();
        this.translator = new PDUTranslator();
    }

    /**
     * The link object calls this function when some data are received on the
     * IPC link and must be performed by the AssocPxy object. When the AssocPxy
     * object receives an encoded PDU from the IPC link, it sends it to the
     * EE_APIPX_Channel thanks to the IEE_ChannelInform interface
     */
    @Override
    public void takeData(byte[] data, int dataType, EE_APIPX_Link pLink, boolean lastPdu)
    {
        if (dataType == MessId.mid_NormalStop.getCode())
        {
            this.normalStop = true;
            return;
        }

        if (this.ieeChannelInitiate == null || this.channelComplete)
        {
            return;
        }

        if (dataType == MessId.mid_SlePdu.getCode())
        {
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest("Received SLE PDU to forward to the responding channel, lastPdu=" + lastPdu);
            }
            this.ieeChannelInitiate.sendSLEPDU(data, lastPdu);
            if (lastPdu)
            {
                releaseChannel();
            }
        }
        else if (dataType == MessId.mid_PeerAbort.getCode())
        {
            AssocChannel_Mess mess = new AssocChannel_Mess(data);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(mess.toString());
            }
            this.ieeChannelInitiate.sendPeerAbort(mess.getDiagnostic());
            releaseChannel();
        }
        else if (dataType == MessId.mid_Disconnect.getCode())
        {
            this.ieeChannelInitiate.sendDisconnect();
            releaseChannel();
        }
        else if (dataType == MessId.mid_Reset.getCode())
        {
            this.ieeChannelInitiate.sendReset();
            releaseChannel();
        }
        else if (dataType == MessId.mid_ResumeReceive.getCode())
        {
            this.ieeChannelInitiate.resumeReceive();
        }
        else if (dataType == MessId.mid_SuspendReceive.getCode())
        {
            this.ieeChannelInitiate.suspendReceive();
        }
    }

    /**
     * This function is called when the IPC connection is closed by the peer
     * side or lost.
     */
    @Override
    public void ipcClosed(EE_APIPX_Link pLink)
    {
        if (!this.normalStop)
        {
            if (this.ieeChannelInitiate != null)
            {
                PeerAbortDiagnostics diag = PeerAbortDiagnostics.PAD_communicationsFailure;
                this.ieeChannelInitiate.sendPeerAbort(diag.getCode());
            }
        }

        this.linkClosed = true;
        if (this.eeAPIPXLink != null)
        {
            this.eeAPIPXLink = null;
        }

        terminateThread();
    }

    /**
     * Receives an encoded PDU.
     */
    @Override
    public void rcvSLEPDU(byte[] data)
    {
        boolean doSend = false;
        Reference<Boolean> authOk = new Reference<Boolean>();
        authOk.setReference(new Boolean(false));
        Reference<Boolean> isInvoke = new Reference<Boolean>();
        isInvoke.setReference(new Boolean(false));
        BindDiagnostics diag = BindDiagnostics.BD_invalid;
        IOperation pOperation = null;
        int dataType = MessId.mid_SlePdu.getCode();

        if (data == null)
        {
            return;
        }

        if (this.eeAPIPXLink != null)
        {
            // already received a bind pdu
            // send the pdu on the ipc link
            doSend = true;
        }
        else
        {
            dumpPdu(data);

            // decode the pdu
            try
            {
            	pOperation = translator.decode(data,isInvoke);
            }
            catch (ApiResultException e)
            {
                LOG.log(Level.FINE, "Invalid operation", e);
                if (e.getResult() == Result.SLE_E_UNKNOWN)
                {
                    // bad service type
                    diag = BindDiagnostics.BD_serviceTypeNotSupported;
                    // generate a trace
                    String error = "Invalid Service Type";
                    String mess = PeerAbortDiagnostics.PAD_encodingError.toString() + " : " + error;
                    LOG.fine(mess);
                    releaseChannel();
                    return;
                }

                if (e.getResult() == Result.SLE_E_INCONSISTENT)
                {
                    // operation type not supported
                    // generate a trace message
                    String error = "Operation Type not supported";
                    String mess = PeerAbortDiagnostics.PAD_encodingError.toString() + " : " + error;
                    LOG.fine(mess);

                    releaseChannel();
                    return;
                }

                if (e.getResult() != Result.S_OK)
                {
                    // unable to decode the pdu
                    // generate a trace

                    String error = "Operation Type not supported";
                    String mess = PeerAbortDiagnostics.PAD_encodingError.toString() + " : " + error;
                    LOG.fine(mess);

                    // close the connection
                    if (this.ieeChannelInitiate != null)
                    {
                        this.ieeChannelInitiate.sendReset();
                    }

                    releaseChannel();
                    return;
                }
            } catch (ApiException e) {
            	LOG.log(Level.FINE, "Api exception from receiving pdu. ", e);
                releaseChannel();
                return;
			} catch (IOException e) {
				LOG.log(Level.FINE, "Api exception from receiving pdu. ", e);
                releaseChannel();
                return;
			}

            // check the operation type: must be a bind invoke
            if (!IBind.class.isAssignableFrom(pOperation.getClass()))
            {
                // close the connection
                if (this.ieeChannelInitiate != null)
                {
                    this.ieeChannelInitiate.sendReset();
                }

                releaseChannel();
                return;
            }
            
            IBind pBind = (IBind) pOperation;

            if (processBind(pBind, diag, authOk))
            {
                dataType = MessId.mid_BindPdu.getCode();
                doSend = true;
            }
            else
            {
                if (!authOk.getReference())
                {
                    // authentication fail
                    this.ieeChannelInitiate.sendReset();
                }
                else
                {
                    if (pBind != null)
                    {
                        sendBindReturn(pBind, diag);
                    }
                }

                releaseChannel();
            }
        }

        if (doSend)
        {
            // send the pdu on the link
            Header_Mess header = new Header_Mess(false, dataType, data.length);
            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(header.toString());
            }

            byte[] headerArray = header.toByteArray();
            byte[] newArray = new byte[headerArray.length + data.length];
            System.arraycopy(headerArray, 0, newArray, 0, headerArray.length);
            System.arraycopy(data, 0, newArray, headerArray.length, data.length);
            sendMessageNoWait(newArray);

            // sendMessageNoWait(header.toByteArray(), data);
        }
    }

    /**
     * Receives a CONNECT request.
     */
    @Override
    public void rcvConnect()
    {
        // nothing in the assocpxy
    }

    /**
     * Receives a PEER_ABORT request.
     */
    @Override
    public void rcvPeerAbort(int diagnostic, boolean originatorIsLocal)
    {
        this.normalStop = true;
        if (this.eeAPIPXLink == null)
        {
            // the first received pdu is not a bind
            if (this.ieeChannelInitiate != null)
            {
                this.ieeChannelInitiate.sendReset();
            }
        }
        else
        {
            // create the Assoc Channel Msg
            AssocChannel_Mess acMess = new AssocChannel_Mess();
            acMess.setDiagnostic(diagnostic);
            acMess.setOriginatorIsLocal(originatorIsLocal);

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(acMess.toString());
            }

            byte[] acMessByteArray = acMess.toByteArray();

            // create the Header Msg
            Header_Mess hMess = new Header_Mess(false,
                                                          MessId.mid_PeerAbort.getCode(),
                                                          acMessByteArray.length);

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(hMess.toString());
            }

            // create a byte array that contains both
            byte[] data = new byte[acMessByteArray.length + Header_Mess.hMsgLength];
            System.arraycopy(hMess.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
            System.arraycopy(acMessByteArray, 0, data, Header_Mess.hMsgLength, acMessByteArray.length);

            // send the message
            sendMessageNoWait(data);
        }
    }

    /**
     * Receives a PROTOCOL_ABORT request.
     */
    @Override
    public void rcvProtocolAbort(ISP1ProtocolAbortDiagnostics diagnostic)
    {
        this.normalStop = true;

        if (this.eeAPIPXLink == null)
        {
            // receive a protocol abort before a BIND pdu !!
            // do not report : done by TML
        }
        else
        {
            // create the Assoc Channel Msg
            AssocChannel_Mess acMess = new AssocChannel_Mess();
            acMess.setPaOriginator(diagnostic);

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(acMess.toString());
            }

            byte[] acMessByteArray = acMess.toByteArray();

            // create the Header Msg
            Header_Mess hMess = new Header_Mess(false,
                                                          MessId.mid_ProtocolAbort.getCode(),
                                                          acMessByteArray.length);

            if (LOG.isLoggable(Level.FINEST))
            {
                LOG.finest(hMess.toString());
            }

            // create a byte array that contains both
            byte[] data = new byte[acMessByteArray.length + Header_Mess.hMsgLength];
            System.arraycopy(hMess.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);
            System.arraycopy(acMessByteArray, 0, data, Header_Mess.hMsgLength, acMessByteArray.length);

            // send the message
            sendMessageNoWait(data);
        }
    }

    /**
     * @Request the suspension of the sending.
     */
    @Override
    public void suspendXmit()
    {
        // create the Assoc Channel Msg
        AssocChannel_Mess acMess = new AssocChannel_Mess();
        byte[] acMessByteArray = acMess.toByteArray();
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(acMess.toString());
        }

        // create the Header Msg
        Header_Mess hMess = new Header_Mess(false,
                                                      MessId.mid_SuspendXmit.getCode(),
                                                      acMessByteArray.length);
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(hMess.toString());
        }

        // create a byte array that contains all zeroes
        byte[] data = new byte[acMessByteArray.length + Header_Mess.hMsgLength];

        // copy the header
        System.arraycopy(hMess.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);

        // send the message
        sendMessageNoWait(data);
    }

    /**
     * @FunctionRequest a resumption of the sending.@EndFunction
     */
    @Override
    public void resumeXmit()
    {
        // create the Assoc Channel Msg
        AssocChannel_Mess acMess = new AssocChannel_Mess();
        byte[] acMessByteArray = acMess.toByteArray();
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(acMess.toString());
        }

        // create the Header Msg
        Header_Mess hMess = new Header_Mess(false,
                                                      MessId.mid_ResumeXmit.getCode(),
                                                      acMessByteArray.length);
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest(hMess.toString());
        }

        // create a byte array that contains all zeroes
        byte[] data = new byte[acMessByteArray.length + Header_Mess.hMsgLength];

        // copy the header
        System.arraycopy(hMess.toByteArray(), 0, data, 0, Header_Mess.hMsgLength);

        // send the message
        sendMessageNoWait(data);
    }

    /**
     * Set the Channel (through the interface IEE_ChannelInitiate) associated
     * with the AssocPxy object.
     */
    public void setChannelInitiate(IChannelInitiate pChannelInitiate)
    {
        this.ieeChannelInitiate = pChannelInitiate;
    }

    /**
     * This is the "main  function" of the thread class instance, when it
     * completes, then the thread will terminate. The goal of the thread here is
     * to manage the writing on the link object of a received PDU. This is done
     * in a separate thread in order to be able to respond immediately to the
     * sender. It should be noted that there is no need to pass any objects in
     * here - the class instance itself can contain any reference to data needed
     * by the threadMain function.
     */
    public void threadMain()
    {
        waitForWrite(this.eeAPIPXLink);

        if (this.channelComplete || this.eeAPIPXLink == null || this.eeAPIPXLink.isClosed())
        {
            this.writingTh.terminate();
        }
    }

    /**
     * Indicates if the IPC link is closed.
     */
    public boolean isClosed()
    {
        return this.linkClosed;
    }

    /**
     * Release the Channel interface.
     */
    public void releaseChannel()
    {
        if (LOG.isLoggable(Level.FINEST))
        {
            LOG.finest("Releasing channel on the association proxy");
        }
        this.channelComplete = true;
        this.ieeChannelInitiate.dispose();
        // release the reference to the link
        if (this.eeAPIPXLink != null)
        {
            // don't set linkClosed otherwise the last pdu is not transmitted
            // over the link !!
            this.eeAPIPXLink.setAssocPxy(null);
            this.eeAPIPXLink = null;
        }

        terminateThread();

        // release the reference from the binder
        EE_APIPX_Binder pBinder = EE_APIPX_Binder.getInstance();
        if (pBinder != null)
        {
            pBinder.cleanAssoc(this);
        }
    }

    /**
     * Process an incoming BIND PDU: checks the SLE service type, the version
     * number, and checks if the service instance is registered thanks to the
     * Binder. If all the checks are OK, sends the BIND PDU to the application
     * through the IPC link. CodesS_OK The service instance is registered, the
     * BIND PDU can be sent. E_FAIL The service instance is not registered, or
     * an error occurred.
     */
    private boolean processBind(IBind bindop, BindDiagnostics diag, Reference<Boolean> authOk)
    {
        EE_APIPX_Binder pBinder = null;
        EE_APIPX_Link pLink = null;
        IServiceInstanceIdentifier psii = null;
        boolean res = false;

        if (bindop == null)
        {
            return false;
        }

        psii = bindop.getServiceInstanceIdentifier();
        pBinder = EE_APIPX_Binder.getInstance();
        pLink = pBinder.getLink(psii);
        if (pBinder == null || psii == null || pLink == null)
        {
            // problem sii not registered
            diag = BindDiagnostics.BD_noSuchServiceInstance;
            res = false;

            // perform control and authentication
            checkBind(bindop, diag, authOk);
        }
        else if (pLink != null && pLink.getAssocPxy() != null)
        {
            // the link is already used by another assocpxy
            // --> must be a second bind for the same sii
            diag = BindDiagnostics.BD_alreadyBound;
            res = false;

            // perform control and authentication
            checkBind(bindop, diag, authOk);
        }
        else
        {
            // the service instance is registered --> forward the pdu to the
            // association
            if (this.eeAPIPXLink == null)
            {
                this.eeAPIPXLink = pLink;
                pLink.setAssocPxy(this);

                // create the writing thread after setting of the link
                this.threadRunning = true;
                this.writingTh = new WritingThread();
                this.writingTh.start();
                
//                this.serviceInstance = pLink.getChannelPxy().
                
                res = true;
            }
        }

        if (!res)
        {
            if (diag == BindDiagnostics.BD_accessDenied)
            {
                // report the error
                IReporter pIsleReporter = EE_APIPX_ReportTrace.getReporterInterface();
                if (pIsleReporter != null)
                {
	                String tmp = bindop.print(512);
	                String mess = "";
	                if (!authOk.getReference())
	                {
	                    mess = "Operation rejected. Authentication error. " + tmp;
	                }
	                else
	                {
	                    mess = "Operation rejected. " +  diag.toString() + tmp;
	                }
	                pIsleReporter.notifyApplication(psii, CstsLogMessageType.ALARM, mess);
	                pIsleReporter.logRecord(psii, 
					                		null, 
					                		Component.CP_proxy, 
					                		AlarmLevel.sleAL_authFailure, 
					                		CstsLogMessageType.ALARM, 
					                		mess);
            	}
            }
            else
            {
                // instantiate a trace message indicating that the pdu is not
                // transmitted
                String messOp = bindop.print(512);
                String mess = "";
                if (!authOk.getReference())
                {
                    mess = "Operation rejected. Authentication error. " + messOp;
                }
                else
                {
                    mess = "Operation rejected. " + diag.toString() + messOp;
                }
                LOG.fine(mess);
            }
        }

        return res;
    }

    /**
     * Checks if the service type and the version number are registered in the
     * database. CodesS_OK The service type and the version number are
     * registered. E_FAIL The service type or the version number is not
     * registered.
     */
    private boolean checkBind(IBind bindop, BindDiagnostics diag, Reference<Boolean> auth_ok)
    {
        AuthenticationMode authmode = AuthenticationMode.NONE;
        boolean res = true;
        boolean authOk = true;

        // check if the initiator identifier of the bind invoke is registered in
        // the peer application list
    	ArrayList<RemotePeer> pPeerAppliDataList = this.config.getRemotePeerList();
    	RemotePeer pPeerApplData = null;
    	for (RemotePeer peer : pPeerAppliDataList){
    		
    		if (peer.getId().equals(bindop.getInitiatorIdentifier()))
    			pPeerApplData = peer;
    		
    	}
        
        if (pPeerApplData == null)
        {
            diag = BindDiagnostics.BD_accessDenied;
            return false;
        }
        else
        {
            authmode = pPeerApplData.getAuthenticationMode();
        }

        if (authmode == AuthenticationMode.BIND_ONLY || authmode == AuthenticationMode.ALL)
        {
            ICredentials pOpCredentials = bindop.getInvokerCredentials();
            ISecAttributes pIsleSecAttr = null;

            pIsleSecAttr = this.util.createSecAttributes();

            if (pOpCredentials == null)
            {
                // no credentials in the op
                diag = BindDiagnostics.BD_accessDenied;
                authOk = false;
                res = false;
            }
            else if (pIsleSecAttr != null)
            {
                String username = "";
                byte[] passwd = null;

                // get the acceptable delay from the database
                int delay = 0;
                ProxyConfig pProxySettings = this.config;
                delay = pProxySettings.getAuthenticationDelay();

                // take user name and password in the peer application
                username = pPeerApplData.getId();
                passwd = pPeerApplData.getPassword().getBytes();
                pIsleSecAttr.setUserName(username);
                pIsleSecAttr.setPassword(passwd);

                if (!pIsleSecAttr.authenticate(pOpCredentials, delay))
                {
                    authOk = false;
                    diag = BindDiagnostics.BD_accessDenied;
                    res = false;
                }
            }
            else
            {
                // cannot create security attributes
                diag = BindDiagnostics.BD_accessDenied;
                authOk = false;
                res = false;
            }
        }

        if (!res)
        {
            return res;
        }

        // check if the service type is a supported service type in the database
        if (this.config.getRole() == ProxyRoleEnum.INITIATOR)
        {
            return true;
        }

        ServerType pSrvType = null;
        ArrayList<ServerType> pSrvTypeList = null;
        pSrvTypeList = this.config.getServerTypeList();
        for (ServerType serverType: pSrvTypeList){
        	
        	if(serverType.getServerId().equals(bindop.getServiceType().getApplicationIdentifierOID().toString())
        			//&& serverType.getServerVersion().contains(bindop.getServiceType().getVersions())
        			)
        		pSrvType = serverType;
        }
        
        if (pSrvType == null)
        {
            diag = BindDiagnostics.BD_serviceTypeNotSupported;
            return false;
        }

        // check if the version number match one entry in the database
        int indexMax = pSrvType.getServerVersion().size();
        int versionNumberBind = bindop.getOperationVersionNumber();
        boolean found = false;
        for (int index = 0; index < indexMax; index++)
        {
            int versionNumber = pSrvType.getServerVersion().get(index);
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

        if (!found)
        {
            diag = BindDiagnostics.BD_versionNotSupported;
            return false;
        }

        auth_ok.setReference(authOk);
        return true;
    }

    /**
     * Sends a negative bind return operation.
     */
    private void sendBindReturn(IBind pBind, BindDiagnostics diag)
    {
        byte[] bindReturnPdu = null;

        // fill and send a bind return operation
        pBind.setBindDiagnostic(diag);
        pBind.setResponderIdentifier(this.config.getLocalId());

        // set the credentials
        if (diag != BindDiagnostics.BD_accessDenied)
        {
            insertSecAttr(pBind);
        }

        if (this.ieeChannelInitiate != null)
        {
            // instantiate a trace message
            String messop = pBind.print(512);
            String mess = "Proxy sends a negative Bind Return. " + messop;
            LOG.fine(mess);

            // encode and send the pdu immediately
            try
            {
                bindReturnPdu = translator.encode(pBind, false);
                dumpPdu(bindReturnPdu);
                this.ieeChannelInitiate.sendSLEPDU(bindReturnPdu, true);
            }
            catch (ApiException e)
            {
                LOG.log(Level.FINE, "ApiException ", e);
            }
            catch (IOException e)
            {
                LOG.log(Level.FINE, "IOException ", e);
            }
        }
    }

    /**
     * Inserts security attributes in a negative bind return operation.
     */
    private void insertSecAttr(IBind pBind)
    {
        ISecAttributes pIsleSecAtt = null;
        AuthenticationMode authenticationMode = AuthenticationMode.NONE;
        String username = "";
        byte[] passwd = null;
        String peerId = pBind.getInitiatorIdentifier();
        pBind.setPerformerCredentials(null);

        if (peerId == null)
        {
            return;
        }

        // set the authentication mode : always taken in the peer-application
    	ArrayList<RemotePeer> pPeerAppliDataList = this.config.getRemotePeerList();
    	RemotePeer pPeerAppl = null;
    	for (RemotePeer peer : pPeerAppliDataList){
    		
    		if (peer.getId() == peerId)
    			pPeerAppl = peer;
    		
    	}
        if (pPeerAppl != null)
        {
            authenticationMode = pPeerAppl.getAuthenticationMode();
        }

        if (authenticationMode != AuthenticationMode.NONE)
        {
            pIsleSecAtt = this.util.createSecAttributes();
            if (pIsleSecAtt != null)
            {
                // set the user name and password : the local one to
                // calculate
                // performer credentials
                username = this.config.getLocalId();
                passwd = this.config.getLocalPw();
                pIsleSecAtt.setUserName(username);
                pIsleSecAtt.setPassword(passwd);
                ICredentials pCredentials = pIsleSecAtt.generateCredentials();
                pBind.setPerformerCredentials(pCredentials);
            }
        }
    }

    /**
     * This function is called to terminate the thread which waits for writing
     * message on the IPC link. It signals the condition variables, and waits
     * for the deletion of the thread (join).
     */
    private void terminateThread()
    {
        signalResponseReceived();

        // close the writing thread
        this.writingTh.terminate();
    }

    /**
     * Dump the content of the PDU by performing a trace.
     */
    private void dumpPdu(byte[] pdu)
    {
        LOG.finest(pdu.toString());
    }

    private class WritingThread extends Thread
    {
        private boolean isRunning = true;


        @Override
        public void run()
        {
            while (this.isRunning)
            {
                threadMain();
            }
        }

        public void terminate()
        {
            this.isRunning = false;
        }
    }
}
