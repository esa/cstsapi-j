package esa.egos.csts.api.procedures.throwevent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;

import b1.ccsds.csts.throw_.event.pdus.TeExecDirNegReturnDiagnosticExt;
import b1.ccsds.csts.throw_.event.pdus.ThrowEventPdu;
import esa.egos.csts.api.diagnostics.ThrowEventDiagnostic;
import esa.egos.csts.api.directives.DirectiveQualifier;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.AbstractStatefulProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.states.throwevent.Inactive;
import esa.egos.csts.api.states.throwevent.ThrowEventState;

public abstract class AbstractThrowEvent extends AbstractStatefulProcedure implements IThrowEventInternal {

	private static final ProcedureType TYPE = ProcedureType.of(OIDs.throwEvent);
	
	private static final int VERSION = 1;
	
	private final Queue<IExecuteDirective> queue;
	
	private ThrowEventDiagnostic diagnostic;
	
	public AbstractThrowEvent() {
		queue = new LinkedList<>();
	}
	
	@Override
	public ProcedureType getType() {
		return TYPE;
	}
	
	@Override
	public int getVersion() {
		return VERSION;
	}

	@Override
	protected void initOperationTypes() {
		addSupportedOperationType(OperationType.EXECUTE_DIRECTIVE);
	}
	
	@Override
	public void terminate() {
		setState(new Inactive(this));
		queue.clear();
	}
	
	@Override
	public ThrowEventDiagnostic getThrowEventDiagnostic() {
		return diagnostic;
	}
	
	@Override
	public void setThrowEventDiagnostic(ThrowEventDiagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}
	
	@Override
	public Queue<IExecuteDirective> getQueue() {
		return queue;
	}
	
	@Override
	public void queueDirective(IExecuteDirective executeDirective) {
		queue.add(executeDirective);
	}
	
	@Override
	public void removeDirective(IExecuteDirective executeDirective) {
		queue.remove(executeDirective);
	}
	
	@Override
	public boolean hasNoFurtherOperationsWaiting() {
		return queue.isEmpty();
	}
	
	@Override
	public CstsResult requestExecution(ObjectIdentifier directiveIdentifier, DirectiveQualifier directiveQualifier) {
		IExecuteDirective executeDirective = createExecuteDirective();
		executeDirective.setDirectiveIdentifier(directiveIdentifier);
		executeDirective.setDirectiveQualifier(directiveQualifier);
		return forwardInvocationToProxy(executeDirective);
	}
	
	@Override
	public void acknowledgeDirective(IExecuteDirective executeDirective) {
		executeDirective.setAcknowledgement(true);
		executeDirective.setPositiveResult();
		((ThrowEventState) getState()).process(executeDirective, false);
	}
	
	@Override
	public void declineDirective(IExecuteDirective executeDirective) {
		executeDirective.setAcknowledgement(true);
		executeDirective.setNegativeResult();
		((ThrowEventState) getState()).process(executeDirective, false);
	}
	
	@Override
	public void actionCompletedSuccesfully(IExecuteDirective executeDirective) {
		executeDirective.setAcknowledgement(false);
		executeDirective.setPositiveResult();
		((ThrowEventState) getState()).process(executeDirective, false);
	}
	
	@Override
	public void actionCompletedUnsuccesfully(IExecuteDirective executeDirective) {
		executeDirective.setAcknowledgement(false);
		executeDirective.setDiagnostic(encodeExecuteDirectiveDiagnosticExt());
		((ThrowEventState) getState()).process(executeDirective, false);
	}
	
	public EmbeddedData encodeExecuteDirectiveDiagnosticExt() {
		return EmbeddedData.of(OIDs.teExecDirDiagExt, diagnostic.encode().code);
	}
	
	@Override
	public CstsResult informOperationReturn(IConfirmedOperation confOperation) {
		if (confOperation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) confOperation;
			if (executeDirective.getExecDirReturnDiagnostic() != null) {
				decodeExecDirNegReturnDiagnosticExt(executeDirective.getExecDirReturnDiagnostic().getDiagnosticExtension());
			}
		}
		return doInformOperationReturn(confOperation);
	}
	
	protected void decodeExecDirNegReturnDiagnosticExt(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.teExecDirDiagExt)) {
			TeExecDirNegReturnDiagnosticExt teExecDirNegReturnDiagnosticExt = new TeExecDirNegReturnDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				teExecDirNegReturnDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			diagnostic = ThrowEventDiagnostic.decode(teExecDirNegReturnDiagnosticExt);
		}
	}
	
	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		
		byte[] encodedOperation;
		ThrowEventPdu pdu = new ThrowEventPdu();
		
		if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (isInvoke) {
				pdu.setExecuteDirectiveInvocation(executeDirective.encodeExecuteDirectiveInvocation());
			} else {
				if (executeDirective.isAcknowledgement()) {
					pdu.setExecuteDirectiveAcknowledge(executeDirective.encodeExecuteDirectiveAcknowledge());
				} else {
					pdu.setExecuteDirectiveReturn(executeDirective.encodeExecuteDirectiveReturn());
				}
			}
		}
		
		try (ReverseByteArrayOutputStream berBAOStream = new ReverseByteArrayOutputStream(10, true)) {
			pdu.encode(berBAOStream);
			encodedOperation = berBAOStream.getArray();
		}
		
		return encodedOperation;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws IOException {

		ThrowEventPdu pdu = new ThrowEventPdu();
		
		try (ByteArrayInputStream is = new ByteArrayInputStream(encodedPdu)) {
			pdu.decode(is);
		}
		
		IExecuteDirective executeDirective = createExecuteDirective();
		
		if (pdu.getExecuteDirectiveInvocation() != null) {
			executeDirective.decodeExecuteDirectiveInvocation(pdu.getExecuteDirectiveInvocation());
		} else if (pdu.getExecuteDirectiveAcknowledge() != null) {
			executeDirective.decodeExecuteDirectiveAcknowledge(pdu.getExecuteDirectiveAcknowledge());
			executeDirective.setAcknowledgement(true);
		} else if (pdu.getExecuteDirectiveReturn() != null) {
			executeDirective.decodeExecuteDirectiveReturn(pdu.getExecuteDirectiveReturn());
			executeDirective.setAcknowledgement(false);
		}
		
		return executeDirective;
	}

}
