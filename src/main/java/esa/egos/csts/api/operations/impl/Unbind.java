package esa.egos.csts.api.operations.impl;

import b1.ccsds.csts.association.control.types.UnbindInvocation;
import b1.ccsds.csts.association.control.types.UnbindReturn;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.AbstractConfirmedOperation;
import esa.egos.csts.api.operations.IUnbind;

/**
 * This class represents a UNBIND operation.
 */
public class Unbind extends AbstractConfirmedOperation implements IUnbind {

	private static final OperationType TYPE = OperationType.UNBIND;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The constructor of an UNBIND operation
	 */
	public Unbind() {
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public String print(int i) {
		return "Unbind [invocationExtension=" + invocationExtension + "]";
	}

	@Override
	public UnbindInvocation encodeUnbindInvocation() {
		UnbindInvocation unbindInvocation = new UnbindInvocation();
		unbindInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		unbindInvocation.setUnbindInvocationExtension(invocationExtension.encode());
		return unbindInvocation;
	}

	@Override
	public void decodeUnbindInvocation(UnbindInvocation unbindInvocation) {
		decodeStandardInvocationHeader(unbindInvocation.getStandardInvocationHeader());
		invocationExtension = Extension.decode(unbindInvocation.getUnbindInvocationExtension());
	}

	@Override
	public UnbindReturn encodeUnbindReturn() {
		return encodeStandardReturnHeader(UnbindReturn.class);
	}

	@Override
	public void decodeUnbindReturn(UnbindReturn unbindReturn) {
		decodeStandardReturnHeader(unbindReturn);
	}

	@Override
	public String toString() {
		return "Unbind [invocationExtension=" + invocationExtension + "]";
	}

}
