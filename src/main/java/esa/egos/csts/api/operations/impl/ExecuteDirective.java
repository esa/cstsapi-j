package esa.egos.csts.api.operations.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import ccsds.csts.common.operations.pdus.ExecDirNegAckDiagnosticExt;
import ccsds.csts.common.operations.pdus.ExecDirNegReturnDiagnosticExt;
import ccsds.csts.common.operations.pdus.ExecuteDirectiveAcknowledge;
import ccsds.csts.common.operations.pdus.ExecuteDirectiveInvocation;
import ccsds.csts.common.operations.pdus.ExecuteDirectiveReturn;
import ccsds.csts.common.types.PublishedIdentifier;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.ExecDirAcknowledgementDiagnostic;
import esa.egos.csts.api.diagnostics.ExecDirReturnDiagnostic;
import esa.egos.csts.api.directives.DirectiveQualifier;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.AbstractAcknowledgedOperation;
import esa.egos.csts.api.operations.IExecuteDirective;

/**
 * This class represents a EXECUTE-DIRECTIVE operation.
 */
public class ExecuteDirective extends AbstractAcknowledgedOperation implements IExecuteDirective {

	private static final OperationType TYPE = OperationType.EXECUTE_DIRECTIVE;

	/**
	 * The directive identifier
	 */
	private ObjectIdentifier directiveIdentifier;

	/**
	 * The directive qualifier
	 */
	private DirectiveQualifier directiveQualifier;

	/**
	 * The EXECUTE-DIRECTIVE acknowledgement diagnostic
	 */
	private ExecDirAcknowledgementDiagnostic execDirAcknowledgementDiagnostic;

	/**
	 * The EXECUTE-DIRECTIVE return diagnostic
	 */
	private ExecDirReturnDiagnostic execDirReturnDiagnostic;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of an EXECUTE-DIRECTIVE operation.
	 */
	public ExecuteDirective() {
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public boolean isBlocking() {
		return false;
	}

	@Override
	public ObjectIdentifier getDirectiveIdentifier() {
		return directiveIdentifier;
	}

	@Override
	public void setDirectiveIdentifier(ObjectIdentifier directiveIdentifier) {
		this.directiveIdentifier = directiveIdentifier;
	}

	@Override
	public DirectiveQualifier getDirectiveQualifier() {
		return directiveQualifier;
	}

	@Override
	public void setDirectiveQualifier(DirectiveQualifier directiveQualifier) {
		this.directiveQualifier = directiveQualifier;
	}

	@Override
	public ExecDirAcknowledgementDiagnostic getExecDirAcknowledgementDiagnostic() {
		return execDirAcknowledgementDiagnostic;
	}

	@Override
	public void setExecDirAcknowledgementDiagnostic(ExecDirAcknowledgementDiagnostic execDirAcknowledgementDiagnostic) {
		this.execDirAcknowledgementDiagnostic = execDirAcknowledgementDiagnostic;
		setDiagnostic(new Diagnostic(encodeExecDirAcknowledgementDiagnostic()));
	}

	@Override
	public ExecDirReturnDiagnostic getExecDirReturnDiagnostic() {
		return execDirReturnDiagnostic;
	}

	@Override
	public void setExecDirReturnDiagnostic(ExecDirReturnDiagnostic execDirReturnDiagnostic) {
		this.execDirReturnDiagnostic = execDirReturnDiagnostic;
		setDiagnostic(new Diagnostic(encodeExecDirReturnDiagnostic()));
	}

	@Override
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	public void setInvocationExtension(Extension invocationExtension) {
		this.invocationExtension = invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public ExecuteDirectiveInvocation encodeExecuteDirectiveInvocation() {
		ExecuteDirectiveInvocation invocation = new ExecuteDirectiveInvocation();
		invocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		invocation.setDirectiveIdentifier(new PublishedIdentifier(directiveIdentifier.toArray()));
		invocation.setDirectiveQualifier(directiveQualifier.encode());
		invocation.setExecuteDirectiveInvocationExtension(invocationExtension.encode());
		return invocation;
	}

	@Override
	public void decodeExecuteDirectiveInvocation(ExecuteDirectiveInvocation executeDirectiveInvocation) {
		decodeStandardInvocationHeader(executeDirectiveInvocation.getStandardInvocationHeader());
		directiveIdentifier = ObjectIdentifier.of(executeDirectiveInvocation.getDirectiveIdentifier().value);
		directiveQualifier = DirectiveQualifier.decode(executeDirectiveInvocation.getDirectiveQualifier());
		invocationExtension = Extension.decode(executeDirectiveInvocation.getExecuteDirectiveInvocationExtension());
	}

	@Override
	public ExecuteDirectiveAcknowledge encodeExecuteDirectiveAcknowledge() {
		return encodeStandardReturnHeader(ExecuteDirectiveAcknowledge.class);
	}

	@Override
	public void decodeExecuteDirectiveAcknowledge(ExecuteDirectiveAcknowledge executeDirectiveAcknowledge) {
		decodeStandardReturnHeader(executeDirectiveAcknowledge);
		setAcknowledgement(true);
	}

	@Override
	public ExecuteDirectiveReturn encodeExecuteDirectiveReturn() {
		return encodeStandardReturnHeader(ExecuteDirectiveReturn.class);
	}

	@Override
	public void decodeExecuteDirectiveReturn(ExecuteDirectiveReturn executeDirectiveReturn) {
		decodeStandardReturnHeader(executeDirectiveReturn);
		setAcknowledgement(false);
	}

	private EmbeddedData encodeExecDirAcknowledgementDiagnostic() {
		return EmbeddedData.of(OIDs.execDirAckDiagExt, execDirAcknowledgementDiagnostic.encode().code);
	}
	
	private EmbeddedData encodeExecDirReturnDiagnostic() {
		return EmbeddedData.of(OIDs.execDirNegReturnDiagnosticExt, execDirReturnDiagnostic.encode().code);
	}
	
	@Override
	protected void decodeNegativeReturn() {
		if (getDiagnostic().isExtended()) {
			decodeDiagnosticExtension();
		}
	}
	
	private void decodeDiagnosticExtension() {
		if (getDiagnostic().getDiagnosticExtension().getOid().equals(OIDs.execDirAckDiagExt)) {
			ExecDirNegAckDiagnosticExt execDirNegAckDiagnosticExt = new ExecDirNegAckDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(getDiagnostic().getDiagnosticExtension().getData())) {
				execDirNegAckDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			execDirAcknowledgementDiagnostic = ExecDirAcknowledgementDiagnostic.decode(execDirNegAckDiagnosticExt);
		} else if (getDiagnostic().getDiagnosticExtension().getOid().equals(OIDs.execDirNegReturnDiagnosticExt)) {
			ExecDirNegReturnDiagnosticExt execDirNegReturnDiagnosticExt = new ExecDirNegReturnDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(getDiagnostic().getDiagnosticExtension().getData())) {
				execDirNegReturnDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			execDirReturnDiagnostic = ExecDirReturnDiagnostic.decode(execDirNegReturnDiagnosticExt);
		}
	}
	
	@Override
	public String print(int i) {
		return toString();
	}

	@Override
	public String toString() {
		return "ExecuteDirective [directiveIdentifier=" + directiveIdentifier + ", directiveQualifier="
				+ directiveQualifier + ", invocationExtension=" + invocationExtension + "]";
	}

}
