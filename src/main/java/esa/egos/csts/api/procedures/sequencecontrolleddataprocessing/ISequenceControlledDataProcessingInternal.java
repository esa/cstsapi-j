package esa.egos.csts.api.procedures.sequencecontrolleddataprocessing;

import java.util.HashMap;

import esa.egos.csts.api.diagnostics.SeqControlledDataProcDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.procedures.dataprocessing.IDataProcessingInternal;
import esa.egos.csts.api.types.ConditionalTime;

public interface ISequenceControlledDataProcessingInternal extends ISequenceControlledDataProcessing, IDataProcessingInternal {

	void setDiagnostics(SeqControlledDataProcDiagnostics diagnostics);

	SeqControlledDataProcDiagnostics getDiagnostics();

	EmbeddedData encodeProcessDataDiagnosticExt();

	void setFirstDataUnitId(long firstDataUnitId);

	long getFirstDataUnitId();
	
	void reset() throws InterruptedException;

	boolean verifyDataUnitId(IConfirmedProcessData processData);

	EmbeddedData encodeProcessDataNegReturnExtension();

	EmbeddedData encodeProcessDataPosReturnExtension();

	ConditionalTime getLatestDataProcessingTime();

	ConditionalTime getEarliestDataProcessingTime();

	boolean verifyConsistentTimeRange();

	void notifyLocked();

	HashMap<IConfirmedProcessData, ConditionalTime> getLatestDataProcessingTimeMap();

	HashMap<IConfirmedProcessData, ConditionalTime> getEarliestDataProcessingTimeMap();
	
}
