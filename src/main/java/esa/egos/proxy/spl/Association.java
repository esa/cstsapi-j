package esa.egos.proxy.spl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ApiResultException;
import esa.egos.csts.api.main.IApi;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.serviceinstance.IServiceConfiguration;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.csts.api.util.ICredentials;
import esa.egos.proxy.ISrvProxyInform;
import esa.egos.proxy.ISrvProxyInitiate;
import esa.egos.proxy.del.PDUTranslator;
import esa.egos.proxy.enums.AbortOriginator;
import esa.egos.proxy.enums.AlarmLevel;
import esa.egos.proxy.enums.AssocState;
import esa.egos.proxy.enums.BindRole;
import esa.egos.proxy.enums.Component;
import esa.egos.proxy.logging.CstsLogMessageType;
import esa.egos.proxy.logging.IReporter;
import esa.egos.proxy.spl.types.SPLEvent;
import esa.egos.proxy.tml.ISP1ProtocolAbortDiagnostics;
import esa.egos.proxy.util.ISecAttributes;
import esa.egos.proxy.util.impl.OperationSequencer;
import esa.egos.proxy.util.impl.Reference;
import esa.egos.proxy.xml.AuthenticationMode;
import esa.egos.proxy.xml.ProxyConfig;
import esa.egos.proxy.xml.RemotePeer;

/**
 * The class EE_APIPX_Association implements the interfaces exported by the
 * component class 'Association', defined in reference [SLE-API]. It is
 * responsible for : - maintaining a send-queue of operations received via the
 * interface IServiceInitiate. The size of the send-queue is controlled with the
 * database's size queue. - passing the encoded PDU's to the Transport Mapping
 * Layer (TML) when the flow control is ok. The flow control of the association
 * is managed with the suspendXmit attribute. - passing the received operation
 * invocations and returns to the service instance. - state table processing
 * common to initiating and responding associations. - removal of Transfer
 * Buffer PDU's on request. - generation of log records. - generation of trace
 * records that are related to the association. The following tasks are
 * delegated to the class EE_APIPX_PDUTranslator: - encoding and decoding of the
 * operation objects. - decoding PDU's received from the TML. - maintening a
 * list of pending returns. The following tasks are delegated to the TML: -
 * mapping of port identifiers to technology specific address information. -
 * monitoring of the state of the data communication connection. The class is a
 * base-class for further refined association classes, where the base-class
 * provides common functionality for derived classes.@EndResponsibility After
 * receiving an operation from the IServiceInitiate, or receiving a PDU from
 * TML, the derived classes InitiatingAssoc and RespondingAssoc call some common
 * functionalities of the Association class. The association receives PDU from
 * TML through the IEE_ChannelInform interface, and if all the checks are ok
 * (calls of the state machine), it forwards the decoded operation to the
 * service element through the ISLE_SrvProxyInform. On the other way, for
 * operations received from the IServiceInitiate interface, the association : -
 * calls the sequencer. - calls the state machine which will call other
 * functions of the association depending of the state of the association and
 * the type of operation. The Association object can be accessed by several
 * threads. To maintain the integrity of the sending queue and of the sequence
 * counting, two mutex are needed.
 */
public abstract class Association implements ISrvProxyInitiate, IChannelInform {
	private static final Logger LOG = Logger.getLogger(Association.class.getName());

	/**
	 * the pointer to the ISLE_SrvProxyInform interface.
	 */
	private ISrvProxyInform srvProxyInform;

	/**
	 * The pointer to the IEE_ChannelInitiate interface.
	 */
	protected IChannelInitiate channelInitiate;

	protected ProxyConfig config;

	private PDUTranslator translator;

	/**
	 * The state of the association.
	 */
	private AssocState state;

	/**
	 * The state of the association when the state is unbound. Can be "Disconnected"
	 * or "Connect Pend".
	 */
	protected boolean unboundStateIsDisconnected;

	/**
	 * The sequence counter used when transmitting the operation to the service
	 * element through the ISLE_SrvProxyInform interface..
	 */
	private long sequenceCounter;

	/**
	 * Indicates if the traces had been started for the association.
	 */
	protected boolean traceStarted;

	/**
	 * The role of the association.
	 */
	protected BindRole role;

	/**
	 * Indicates if the sending of PDU to TML is suspended or not.
	 */
	protected boolean suspendXmit;

	protected IReporter reporter;

	private IApi api;

	/**
	 * The authentication mode set in the association.
	 */
	protected AuthenticationMode authenticationMode;

	/**
	 * Indicates if the association has been aborted.
	 */
	protected boolean isAborted;

	/**
	 * Indicates if the association has been released.
	 */
	protected boolean isReleased;

	protected int queueSize;

	protected int version;

	protected OperationSequencer oPSequencer;

	protected ProxyAdmin proxy;

	private final LinkedList<SPLOperation> iOperation;

	private ISecAttributes iSecAttr;

	protected ReentrantLock objMutex;

	protected ReentrantLock innerLock = new ReentrantLock();

	public Association() {
		this.srvProxyInform = null;
		this.channelInitiate = null;
		this.state = AssocState.sleAST_unbound;
		this.unboundStateIsDisconnected = true;
		this.sequenceCounter = 0;
		this.role = BindRole.BR_initiator;
		this.suspendXmit = true;
		this.reporter = null;
		this.api = null;
		this.authenticationMode = AuthenticationMode.NONE;
		this.isAborted = false;
		this.isReleased = false;
		this.queueSize = 1;
		this.version = 0;
		this.oPSequencer = new OperationSequencer();
		this.iOperation = new LinkedList<SPLOperation>();
		this.iSecAttr = null;
		this.objMutex = new ReentrantLock();

		initAssoc();
	}

	/**
	 * See specification of IServiceInitiate.
	 */
	@Override
	public void discardBuffer() throws ApiException {
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("Discard buffer request received");
		}
		this.objMutex.lock();
		if (this.state != AssocState.sleAST_bound) {
			this.objMutex.unlock();
			throw new ApiException(Result.SLE_E_STATE.toString());
		}

		// check all the operations of the sending queue
		boolean onebufDiscarded = false;
		ListIterator<SPLOperation> listIter = this.iOperation.listIterator();
		while (listIter.hasNext()) {
			IOperation pIsleOperation = listIter.next().getpOperation();
			if (pIsleOperation != null) {
				// TODO transferBuffer same as process data?
				if (IProcessData.class.isAssignableFrom(pIsleOperation.getClass())) {
					this.iOperation.remove(this.iOperation.indexOf(pIsleOperation));
					onebufDiscarded = true;
				}
			} else {
				this.iOperation.remove(this.iOperation.indexOf(pIsleOperation));
			}
		}

		if (onebufDiscarded) {
			this.objMutex.unlock();
			throw new ApiException(Result.SLE_S_DISCARDED.toString());
		}

		this.objMutex.unlock();
		throw new ApiException(Result.SLE_S_NOTDISCARDED.toString());
	}

	/**
	 * Returns the current state of the association.
	 */
	@Override
	public AssocState getAssocState() {
		return getState();
	}

	/**
	 * Makes a context switch to a new thread to call
	 * "doStateProcessingAndResumeRecv" For PEER-ABORT / PROTOCOL-ABORT no context
	 * switch is done S_OK The state-processing is complete. SLE_E_PROTOCOL The
	 * operation cannot be accepted in the current state. SLE_E_UNBINDING The pdu
	 * can no longer be accepted because an unbinding operation has already been
	 * initialized. E_FAIL The state-processing has failed.
	 */
	protected void rcvSLEPDUBlocking(byte[] data) {
		this.innerLock.lock();
		this.objMutex.lock();
		try {
			dumpPdu(true, data);

			Reference<IOperation> poperation = new Reference<IOperation>();
			poperation.setReference(null);
			Reference<Boolean> isInvoke = new Reference<Boolean>();
			isInvoke.setReference(false);

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("poperation before netwPreProcessing: " + poperation.getReference());
			}
			Result result = netwPreProcessing(data, poperation, isInvoke);

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("poperation after netwPreProcessing: " + poperation.getReference());
			}
			if (result == Result.S_OK) {
				doStateProcessing(true, poperation.getReference(), isInvoke.getReference(), false);
			}
		} finally {
			this.objMutex.unlock();
			this.innerLock.unlock();
		}
	}

	/**
	 * Receives an encoded PDU from the Channel. The pre-processing of the PDU is
	 * done before the operation is given to the state machine.
	 */
	@Override
	public void rcvSLEPDU(byte[] data) {
		rcvSLEPDUBlocking(data);
	}

	/**
	 * Reception of a CONNECT request. Nothing to do in the association.
	 */
	@Override
	public void rcvConnect() {
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("Connect request received");
		}
		this.objMutex.lock();

		if (this.state == AssocState.sleAST_unbound && this.role == BindRole.BR_initiator
				&& !this.unboundStateIsDisconnected) {
			// set the state to bind pending
			changeState(SPLEvent.PXSPL_initiateBindInvoke, AssocState.sleAST_bindPending);

			// send the bind
			this.objMutex.unlock();

			resumeXmit();
		} else {
			this.objMutex.unlock();
		}
	}

	/**
	 * Reception of a BIND from the peer proxy. This a pure virtual methods which is
	 * implemented in the derived class. S_OK The processing is complete.
	 * SLE_E_PROTOCOL The operation cannot be accepted in the current state.
	 * SLE_E_UNBINDING The pdu can no onger be accepted because an unbinding
	 * operation has already been initialised. E_FAIL The processing has failed.
	 */
	public abstract Result rcvBindInvoke(IOperation poperation);

	/**
	 * Reception of a BIND Return from the peer proxy. This a pure virtual methods
	 * which is implemented in the derived class. S_OK The processing is complete.
	 * SLE_E_PROTOCOL The operation cannot be accepted in the current state.
	 * SLE_E_UNBINDING The pdu can no onger be accepted because an unbinding
	 * operation has already been initialised. E_FAIL The processing has failed.
	 */
	public abstract Result rcvBindReturn(IOperation poperation);

	/**
	 * Reception of a UNBIND Invoke from the peer proxy. This a pure virtual methods
	 * which is implemented in the derived class. S_OK The processing is complete.
	 * SLE_E_PROTOCOL The operation cannot be accepted in the current state.
	 * SLE_E_UNBINDING The pdu can no onger be accepted because an unbinding
	 * operation has already been initialised. E_FAIL The processing has failed.
	 */
	public abstract Result rcvUnbindInvoke(IOperation poperation);

	/**
	 * Reception of a UNBIND Return from the peer proxy. This a pure virtual methods
	 * which is implemented in the derived class. S_OK The processing is complete.
	 * SLE_E_PROTOCOL The operation cannot be accepted in the current state.
	 * SLE_E_UNBINDING The pdu can no onger be accepted because an unbinding
	 * operation has already been initialised. E_FAIL The processing has failed.
	 */
	public abstract Result rcvUnbindReturn(IOperation poperation);

	/**
	 * Requests the suspension of the sending.
	 */
	@Override
	public void suspendXmit() {
		this.objMutex.lock();
		this.suspendXmit = true;
		this.objMutex.unlock();
	}

	/**
	 * Requests a resumption of the sending.
	 */
	@Override
	public void resumeXmit() {
		this.objMutex.lock();

		SPLOperation theOp = null;

		// the channel is ready for transmission
		if (this.suspendXmit) {
			// send the next operation if one
			if (!this.iOperation.isEmpty()) {
				// take the first operation in the list
				theOp = this.iOperation.pollFirst();
			}
		}

		if (theOp != null) {
			// encode and send the pdu immediately
			byte[] encodedPdu = null;
			try {
				if (LOG.isLoggable(Level.FINEST)) {
					LOG.finest("theOp.getpOperation() " + theOp.getpOperation());
				}
				encodedPdu = this.api.getTranslator(theOp.getpOperation().getServiceInstanceIdentifier())
						.encode(theOp.getpOperation(), theOp.isInvoke());
				dumpPdu(false, encodedPdu);
				this.suspendXmit = true;

				this.objMutex.unlock();
				if (LOG.isLoggable(Level.FINEST)) {
					LOG.finest("encodedPdu.length " + encodedPdu.length);
				}
				this.channelInitiate.sendSLEPDU(encodedPdu, theOp.isLastPdu());
				if (theOp.isReportTransmission()) {
					try {
						this.srvProxyInform.pduTransmitted(theOp.getpOperation());
					} catch (ApiException e) {
						LOG.log(Level.FINE, "CstsApiException ", e);
					} finally {
						this.objMutex.lock();
					}
				} else {
					this.objMutex.lock();
				}
			} catch (ApiException | IOException e) {
				this.suspendXmit = false;
				String mess = PeerAbortDiagnostics.ENCODING_ERROR.toString() + ":" + e.getMessage();
				notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_localAbort, 1004, mess, null);
			}
		} else {
			this.suspendXmit = false;
		}

		this.objMutex.unlock();
	}

	/**
	 * Returns the role of the association.
	 */
	public BindRole getRole() {
		return this.role;
	}

	/**
	 * Returns the is released attribute.
	 */
	public boolean getIsReleased() {
		return this.isReleased;
	}

	/**
	 * Returns the IEE_ChannelInitiate pointer.
	 */
	public IChannelInitiate getChannelInitiate() {
		return this.channelInitiate;
	}

	public void setChannelInitiate(IChannelInitiate value) {
		this.channelInitiate = value;
	}

	/**
	 * Sets the SrvProxyInform interface.
	 */
	protected void setSrvProxyInform(ISrvProxyInform srvproxyinform) {
		if (this.srvProxyInform != null) {
			this.srvProxyInform = null;
		}

		this.srvProxyInform = srvproxyinform;
	}

	/**
	 * Removes the association from the list of association in the Proxy, and
	 * releases the reference to the association.
	 */
	protected void releaseAssociation() {
		this.objMutex.unlock();
		this.proxy.deregisterAssoc();
		this.objMutex.lock();
		releaseChannel();

		this.isReleased = true;
		this.state = AssocState.sleAST_unbound;
		this.unboundStateIsDisconnected = true;
	}

	/**
	 * Removes and discard all operation invocations that are queued for
	 * transmission.
	 */
	protected void discardAllInvocationPdu() {
		LOG.finest("Discard all invocation PDU. Operation queue now empty");

		// release all the operation of the sending queue
		this.iOperation.clear();
	}

	/**
	 * Releases all the confirmed operations inserted in the list of pending return,
	 * and removes the list.
	 */
	protected void clearAllPendingReturn() {
		if (getSrvProxyInform().getTranslator() != null) {
			getSrvProxyInform().getTranslator().removeAllPendingReturns();
		}
	}

	/**
	 * Receives a PEER_ABORT request from the Channel. If the originator is not
	 * local : - decodes the PEER-ABORT to get a peer-abort operation. - forwards
	 * the PEER-ABORT operation to the local client. - discards all the PDU's of the
	 * transmission queue. - clears the list of pending returns operation. - sets
	 * the state to unbound.
	 */
	@Override
	public void rcvPeerAbort(int diagnostic, boolean originatorIsLocal) {
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("PEER-ABORT received, diagnostic " + diagnostic + ", originator local " + originatorIsLocal);
		}
		if (this.state == AssocState.sleAST_unbound) {
			if (this.unboundStateIsDisconnected) {
				// protocol error
				return;
			} else {
				// reset the connection
				if (this.channelInitiate != null) {
					this.objMutex.unlock();
					this.channelInitiate.sendReset();
					this.objMutex.lock();
				}

				this.unboundStateIsDisconnected = true;
			}
		}

		IOperation poperation = netwPreProcessing(diagnostic, originatorIsLocal);
		if (poperation != null) {
			doStateProcessing(true, poperation, false, false);
		}
	}

	/**
	 * Receives a PROTOCOL_ABORT request : - sends a PROTOCOL-ABORT to the local
	 * client. - discards all the PDU's of the transmission queue. - clears the list
	 * of pending returns operation. - sets the state to unbound.
	 */
	@Override
	public void rcvProtocolAbort(ISP1ProtocolAbortDiagnostics diagnostic) {
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("Protocol abort received, diagnostic " + diagnostic);
		}
		if (this.state != AssocState.sleAST_unbound
				|| (this.state == AssocState.sleAST_unbound && !this.unboundStateIsDisconnected)) {
			// sends a protocol abort to the client
			if (this.srvProxyInform != null) {
				this.objMutex.unlock();
				try {
					this.srvProxyInform.protocolAbort(diagnostic.getDiagAsByteArray());
				} catch (ApiException e) {
					LOG.log(Level.FINE, "CstsApiException ", e);
				} finally {
					this.objMutex.lock();
				}
			}
		}

		changeState(SPLEvent.PXSPL_rcvProtocolAbort, AssocState.sleAST_unbound);
		this.unboundStateIsDisconnected = true;
		initAssoc();

		this.objMutex.unlock();
		this.oPSequencer.reset(Result.SLE_E_ABORTED);
		this.objMutex.lock();

		// cleanup
		discardAllInvocationPdu();
		clearAllPendingReturn();
		releaseChannel();
	}

	/**
	 * Receives a PDU from the peer-proxy with an invocation that is valid for the
	 * service type. If the state is "bound", or "loc unbind pend", or "rem unbind
	 * pend": - increments the sequence counter. - sends the operation to the local
	 * client. S_OK The processing is complete. SLE_E_PROTOCOL The operation cannot
	 * be accepted in the current state. SLE_E_UNBINDING The pdu can no onger be
	 * accepted because an unbinding operation has already been initialised. E_FAIL
	 * The processing has failed.
	 */
	protected Result rcvSrvPduInvoke(IOperation poperation) {
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("Server PDU invoke called, operation " + poperation.toString());
		}
		if (this.state == AssocState.sleAST_unbound) {
			return Result.SLE_E_PROTOCOL;
		} else if (this.state == AssocState.sleAST_bound || this.state == AssocState.sleAST_localUnbindPending) {
			if (this.srvProxyInform != null) {
				long seqc = this.sequenceCounter++;
				this.objMutex.unlock();
				try {
					this.srvProxyInform.informOpInvoke(poperation, seqc);
				} catch (ApiException e) {
					return Result.E_FAIL;
				} finally {
					this.objMutex.lock();
				}
			}
		} else if (this.state == AssocState.sleAST_bindPending || this.state == AssocState.sleAST_remoteUnbindPending) {
			doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
			return Result.SLE_E_PROTOCOL;
		}

		return Result.S_OK;
	}

	/**
	 * Receives a return PDU from the peer-proxy with an invocation that is valid
	 * for the service type. If the state is "bound", or "loc unbind pend", or "rem
	 * unbind pend": - checks if a pending return exists for this operation. -
	 * increments the sequence counter. - sends the operation to the local client.
	 * S_OK The processing is complete. SLE_E_PROTOCOL The operation cannot be
	 * accepted in the current state. SLE_E_UNBINDING The pdu can no onger be
	 * accepted because an unbinding operation has already been initialised. E_FAIL
	 * The processing has failed.
	 */
	protected Result rcvSrvPduReturn(IOperation poperation) {
		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("Server PDU return called, operation " + poperation.toString());
		}
		if (this.state == AssocState.sleAST_unbound) {
			return Result.SLE_E_PROTOCOL;
		} else if (this.state == AssocState.sleAST_bound || this.state == AssocState.sleAST_localUnbindPending) {
			if (this.srvProxyInform != null) {
				IConfirmedOperation pConfOp = (IConfirmedOperation) poperation;
				if (pConfOp != null) {
					long seqc = this.sequenceCounter++;
					if (LOG.isLoggable(Level.FINE)) {
						LOG.fine("seqCount : " + seqc + "   " + poperation.toString());
					}
					this.objMutex.unlock();
					try {
						this.srvProxyInform.informOpReturn(pConfOp, seqc);
					} catch (ApiException e) {
						LOG.log(Level.FINE, "CstsApiException ", e);
						return Result.E_FAIL;
					} finally {
						this.objMutex.lock();
					}
				}
			}
		} else if (this.state == AssocState.sleAST_bindPending || this.state == AssocState.sleAST_remoteUnbindPending) {
			doAbort(PeerAbortDiagnostics.PROTOCOL_ERROR, AbortOriginator.INTERNAL, true);
			return Result.SLE_E_PROTOCOL;
		}

		return Result.S_OK;
	}

	/**
	 * Abort the association : - releases all the operations of the sending queue. -
	 * sends a PEER-ABORT pdu to the peer-proxy. - sends a PEER-ABORT to the local
	 * client. - clears the list of pending returns operation. - sets the state to
	 * unbound. S_OK The processing is complete. E_FAIL The processing has failed.
	 */
	protected Result doAbort(IPeerAbort pPeerAbort, boolean sendToPeer) {
		Result res = Result.S_OK;

		if (this.isAborted) {
			return Result.S_OK;
		}

		this.isAborted = true;

		AbortOriginator ao = pPeerAbort.getAbortOriginator();

		if (this.channelInitiate != null && ao != AbortOriginator.PEER && sendToPeer) {
			// send the peer abort to the remote proxy
			int diag = -1;

			diag = pPeerAbort.getPeerAbortDiagnostic().getCode();

			this.objMutex.unlock();
			this.channelInitiate.sendPeerAbort(diag);
			this.objMutex.lock();
		}

		if (ao == AbortOriginator.PEER) {
			changeState(SPLEvent.PXSPL_rcvPeerAbort, AssocState.sleAST_unbound);
		} else {
			changeState(SPLEvent.PXSPL_initiatePeerAbort, AssocState.sleAST_unbound);
		}

		this.unboundStateIsDisconnected = true;

		// send a peer abort operation to the client
		if (this.srvProxyInform != null && (ao == AbortOriginator.PEER || ao == AbortOriginator.INTERNAL)) {
			// send the peer abort operation
			long seqc = this.sequenceCounter++;

			//
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("th" + Thread.currentThread().getId() + " is calling initAssoc()");
			}
			initAssoc();

			// cleanup
			discardAllInvocationPdu();
			clearAllPendingReturn();
			//

			this.objMutex.unlock();

			try {
				this.srvProxyInform.informOpInvoke(pPeerAbort, seqc);
			} catch (ApiException e) {

				LOG.log(Level.FINE, "CstsApiException ", e);
				res = Result.E_FAIL;
				return res;
			} finally {

				this.objMutex.lock();

			}

			res = Result.S_OK;
		}

		// if (LOG.isLoggable(Level.FINEST))
		// {
		// LOG.finest(">>>>>>>>>>>>>>>>>>>>>>>>>>>> TH" +
		// Thread.currentThread().getId() + " is calling initAssoc()");
		// }
		// initAssoc();
		//
		// this.objMutex.unlock();
		this.oPSequencer.reset(Result.SLE_E_ABORTED);
		// this.objMutex.lock();
		//
		// // cleanup
		// discardAllInvocationPdu();
		// clearAllPendingReturn();

		return res;
	}

	/**
	 * Abort the association : - releases all the operations of the sending queue. -
	 * sends a PEER-ABORT pdu to the peer-proxy. - sends a PEER-ABORT to the local
	 * client. - clears the list of pending returns operation. - sets the state to
	 * unbound. S_OK The processing is complete. E_FAIL The processing has failed.
	 */
	protected Result doAbort(PeerAbortDiagnostics diagnostic, AbortOriginator abortoriginator, boolean sendToPeer) {
		IPeerAbort pPeerAbort = null;

		if (this.isAborted) {
			return Result.S_OK;
		}

		try {
			pPeerAbort = this.api.createAbort();
		} catch (ApiException e) {
			LOG.log(Level.FINE, "CstsApiException ", e);
			return Result.E_FAIL;
		}

		// fill the operation
		pPeerAbort.setPeerAbortDiagnostic(diagnostic);
		pPeerAbort.setAbortOriginator(abortoriginator);

		doAbort(pPeerAbort, sendToPeer);

		return Result.S_OK;
	}

	/**
	 * If authentication is required, authenticates the identity of the peer-proxy
	 * thanks to : - the EE_SLE_SecAttributes set in the association. - the peer
	 * credentials.
	 */
	protected boolean authenticate(IOperation poperation, boolean isInvoke) {
		boolean res = false;

		if (this.authenticationMode == AuthenticationMode.NONE) {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Authentication NONE");
			}
			return true;
		}

		if (this.iSecAttr == null || this.api == null) {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Authentication fails, secAttr or db null");
			}
			return false;
		}

		// get the acceptable delay from the database
		int delay = 0;
		ProxyConfig pProxySettings = this.api.getProxySettings();
		delay = pProxySettings.getAuthenticationDelay();

		if (this.authenticationMode == AuthenticationMode.BIND_ONLY) {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Authenticate BIND ONLY");
			}

			if (IBind.class.isAssignableFrom(poperation.getClass())) {
				if (LOG.isLoggable(Level.FINE)) {
					LOG.fine("Authenticate BIND operation");
				}

				res = authenticateOperation(poperation, isInvoke, delay);
			} else {
				res = true;
			}
		} else {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Authenticate NOT BIND ONLY");
			}

			res = authenticateOperation(poperation, isInvoke, delay);
		}

		return res;
	}

	/**
	 * If authentication is required, inserts the security attributes of the
	 * association in the operation.
	 */
	protected void insertSecurityAttributes(IOperation poperation, boolean isInvoke) {
		ISecAttributes pSecAttr = this.proxy.getSecurityAttribures();

		if (isInvoke) {
			if (pSecAttr == null) {
				insertCredentialsInOp(poperation, null);
				return;
			}

			if (this.authenticationMode == AuthenticationMode.NONE
					|| (this.authenticationMode == AuthenticationMode.BIND_ONLY
							&& !IBind.class.isAssignableFrom(poperation.getClass()))) {
				insertCredentialsInOp(poperation, null);
				return;
			}

			ICredentials pCredentials = pSecAttr.generateCredentials();
			insertCredentialsInOp(poperation, pCredentials);
		} else {
			IConfirmedOperation pConfOp = (IConfirmedOperation) poperation;
			if (pConfOp != null) {
				if (pSecAttr == null) {
					insertCredentialsInConfOp(pConfOp, null);
					return;
				}

				if (this.authenticationMode == AuthenticationMode.NONE
						|| (this.authenticationMode == AuthenticationMode.BIND_ONLY
								&& !IBind.class.isAssignableFrom(poperation.getClass()))) {
					insertCredentialsInConfOp(pConfOp, null);
					return;
				}

				ICredentials pcredentials = pSecAttr.generateCredentials();
				insertCredentialsInConfOp(pConfOp, pcredentials);
			}
		}
	}

	/**
	 * Receives a PEER_ABORT request from the local client, or the proxy invokes the
	 * PEER-ABORT itself : - discards all the PDU's of the transmission queue. -
	 * encodes and sends the PEER-ABORT to the peer proxy. - terminates the data
	 * communication association. - sets the state to unbound. - if the PEER-ABORT
	 * is initiated by the proxy itself, forwards the PEER-ABORT operation to the
	 * local client. See specification of IServiceInitiate.
	 */
	protected Result initiatePeerAbort(IPeerAbort pPeerAbort, boolean report) {
		if (this.state == AssocState.sleAST_unbound) {
			if (this.unboundStateIsDisconnected) {
				return Result.SLE_E_PROTOCOL;
			} else {
				// send a reset to TML
				this.objMutex.unlock();
				this.channelInitiate.sendReset();
				this.objMutex.lock();

				this.unboundStateIsDisconnected = true;

				discardAllInvocationPdu();
				clearAllPendingReturn();
				return Result.S_OK;
			}
		}

		if (report && this.srvProxyInform != null) {
			this.objMutex.unlock();
			try {
				this.srvProxyInform.pduTransmitted(pPeerAbort);
			} catch (ApiException e) {
				LOG.log(Level.FINE, "CstsApiException ", e);
			} finally {
				this.objMutex.lock();
			}
		}

		Result res = doAbort(pPeerAbort, true);
		return res;
	}

	/**
	 * Receives an operation from the local client with an invocation that is valid
	 * for the service type. If the state is bound and if the sending queue is empty
	 * : - encodes the operation and if it is a confirmed operation, inserts the
	 * pending return in the list. - sends the PDU to the peer-proxy. Otherwise,
	 * inserts the operation in the sending queue. See specification of
	 * IServiceInitiate.
	 */
	protected Result initiateSrvOperationInvoke(IOperation poperation, boolean report) {
		if (this.state == AssocState.sleAST_unbound || this.state == AssocState.sleAST_bindPending
				|| this.state == AssocState.sleAST_localUnbindPending) {
			return Result.SLE_E_PROTOCOL;
		} else if (this.state == AssocState.sleAST_remoteUnbindPending) {
			return Result.SLE_E_UNBINDING;
		}

		// the operation is encoded and sent in the post-processing
		Result res = clientPostProcessing(poperation, report, true, false);
		return res;
	}

	/**
	 * Receives a return operation from the local client. If the sending queue is
	 * empty : - encodes the operation. - sends the PDU to the peer-proxy.
	 * Otherwise, inserts the operation in the sending queue. See specification of
	 * IServiceInitiate.
	 */
	protected Result initiateSrvOperationReturn(IConfirmedOperation poperation, boolean report) {
		if (this.state == AssocState.sleAST_unbound || this.state == AssocState.sleAST_bindPending
				|| this.state == AssocState.sleAST_localUnbindPending) {
			return Result.SLE_E_PROTOCOL;
		}

		// the operation is encoded and sent in the post-processing
		Result res = clientPostProcessing(poperation, report, false, false);
		return res;
	}

	/**
	 * Receives an invoke operation from the service element. The operation is given
	 * to the sequencer, and when the sequencer returns, the operation is given to
	 * the client-pre-processing and then to the state machine. See specification of
	 * IServiceInitiate.
	 * 
	 * @throws ApiException
	 */
	@Override
	public void initiateOpInvoke(IOperation operation, boolean reportTransmission, long seqCount) throws ApiException {
		Result res = Result.S_OK;
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("Before locking obj mutex");
		}
		this.innerLock.lock();
		this.objMutex.lock();
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("After locking obj mutex");
		}

		res = clientPreProcessing(operation, seqCount);

		if (res == Result.S_OK) {
			res = doStateProcessing(false, operation, true, reportTransmission);
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("poperation on to be invoked " + operation + "  res = " + res);
			}

			if (res == Result.SLE_S_TRANSMITTED) {
				resumeXmit();
			}

			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Before unlocking obj mutex");
			}
			this.objMutex.unlock();
			this.innerLock.unlock();
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("After unlocking obj mutex");
			}

			// sequencer continues delivering the next operation
			this.oPSequencer.cont();
		} else {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("Before unlocking obj mutex");
			}
			this.objMutex.unlock();
			this.innerLock.unlock();
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine("After unlocking obj mutex");
			}
		}
	}

	/**
	 * Receives a BIND request from the local client. This a pure virtual methods
	 * which is implemented in the derived class. See specification of
	 * IServiceInitiate.
	 */
	protected abstract Result initiateBindInvoke(IOperation poperation, boolean reportTransmission);

	/**
	 * Receives a UNBIND request from the local client.This a pure virtual methods
	 * which is implemented in the derived class. See specification of
	 * IServiceInitiate.
	 */
	protected abstract Result initiateUnbindInvoke(IOperation poperation, boolean report);

	/**
	 * Receives a return operation from the service element. The operation is given
	 * to the sequencer, and when the sequencer returns, the operation is given to
	 * the client-pre-processing and then to the state machine. See specification of
	 * IServiceInitiate.
	 */
	@Override
	public void initiateOpReturn(IConfirmedOperation poperation, boolean report, long seqCount) throws ApiException {
		Result res;
		this.innerLock.lock();
		this.objMutex.lock();

		if ((res = clientPreProcessing(poperation, seqCount)) == Result.S_OK) {

			res = doStateProcessing(false, poperation, false, report);
			if (res == Result.SLE_S_TRANSMITTED) {
				resumeXmit();
			}
			this.objMutex.unlock();
			this.innerLock.unlock();
			this.oPSequencer.cont(); // sequencer continues delivering the next
										// operation
		} else {
			this.objMutex.unlock();
			this.innerLock.unlock();
		}

		if (res != Result.S_OK && res != Result.SLE_S_TRANSMITTED) {
			throw new ApiException(res.toString());
		}
	}

	/**
	 * Receives a BIND Return from the local client. This a pure virtual methods
	 * which is implemented in the derived class. See specification of
	 * IServiceInitiate.
	 */
	protected abstract Result initiateBindReturn(IConfirmedOperation poperation, boolean report);

	/**
	 * Receives an UNBIND Return from the local client. This a pure virtual methods
	 * which is implemented in the derived class. See specification of
	 * IServiceInitiate.
	 */
	protected abstract Result initiateUnbindReturn(IConfirmedOperation poperation, boolean report);

	/**
	 * Performs state processing as specified in the state-table. The
	 * member-function performs a state change if necessary, and initiates all
	 * necessary actions e.g. transmitting the operation to the post-processing,
	 * aborting an association, encoding and transmitting an operation to the
	 * network, etc. Note that this member-function is only called after a
	 * successful pre-processing. S_OK The state-processing is complete.
	 * SLE_E_PROTOCOL The operation cannot be accepted in the current state.
	 * SLE_E_UNBINDING The pdu can no onger be accepted because an unbinding
	 * operation has already been initialised. E_FAIL The state-processing has
	 * failed.
	 */
	private Result doStateProcessing(boolean originatorIsNetwork, IOperation pOperation, boolean isInvoke,
			boolean reportTransmission) {
		Result res = Result.E_FAIL;

		if (IBind.class.isAssignableFrom(pOperation.getClass())) {
			if (originatorIsNetwork) {
				if (isInvoke) {
					res = rcvBindInvoke(pOperation);
				} else {
					res = rcvBindReturn(pOperation);
				}
			} else {
				if (isInvoke) {
					res = initiateBindInvoke(pOperation, reportTransmission);
				} else {
					IConfirmedOperation pConfOp = (IConfirmedOperation) pOperation;

					if (pConfOp != null) {
						res = initiateBindReturn(pConfOp, reportTransmission);
					}
				}
			}
		} else if (IUnbind.class.isAssignableFrom(pOperation.getClass())) {
			if (originatorIsNetwork) {
				if (isInvoke) {
					res = rcvUnbindInvoke(pOperation);
				} else {
					res = rcvUnbindReturn(pOperation);
				}
			} else {
				if (isInvoke) {
					res = initiateUnbindInvoke(pOperation, reportTransmission);
				} else {
					IConfirmedOperation pConfOp = (IConfirmedOperation) pOperation;
					if (pConfOp != null) {
						res = initiateUnbindReturn(pConfOp, reportTransmission);
					}
				}
			}
		} else if (IPeerAbort.class.isAssignableFrom(pOperation.getClass())) {
			IPeerAbort pPeerAbort = (IPeerAbort) pOperation;
			if (originatorIsNetwork) {
				if (pPeerAbort != null) {
					res = doAbort(pPeerAbort, false);
				}
			} else {
				if (pPeerAbort != null) {
					res = initiatePeerAbort(pPeerAbort, reportTransmission);
				}
			}
		} else {
			if (originatorIsNetwork) {
				if (isInvoke) {
					res = rcvSrvPduInvoke(pOperation);
				} else {
					res = rcvSrvPduReturn(pOperation);
				}
			} else {
				if (isInvoke) {
					res = initiateSrvOperationInvoke(pOperation, reportTransmission);
				} else {
					IConfirmedOperation pConfOp = (IConfirmedOperation) pOperation;
					if (pConfOp != null) {
						res = initiateSrvOperationReturn(pConfOp, reportTransmission);
					}
				}
			}
		}

		return res;
	}

	/**
	 * Performs pre-processing of events received from the network interface, as
	 * specified in the state-table. S_OK The pre-processing is complete. E_FAIL The
	 * pre-processing has failed.
	 */
	protected Result netwPreProcessing(byte[] data, Reference<IOperation> poperation, Reference<Boolean> isInvoke) {
		Result res = Result.S_OK;

		IOperation op = null;

		try {
			if (getSrvProxyInform() != null && getSrvProxyInform().getTranslator() != null) {
				op = getSrvProxyInform().getTranslator().decode(data, isInvoke);
			} else {

				// assume BIND so create a temporary translator to process the bind
				if (this.translator == null)
					this.translator = new PDUTranslator();

				op = this.translator.decode(data, isInvoke);
			}
			poperation.setReference(op);
		} catch (ApiResultException e) {
			if (e.getResult() == Result.SLE_E_INVALIDPDU) {
				// the pdu is not the expected one
				String mess = PeerAbortDiagnostics.ENCODING_ERROR.toString() + " : " + e.getMessage();
				notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_localAbort, 1004, mess, null);

				if (this.role == BindRole.BR_responder && this.state == AssocState.sleAST_unbound) {
					// the first received pdu is not a bind
					// reset the connection
					this.objMutex.unlock();
					this.channelInitiate.sendReset();
					this.objMutex.lock();

					// cleanup
					discardAllInvocationPdu();
					clearAllPendingReturn();

					// delete the association
					releaseAssociation();
				} else {
					doAbort(PeerAbortDiagnostics.ENCODING_ERROR, AbortOriginator.INTERNAL, true);
				}
			} else if (e.getResult() == Result.E_FAIL) {
				String mess = PeerAbortDiagnostics.ENCODING_ERROR.toString() + " : " + e.getMessage();
				notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_localAbort, 1004, mess, null);

				doAbort(PeerAbortDiagnostics.ENCODING_ERROR, AbortOriginator.INTERNAL, true);
			} else if (e.getResult() == Result.E_PENDING) {
				doAbort(PeerAbortDiagnostics.UNSOLICITED_INVOKE_ID, AbortOriginator.INTERNAL, true);
			} else {
				// res == S_OK
				// for bind invoke pdu, the authenticate is done after
				// location

				if (IBind.class.isAssignableFrom(poperation.getClass()) || !isInvoke.getReference()) {
					// authenticate the PDU
					if (!authenticate(poperation.getReference(), isInvoke.getReference())) {
						// generate an authentication alarm
						String tmp = poperation.getReference().print(512);
						String mess = "Operation rejected. Authentication failure. " + tmp;
						notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_authFailure, 1002, mess, null);

						if (IBind.class.isAssignableFrom(poperation.getClass())
								|| IUnbind.class.isAssignableFrom(poperation.getClass())) {
							// unbind return must be ignored
							if (!(IUnbind.class.isAssignableFrom(poperation.getClass()) && !isInvoke.getReference())) {

								if (this.role == BindRole.BR_responder) {
									if (IBind.class.isAssignableFrom(poperation.getClass())) {
										// responder side, authenticate fails
										// for bind
										// invoke
										// reset the connection
										this.objMutex.unlock();
										this.channelInitiate.sendReset();
										this.objMutex.lock();
									} else {
										// responder side, authenticate fails
										// for a
										// unbind invoke.
										// reset the connection and send peer
										// abort to
										// service element
										this.objMutex.unlock();
										this.channelInitiate.sendReset();
										this.objMutex.lock();
										doAbort(PeerAbortDiagnostics.OTHER_REASON, AbortOriginator.INTERNAL, false);
									}
								} else if (this.role == BindRole.BR_initiator
										&& IBind.class.isAssignableFrom(poperation.getClass())) {
									// initiator side. authenticate fails for
									// bind
									// return
									// reset the connection
									this.objMutex.unlock();
									this.channelInitiate.sendReset();
									this.objMutex.lock();
									// abort the connection, but do not send
									// peer abort
									// to peer
									doAbort(PeerAbortDiagnostics.OTHER_REASON, AbortOriginator.INTERNAL, false);
								} else {
									// abort the connection
									doAbort(PeerAbortDiagnostics.OTHER_REASON, AbortOriginator.INTERNAL, true);
								}

								// cleanup
								discardAllInvocationPdu();
								clearAllPendingReturn();

								if (this.role == BindRole.BR_responder) {
									// delete the association
									releaseAssociation();
								}
							}
						}
					}
				}
			}

			res = Result.E_FAIL;
		} catch (IOException e) {
			LOG.log(Level.FINE, "IOException ", e);
			res = Result.E_FAIL;
		} catch (ApiException e) {
			LOG.log(Level.FINE, "ApiException ", e);
			res = Result.E_FAIL;
		}

		// check the PDU type
		if (res != Result.S_OK) {
			// abort the association
			doAbort(PeerAbortDiagnostics.ENCODING_ERROR, AbortOriginator.INTERNAL, true);
		}

		return res;
	}

	/**
	 * Performs pre-processing of PEER-ABORT event received from the network
	 * interface, as specified in the state-table. S_OK The pre-processing is
	 * complete. E_FAIL The pre-processing has failed.
	 */
	protected IOperation netwPreProcessing(int peerabortDiag, boolean peerabortOriginatorIsLocal) {
		AbortOriginator abortOriginator;

		if (peerabortOriginatorIsLocal) {
			abortOriginator = AbortOriginator.INTERNAL;
		} else {
			abortOriginator = AbortOriginator.PEER;
		}

		IOperation poperation = null;

		try {
			poperation = getSrvProxyInform().getTranslator().decode(peerabortDiag, null, abortOriginator);
		} catch (ApiException e) {
			doAbort(PeerAbortDiagnostics.ENCODING_ERROR, AbortOriginator.INTERNAL, true);
			String mess = PeerAbortDiagnostics.ENCODING_ERROR.toString() + " : " + e.getMessage();
			notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_localAbort, 1004, mess, null);
		}

		return poperation;
	}

	/**
	 * Performs pre-processing of events received from the client interface, as
	 * specified in the state-table. S_OK The pre-processing is complete.
	 * SLE_E_SEQUENCE Sequence count out of acceptable window. SLE_E_INVALIDPDU The
	 * operation is not supported for the service type. SLE_E_ABORTED The
	 * association has been aborted. SLE_E_OVERFLOW The queuing capability has been
	 * exceeded. E_FAIL The pre-processing has failed.
	 */
	protected Result clientPreProcessing(IOperation pOperation, long seqCount) {
		if (this.isAborted && this.role == BindRole.BR_responder) {
			return Result.SLE_E_ABORTED;
		}

		this.objMutex.unlock();
		this.innerLock.unlock();

		Result res = this.oPSequencer.serialise(pOperation, seqCount);

		this.innerLock.lock();
		this.objMutex.lock();

		if (res == Result.SLE_E_SEQUENCE) {
			// the sequence counter is out of window size
			return Result.SLE_E_SEQUENCE;
		} else if (res == Result.SLE_E_ABORTED) {
			// association is aborted
			return Result.SLE_E_ABORTED;
		}

		// check if the sending queue is full
		if (this.iOperation.size() >= this.queueSize && !IPeerAbort.class.isAssignableFrom(pOperation.getClass())) {

			String messop = pOperation.print(512);
			LOG.finer(messop);

			return Result.SLE_E_OVERFLOW;
		}

		return Result.S_OK;
	}

	/**
	 * Performs post-processing of events received from the client interface, as
	 * specified in the state-table. This method is called only if the
	 * pre-processing and the state-processing have return ok. SLE_S_TRANSMITTED The
	 * pdu has been passed to the communication system for transmission.
	 * SLE_S_QUEUED The pdu has been queud locally. SLE_E_COMMS The communication
	 * system has failed. E_FAIL The post-processing has failed.
	 */
	protected Result clientPostProcessing(IOperation pOperation, boolean reportTransmission, boolean isInvoke,
			boolean lastPdu) {

		Result res = Result.E_FAIL;

		if (this.channelInitiate == null) {
			return Result.SLE_S_QUEUED;
		}

		insertSecurityAttributes(pOperation, isInvoke);

		if (IBind.class.isAssignableFrom(pOperation.getClass())) {
			// insertion of local application identifier
			String localId = this.api.getProxySettings().getLocalId();
			IBind pBind = (IBind) pOperation;
			if (pBind != null) {
				pBind.setInitiatorIdentifier(localId);
			}
		}

		if (!this.suspendXmit) {
			// encode and send the pdu immediately
			byte[] data = null;
			try {
				data = this.api.getTranslator(pOperation.getServiceInstanceIdentifier()).encode(pOperation, isInvoke);
				this.suspendXmit = true;
				dumpPdu(false, data);
				this.objMutex.unlock();
				if (this.channelInitiate != null) {
					this.channelInitiate.sendSLEPDU(data, lastPdu);
				}
				this.objMutex.lock();
				res = Result.SLE_S_TRANSMITTED;
			} catch (ApiException | IOException e) {
				String mess = PeerAbortDiagnostics.ENCODING_ERROR.toString() + " : " + e.getMessage();
				notify(CstsLogMessageType.ALARM, AlarmLevel.sleAL_localAbort, 1004, mess, null);
			}
		} else {
			SPLOperation theOp = new SPLOperation();

			String messop = pOperation.print(512);
			LOG.finer(messop);

			theOp.setpOperation(pOperation);
			theOp.setInvoke(isInvoke);
			theOp.setLastPdu(lastPdu);
			theOp.setReportTransmission(reportTransmission);

			// queue the pdu
			this.iOperation.addLast(theOp);
			res = Result.SLE_S_QUEUED;
		}

		return res;
	}

	/**
	 * Notify an alarm.
	 */
	protected void notify(CstsLogMessageType type, AlarmLevel alarm, int messId, String text,
			IServiceInstanceIdentifier psii) {
		if (this.reporter != null) {
			if (psii == null) {
				if (this.srvProxyInform != null) {
					IServiceConfiguration pSIAdmin = (IServiceConfiguration) this.srvProxyInform;

					if (pSIAdmin != null) {
						psii = pSIAdmin.getServiceInstanceIdentifier();
					}
				}

				this.reporter.notifyApplication(psii, type, text);
				this.reporter.logRecord(null, null, Component.CP_proxy, alarm, type, text);
			}
		}
	}

	/**
	 * Sets the authentication mode and the security attributes to the association.
	 */
	protected void setSecurityAttributes(String peerId) {
		if (peerId == null) {
			if (this.iSecAttr != null) {
				this.iSecAttr = null;
			}
			return;
		}

		if (this.iSecAttr == null) {
			ISecAttributes pIsleSecAtt = null;

			pIsleSecAtt = this.api.createSecAttributes();

			String username = null;
			byte[] passwd = null;

			this.iSecAttr = pIsleSecAtt;

			// set the authentication mode: always taken in the peer-application
			ArrayList<RemotePeer> pPeerAppliDataList = this.api.getPeerList();
			RemotePeer pPeerAppl = null;
			for (RemotePeer peer : pPeerAppliDataList) {

				if (peer.getId() == peerId)
					pPeerAppl = peer;

			}
			if (pPeerAppl != null) {
				this.authenticationMode = pPeerAppl.getAuthenticationMode();

				// set the user name and password
				// take the user name and password in the peer application
				username = pPeerAppl.getId();
				passwd = pPeerAppl.getPassword().getBytes();
			}

			this.iSecAttr.setUserName(username);
			this.iSecAttr.setPassword(passwd);
		}

	}

	/**
	 * Changes the state of the association and performs a trace if necessary.
	 */
	protected void changeState(SPLEvent event, AssocState newState) {
		AssocState oldState = this.state;

		if (oldState == newState) {
			return;
		}

		this.state = newState;

		IServiceInstanceIdentifier psii = null;
		IServiceConfiguration pSiAdmin = (IServiceConfiguration) this.srvProxyInform;

		if (this.srvProxyInform != null && pSiAdmin != null) {
			psii = pSiAdmin.getServiceInstanceIdentifier();
		}

		LOG.fine(psii.toString() + event.toString() + oldState.toString() + newState.toString());

	}

	/**
	 * Dump the content of the PDU by performing a trace.
	 */
	public void dumpPdu(boolean rcv_from_network, byte[] pdu) {
		// TODO CHECK
		IServiceInstanceIdentifier psii = null;
		if (this.srvProxyInform != null) {
			IServiceConfiguration pSiAdmin = (IServiceConfiguration) this.srvProxyInform;
			if (this.srvProxyInform != null && pSiAdmin != null) {
				psii = pSiAdmin.getServiceInstanceIdentifier();
			}
		}
		if (psii == null) {
			LOG.finest("PDU: " + pdu.toString());
		} else {
			LOG.finest("PDU: " + pdu.toString() + " - SIID: " + psii.toString());
		}
	}

	/**
	 * Release the Channel interface.
	 */
	public void releaseChannel() {
		if (this.channelInitiate != null) {
			this.channelInitiate.dispose();
			this.channelInitiate = null;
		}
	}

	/**
	 * Inserts the credentials in the operation.
	 */
	private void insertCredentialsInOp(IOperation poperation, ICredentials pcredentials) {
		// TODO OT_transferBuffer same as processdata?
		if (IProcessData.class.isAssignableFrom(poperation.getClass())) {
			IOperation pCurrentOp = null;

			// set the credentials for the transfer buffer op
			poperation.setInvokerCredentials(pcredentials);

			IProcessData pTransferBufferOperation = (IProcessData) poperation;
			/*
			if (pTransferBufferOperation != null) {
				// for all the operation of the transfer buffer
				pTransferBufferOperation.reset();
				while (pTransferBufferOperation.moreData()) {
					pCurrentOp = pTransferBufferOperation.next();
					pCurrentOp.setInvokerCredentials(pcredentials);
				}
			}
			*/
		} else {
			poperation.setInvokerCredentials(pcredentials);
		}
	}

	/**
	 * Inserts the credentials in the operation.
	 */
	private void insertCredentialsInConfOp(IConfirmedOperation poperation, ICredentials pcredentials) {
		poperation.setPerformerCredentials(pcredentials);
	}

	/**
	 * If authentication is required, authentication for one operation.
	 */
	private boolean authenticateOperation(IOperation poperation, boolean isInvoke, int delay) {
		ICredentials pOpCredentials = null;
		boolean res = false;

		if (LOG.isLoggable(Level.FINEST)) {
			LOG.finest("Authenticate Operation: Invoke=" + isInvoke + ", operation =" + poperation.toString()
					+ " delay=" + delay);
		}

		if (isInvoke) {
			// TODO OT_transferBuffer same as processdata?
			/*
			 * if (IProcessData.class.isAssignableFrom(poperation.getClass())) { IOperation
			 * pCurrentOp = null; IProcessData pTransferBufferOperation =(IProcessData)
			 * poperation; if (pTransferBufferOperation != null) { // for all the operation
			 * of the transfer buffer pTransferBufferOperation.reset(); while
			 * (pTransferBufferOperation.moreData()) { pCurrentOp =
			 * pTransferBufferOperation.next(); pOpCredentials =
			 * pCurrentOp.getInvokerCredentials(); if (pOpCredentials == null) { return
			 * false; } res = this.iSecAttr.authenticate(pOpCredentials, delay); if (!res) {
			 * break; } } } } else
			 */
			{
				pOpCredentials = poperation.getInvokerCredentials();
				if (pOpCredentials == null) {
					if (LOG.isLoggable(Level.FINEST)) {
						LOG.finest("Operation Credentials NULL");
					}
					return false;
				}

				res = this.iSecAttr.authenticate(pOpCredentials, delay);
			}
		} else {
			IConfirmedOperation pConfOp = (IConfirmedOperation) poperation;
			if (pConfOp != null) {
				pOpCredentials = pConfOp.getPerformerCredentials();
			}

			if (pOpCredentials == null) {
				return false;
			}

			res = this.iSecAttr.authenticate(pOpCredentials, delay);
		}

		return res;
	}

	/**
	 * Initialises the association.
	 */
	protected void initAssoc() {
		this.isAborted = false;
		this.isReleased = false;
		this.suspendXmit = true;
		setSecurityAttributes(null);
		this.sequenceCounter = 1;
		this.authenticationMode = AuthenticationMode.NONE;
	}

	/**
	 * Increments the sequence counting.
	 */
	protected long incSeqCounter() {
		return this.sequenceCounter++;
	}

	protected AssocState getState() {
		return this.state;
	}

	public boolean isUnboundStateIsDisconnected() {
		return this.unboundStateIsDisconnected;
	}

	public void setUnboundStateIsDisconnected(boolean unboundStateIsDisconnected) {
		this.unboundStateIsDisconnected = unboundStateIsDisconnected;
	}

	protected long getSequenceCounter() {
		return this.sequenceCounter;
	}

	protected void setSequenceCounter(long sequenceCounter) {
		this.sequenceCounter = sequenceCounter;
	}

	public boolean isSuspendXmit() {
		return this.suspendXmit;
	}

	public void setSuspendXmit(boolean suspendXmit) {
		this.suspendXmit = suspendXmit;
	}

	public IReporter getReporter() {
		return this.reporter;
	}

	public void setReporter(IReporter reporter) {
		this.reporter = reporter;
	}

	public IApi getApi() {
		return this.api;
	}

	public void setApi(IApi api) {
		this.api = api;
	}

	public AuthenticationMode getAuthenticationMode() {
		return this.authenticationMode;
	}

	public void setAuthenticationMode(AuthenticationMode authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public boolean isAborted() {
		return this.isAborted;
	}

	public void setAborted(boolean isAborted) {
		this.isAborted = isAborted;
	}

	public boolean isReleased() {
		return this.isReleased;
	}

	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}

	public int getQueueSize() {
		return this.queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	protected ISrvProxyInform getSrvProxyInform() {
		return this.srvProxyInform;
	}

	public void setRole(BindRole role) {
		this.role = role;
	}

}
