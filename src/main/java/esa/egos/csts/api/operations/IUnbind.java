package esa.egos.csts.api.operations;

import ccsds.csts.association.control.types.UnbindInvocation;
import ccsds.csts.association.control.types.UnbindReturn;

public interface IUnbind extends IConfirmedOperation {

	UnbindInvocation encodeUnbindInvocation();

	void decodeUnbindInvocation(UnbindInvocation unbindInvocation);

	UnbindReturn encodeUnbindReturn();

	void decodeUnbindReturn(UnbindReturn unbindReturn);

}
