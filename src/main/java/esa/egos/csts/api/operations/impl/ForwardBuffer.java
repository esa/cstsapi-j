package esa.egos.csts.api.operations.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.IForwardBuffer;
import esa.egos.csts.api.operations.IProcessData;

public class ForwardBuffer extends AbstractOperation implements IForwardBuffer {

	private static final OperationType TYPE = OperationType.FORWARD_BUFFER;
	
	private BlockingQueue<IProcessData> forwardBuffer;
	
	public ForwardBuffer() {
		forwardBuffer = new LinkedBlockingQueue<>();
	}
	
	@Override
	public BlockingQueue<IProcessData> getBuffer() {
		return forwardBuffer;
	}
	
	@Override
	public OperationType getType() {
		return TYPE;
	}
	
	@Override
	public String print(int i) {
		return "ForwardBuffer [forwardBuffer=" + forwardBuffer + "]";
	}
	
}
