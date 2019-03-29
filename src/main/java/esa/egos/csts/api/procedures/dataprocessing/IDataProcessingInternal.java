package esa.egos.csts.api.procedures.dataprocessing;

import java.util.Collection;

import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.parameters.impl.IntegerConfigurationParameter;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public interface IDataProcessingInternal extends IDataProcessing, IStatefulProcedureInternal {

	boolean isDataUnitReady();

	boolean isDataQueued();

	IntegerConfigurationParameter getInputQueueSize();

	void setProduceReport(boolean produceReport);

	boolean isProduceReport();

	void setProcessing(boolean processing);

	boolean isProcessing();

	void setInterruptNotified(boolean interruptNotified);

	boolean isInterruptNotified();

	boolean isQueueFull();

	int getQueueSize();

	void removeFromQueue(IProcessData processData);

	void queueProcessData(Collection<IProcessData> queue);

	void queueProcessData(IProcessData operation);

}
