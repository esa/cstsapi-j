package esa.egos.csts.api.operations;

import java.util.concurrent.BlockingQueue;

public interface IForwardBuffer extends IOperation {

	BlockingQueue<IProcessData> getBuffer();
	
}
