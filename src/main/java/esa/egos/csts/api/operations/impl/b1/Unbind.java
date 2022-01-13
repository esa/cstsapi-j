package esa.egos.csts.api.operations.impl.b1;

import com.beanit.jasn1.ber.types.BerType;

import b1.ccsds.csts.association.control.types.UnbindInvocation;
import b1.ccsds.csts.association.control.types.UnbindReturn;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.extensions.Extension;
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

	/**
	 * Return a String w/ CSTS Unbind operation parameters
	 * @param i capacity
	 * @return String w/ CSTS Unbind parameters
	 */
	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		sb.append("\nOperation                      : UNBIND\n");
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : true\n");
		sb.append("Diagnostic Type                : no diagnostic\n");
		sb.append("Common Diagnostics             : Invalid\n");
		sb.append("Framework                      : B1\n");

		return sb.toString();
	}

	@Override
	public BerType encodeUnbindInvocation() {
		UnbindInvocation unbindInvocation = new UnbindInvocation();
		unbindInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		unbindInvocation.setUnbindInvocationExtension(invocationExtension.encode(
				new b1.ccsds.csts.common.types.Extended()));
		return unbindInvocation;
	}

	@Override
	public void decodeUnbindInvocation(BerType unbindInvocation) {
		UnbindInvocation unbindInvocation1 = (UnbindInvocation)unbindInvocation;
		decodeStandardInvocationHeader(unbindInvocation1.getStandardInvocationHeader());
		invocationExtension = Extension.decode(unbindInvocation1.getUnbindInvocationExtension());
	}

	@Override
	public UnbindReturn encodeUnbindReturn() {
		return encodeStandardReturnHeader(UnbindReturn.class);
	}

	@Override
	public void decodeUnbindReturn(BerType unbindReturn) {
		UnbindReturn unbindREturn1 = (UnbindReturn)unbindReturn;
		decodeStandardReturnHeader(unbindREturn1);
	}

	@Override
	public String toString() {
		return "Unbind [invocationExtension=" + invocationExtension + "]";
	}

}
