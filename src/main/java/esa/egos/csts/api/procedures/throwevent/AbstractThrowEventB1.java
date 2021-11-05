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


public abstract class AbstractThrowEventB1 extends AbstractThrowEvent{

	
	public EmbeddedData encodeExecuteDirectiveDiagnosticExt() {
		return EmbeddedData.of(OIDs.teExecDirDiagExt, getThrowEventDiagnostic().encode(new b1.ccsds.csts.throw_.event.pdus.TeExecDirNegReturnDiagnosticExt()).code);
	}
	
	
	protected void decodeExecDirNegReturnDiagnosticExt(EmbeddedData embeddedData) {
		if (embeddedData.getOid().equals(OIDs.teExecDirDiagExt)) {
			TeExecDirNegReturnDiagnosticExt teExecDirNegReturnDiagnosticExt = new TeExecDirNegReturnDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData())) {
				teExecDirNegReturnDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setThrowEventDiagnostic(ThrowEventDiagnostic.decode(teExecDirNegReturnDiagnosticExt));
		}
	}
	
	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws IOException {
		
		byte[] encodedOperation;
		ThrowEventPdu pdu = new ThrowEventPdu();
		
		if (operation.getType() == OperationType.EXECUTE_DIRECTIVE) {
			IExecuteDirective executeDirective = (IExecuteDirective) operation;
			if (isInvoke) {
				pdu.setExecuteDirectiveInvocation((b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation)executeDirective.encodeExecuteDirectiveInvocation());
			} else {
				if (executeDirective.isAcknowledgement()) {
					pdu.setExecuteDirectiveAcknowledge((b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveAcknowledge)executeDirective.encodeExecuteDirectiveAcknowledge());
				} else {
					pdu.setExecuteDirectiveReturn((b1.ccsds.csts.common.operations.pdus.ExecuteDirectiveReturn)executeDirective.encodeExecuteDirectiveReturn());
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
