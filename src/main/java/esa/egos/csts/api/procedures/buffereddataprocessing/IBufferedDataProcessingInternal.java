package esa.egos.csts.api.procedures.buffereddataprocessing;

import java.util.Collection;

import esa.egos.csts.api.enumerations.DataTransferMode;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.dataprocessing.IDataProcessingInternal;

public interface IBufferedDataProcessingInternal extends IBufferedDataProcessing, IDataProcessingInternal {

	IntegerConfigurationParameter getProcessingLatencyLimit();

	IntegerConfigurationParameter getMaximumForwardBufferSize();

	DataTransferMode getDataTransferMode();

	boolean isSufficientSpaceAvailable();

	void suspend() throws InterruptedException;
	
	void resume();

	void queueProcessData(Collection<IProcessData> buffer);

	void queueProcessData(IProcessData processData);

	void discardOldestUnit();

	boolean isSufficientSpaceAvailable(long size);
	
	void discardOldestUnits(int size);

}
