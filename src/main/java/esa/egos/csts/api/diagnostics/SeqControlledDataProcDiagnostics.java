package esa.egos.csts.api.diagnostics;

import java.io.IOException;

import org.openmuc.jasn1.ber.BerByteArrayOutputStream;
import org.openmuc.jasn1.ber.types.BerNull;

import ccsds.csts.sequence.controlled.data.processing.pdus.SequContrDataProcProcDataDiagnosticExt;
import esa.egos.csts.api.extensions.EmbeddedData;

/**
 * This class represents the PROCESS-DATA Diagnostic of a Sequence-Controlled
 * Data Processing Procedure in case a negative result must be returned.
 */
public class SeqControlledDataProcDiagnostics {

	private SeqControlledDataProcDiagnosticsType type;

	private final EmbeddedData diagnosticExtension;

	/**
	 * Instantiates a new Sequence-Controlled Data Processing PROCESS-DATA
	 * Diagnostic with its specified type.
	 * 
	 * @param type the Sequence-Controlled Data Processing PROCESS-DATA Diagnostic
	 *             type
	 */
	public SeqControlledDataProcDiagnostics(SeqControlledDataProcDiagnosticsType type) {
		this.type = type;
		diagnosticExtension = null;
	}

	/**
	 * Instantiates a new extended Sequence-Controlled Data Processing PROCESS-DATA
	 * Diagnostic.
	 * 
	 * @param diagnosticExtension the Sequence-Controlled Data Processing
	 *                            PROCESS-DATA Diagnostic extension
	 */
	public SeqControlledDataProcDiagnostics(EmbeddedData diagnosticExtension) {
		type = SeqControlledDataProcDiagnosticsType.EXTENDED;
		this.diagnosticExtension = diagnosticExtension;
	}

	/**
	 * Returns the Diagnostic type.
	 * 
	 * @return the Diagnostic type
	 */
	public SeqControlledDataProcDiagnosticsType getType() {
		return type;
	}

	/**
	 * Indicates whether this Diagnostic is extended.
	 * 
	 * @return true is this Diagnostic is extended, false otherwise
	 */
	public boolean isExtended() {
		return type == SeqControlledDataProcDiagnosticsType.EXTENDED;
	}

	/**
	 * Returns the Diagnostic extension.
	 * 
	 * @return the Diagnostic extension
	 */
	public EmbeddedData getDiagnosticExtension() {
		return diagnosticExtension;
	}

	/**
	 * Encodes this Sequence-Controlled Data Processing PROCESS-DATA Diagnostic into
	 * a CCSDS BuffDataDelStartDiagnosticExt type.
	 * 
	 * @return the CCSDS SequContrDataProcProcDataDiagnosticExt type representing
	 *         this Diagnostic
	 */
	public SequContrDataProcProcDataDiagnosticExt encode() {
		SequContrDataProcProcDataDiagnosticExt diagnostics = new SequContrDataProcProcDataDiagnosticExt();
		switch (type) {
		case DATA_ERROR:
			diagnostics.setDataError(new BerNull());
			break;
		case INCONSISTENT_TIME_RANGE:
			diagnostics.setInconsistentTimeRange(new BerNull());
			break;
		case INVALID_TIME:
			diagnostics.setInvalidTime(new BerNull());
			break;
		case LATE_DATA:
			diagnostics.setLateData(new BerNull());
			break;
		case OUT_OF_SEQUENCE:
			diagnostics.setOutOfSequence(new BerNull());
			break;
		case SERVICE_INSTANCE_LOCKED:
			diagnostics.setServiceInstanceLocked(new BerNull());
			break;
		case UNABLE_TO_PROCESS:
			diagnostics.setUnableToProcess(new BerNull());
			break;
		case UNABLE_TO_STORE:
			diagnostics.setUnableToStore(new BerNull());
			break;
		case EXTENDED:
			diagnostics.setSequContrDataProcProcDataDiagnosticExtExtension(diagnosticExtension.encode());
			break;
		}
		// encode with a resizable output stream and an initial capacity of 128 bytes
		try (BerByteArrayOutputStream os = new BerByteArrayOutputStream(128, true)) {
			diagnostics.encode(os);
			diagnostics.code = os.getArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return diagnostics;
	}

	/**
	 * Decodes a specified CCSDS SeqControlledDataProcDiagnostics type.
	 * 
	 * @param diagnostics the specified CCSDS SeqControlledDataProcDiagnostics type
	 * @return a new Sequence-Controlled Data Processing PROCESS-DATA decoded from
	 *         the specified CCSDS BuffDataDelStartDiagnosticExt type
	 */
	public static SeqControlledDataProcDiagnostics decode(SequContrDataProcProcDataDiagnosticExt diagnostics) {

		SeqControlledDataProcDiagnostics seqControlledDataProcDiagnostics = null;
		if (diagnostics.getDataError() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.DATA_ERROR);
		} else if (diagnostics.getInconsistentTimeRange() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.INCONSISTENT_TIME_RANGE);
		} else if (diagnostics.getInvalidTime() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.INVALID_TIME);
		} else if (diagnostics.getLateData() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.LATE_DATA);
		} else if (diagnostics.getOutOfSequence() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.OUT_OF_SEQUENCE);
		} else if (diagnostics.getServiceInstanceLocked() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.SERVICE_INSTANCE_LOCKED);
		} else if (diagnostics.getUnableToProcess() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.UNABLE_TO_PROCESS);
		} else if (diagnostics.getUnableToStore() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					SeqControlledDataProcDiagnosticsType.UNABLE_TO_STORE);
		} else if (diagnostics.getSequContrDataProcProcDataDiagnosticExtExtension() != null) {
			seqControlledDataProcDiagnostics = new SeqControlledDataProcDiagnostics(
					EmbeddedData.decode(diagnostics.getSequContrDataProcProcDataDiagnosticExtExtension()));
		}
		return seqControlledDataProcDiagnostics;
	}

}
