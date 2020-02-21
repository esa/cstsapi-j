package esa.egos.csts.api.operations.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import b1.ccsds.csts.common.operations.pdus.StartDiagnosticExt;
import b1.ccsds.csts.common.operations.pdus.StartInvocation;
import b1.ccsds.csts.common.operations.pdus.StartReturn;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.diagnostics.StartDiagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IStart;

/**
 * This class represents a START operation.
 */
public class Start extends AbstractConfirmedOperation implements IStart {

	private static final OperationType TYPE = OperationType.START;

	/**
	 * The diagnostic (negative return)
	 */
	private StartDiagnostic startDiagnostic;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor method of a START operation.
	 */
	public Start() {
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public StartDiagnostic getStartDiagnostic() {
		return startDiagnostic;
	}

	@Override
	public void setStartDiagnostic(StartDiagnostic startDiagnostic) {
		this.startDiagnostic = startDiagnostic;
		setDiagnostic(new Diagnostic(encodeStartDiagnosticExt()));
	}

	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		sb.append("\nOperation                      : START\n");
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : true\n");
		sb.append("Diagnostic Type                : no diagnostic\n");
		sb.append("Common Diagnostics             : Invalid\n");

		return sb.toString();
	}

	@Override
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public StartInvocation encodeStartInvocation() {
		StartInvocation startInvocation = new StartInvocation();
		startInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		startInvocation.setStartInvocationExtension(invocationExtension.encode());
		return startInvocation;
	}

	@Override
	public void decodeStartInvocation(StartInvocation startInvocation) {
		decodeStandardInvocationHeader(startInvocation.getStandardInvocationHeader());
		invocationExtension = Extension.decode(startInvocation.getStartInvocationExtension());
	}

	@Override
	public StartReturn encodeStartReturn() {
		return encodeStandardReturnHeader(StartReturn.class);
	}

	private EmbeddedData encodeStartDiagnosticExt() {
		return EmbeddedData.of(OIDs.startDiagnosticExt, startDiagnostic.encode().code);
	}

	@Override
	public void decodeStartReturn(StartReturn startReturn) {
		decodeStandardReturnHeader(startReturn);
	}

	@Override
	protected void decodeNegativeReturn() {
		if (getDiagnostic().isExtended()) {
			decodeDiagnosticExtension();
		}
	}

	private void decodeDiagnosticExtension() {
		if (getDiagnostic().getDiagnosticExtension().getOid().equals(OIDs.startDiagnosticExt)) {
			StartDiagnosticExt startDiagnosticExt = new StartDiagnosticExt();
			try (ByteArrayInputStream is = new ByteArrayInputStream(
					getDiagnostic().getDiagnosticExtension().getData())) {
				startDiagnosticExt.decode(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			startDiagnostic = StartDiagnostic.decode(startDiagnosticExt);
		}
	}

	@Override
	public String toString() {
		return "Start [startDiagnostic=" + startDiagnostic + ", invocationExtension=" + invocationExtension + "]";
	}

}
