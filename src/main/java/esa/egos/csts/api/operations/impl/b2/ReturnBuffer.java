package esa.egos.csts.api.operations.impl.b2;

import java.util.ArrayList;
import java.util.List;

import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IReturnBuffer;

/**
 * This class is a dummy operation representing the return buffer type used in
 * the Buffered Data Delivery.
 */
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
		return toString();
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(System.lineSeparator() + "ReturnBuffer of size " + returnBuffer.size());
		if(this.returnBuffer != null) {
			for(int idx=0; idx<this.returnBuffer.size(); idx++) {
				s.append(System.lineSeparator() + "Return Buffer Operation " + (idx+1) + "/" + returnBuffer.size());
				s.append(returnBuffer.get(idx).print(1024));
			}
		}
		
		return s.toString();
	}

}
