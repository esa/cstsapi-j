package esa.egos.csts.api.operations.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerEmbeddedPdv.Identification;
import org.openmuc.jasn1.ber.types.BerOctetString;

import ccsds.csts.common.operations.pdus.OidValues;
import ccsds.csts.common.operations.pdus.StartDiagnosticExt;
import ccsds.csts.common.operations.pdus.StartInvocation;
import ccsds.csts.common.operations.pdus.StartReturn;
import ccsds.csts.common.types.AdditionalText;
import ccsds.csts.common.types.Embedded;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.diagnostics.Diagnostic;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.StartDiagnostic;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * The START operation (confirmed)
 */
public class Start extends AbstractConfirmedOperation implements IStart {

	private final OperationType type = OperationType.START;
	
	private final static int versionNumber = 1;

	private StartDiagnostic startDiagnostic;

	public Start() {
		super(versionNumber);
	}
	
	@Override
	public OperationType getType() {
		return type;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public StartDiagnostic getStartDiagnostics() {
		return startDiagnostic;
	}

	@Override
	public void setStartDiagnostics(StartDiagnostic startDiagnostics) {
		this.startDiagnostic = startDiagnostics;
	}

	@Override
	public String print(int i) {
		return "Start [startDiagnostics=" + startDiagnostic + "]";
	}

	@Override
	public StartInvocation encodeStartInvocation() {
		return encodeStartInvocation(CSTSUtils.nonUsedExtension());
	}

	@Override
	public StartInvocation encodeStartInvocation(Extended extension) {
		StartInvocation startInvocation = new StartInvocation();
		startInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		startInvocation.setStartInvocationExtension(extension);
		return startInvocation;
	}

	@Override
	public StartReturn encodeStartReturn() {
		return encodeStandardReturnHeader(StartReturn.class);
	}

	@Override
	public StartReturn encodeStartReturn(Extended resultExtension) {
		return encodeStandardReturnHeader(StartReturn.class, resultExtension);
	}

	@Override
	protected void encodeDiagnosticExtension() {
		if (startDiagnostic != null) {
			Embedded diagnosticExtension = new Embedded();
			Identification identification = new Identification();
			identification.setSyntax(OidValues.startDiagnosticExt);
			diagnosticExtension.setIdentification(identification);
			diagnosticExtension.setDataValue(new BerOctetString(encodeStartDiagnosticExt().code));
			setDiagnostic(new Diagnostic(diagnosticExtension));
		}
	}

	protected StartDiagnosticExt encodeStartDiagnosticExt() {
		StartDiagnosticExt startDiagnosticExt = new StartDiagnosticExt();
		if (startDiagnostic == StartDiagnostic.UNABLE_TO_COMPLY) {
			startDiagnosticExt
					.setUnableToComply(new AdditionalText(startDiagnostic.toString().getBytes(StandardCharsets.UTF_8)));
		} else if (startDiagnostic == StartDiagnostic.OUT_OF_SERVICE) {
			startDiagnosticExt
					.setOutOfService(new AdditionalText(startDiagnostic.toString().getBytes(StandardCharsets.UTF_8)));
		}
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			startDiagnosticExt.encode(os);
			startDiagnosticExt.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return startDiagnosticExt;
	}

	@Override
	public void decodeStartInvocation(StartInvocation startInvocation) {
		decodeStandardInvocationHeader(startInvocation.getStandardInvocationHeader());
	}

	@Override
	public void decodeStartReturn(StartReturn startReturn) {
		decodeStandardReturnHeader(startReturn);
	}

	@Override
	protected void decodeDiagnosticExtension() {
		if (getDiagnostic().getDiagnosticExtension() != null) {
			if (CSTSUtils.equalsIdentifier(getDiagnostic().getDiagnosticExtension(),
					OidValues.startDiagnosticExt)) {
				StartDiagnosticExt startDiagnosticExt = new StartDiagnosticExt();
				try (ByteArrayInputStream is = new ByteArrayInputStream(
						getDiagnostic().getDiagnosticExtension().getDataValue().value)) {
					startDiagnosticExt.decode(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (startDiagnosticExt.getUnableToComply() != null) {
					startDiagnostic = StartDiagnostic.UNABLE_TO_COMPLY;
				} else if (startDiagnosticExt.getOutOfService() != null) {
					startDiagnostic = StartDiagnostic.OUT_OF_SERVICE;
				}
			}
		}
	}

}
