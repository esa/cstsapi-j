package esa.egos.csts.api.operations.impl;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IReturnBuffer;

public class ReturnBuffer extends AbstractOperation implements IReturnBuffer {

	private static final OperationType TYPE = OperationType.RETURN_BUFFER;

	private List<IOperation> returnBuffer;

	public ReturnBuffer() {
		returnBuffer = new ArrayList<>();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public List<IOperation> getBuffer() {
		return returnBuffer;
	}

	@Override
	public String print(int i) {
		return "ReturnBuffer [returnBuffer=" + returnBuffer + "]";
	}

}
