package esa.egos.csts.api.procedures;

import java.io.IOException;

import esa.egos.csts.api.enums.ProcedureTypeEnum;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.procedures.impl.ProcedureType;

// TODO extend NOTIFY
public abstract class AbstractBufferedDataProcessing extends AbstractDataProcessing implements IBufferedDataProcessing {

	private final ProcedureType type = new ProcedureType(ProcedureTypeEnum.bufferedDataProcessing);

	protected AbstractBufferedDataProcessing() {
		super();
	}

	@Override
	public ProcedureType getType() {
		return type;
	}

	protected void initOperationSet() {
		super.initOperationSet();
	}

	@Override
	protected void initialiseOperationFactory() {
		super.initialiseOperationFactory();
	}

	@Override
	public byte[] encodeOperation(IOperation operation, boolean isInvoke) throws ApiException, IOException {
		return null;
	}

	@Override
	public IOperation decodeOperation(byte[] encodedPdu) throws ApiException, IOException {
		return null;
	}

}
