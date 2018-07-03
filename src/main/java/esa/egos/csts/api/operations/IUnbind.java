package esa.egos.csts.api.operations;

import ccsds.csts.association.control.types.UnbindInvocation;
import ccsds.csts.association.control.types.UnbindReturn;
import esa.egos.proxy.enums.UnbindReason;

public interface IUnbind extends IConfirmedOperation{

    /**
     * Returns the UNBIND reason
     * 
     * @return the UNBIND reason
     */
    UnbindReason getUnbindReason();

    /**
     * Sets the UNBIND reason
     * 
     * @param reason the UNBIND reason
     */
    void setUnbindReason(UnbindReason reason);

    UnbindReturn encodeUnbindReturn();
    
	void decodeUnbindReturn(UnbindReturn unbindReturn);

	void decodeUnbindInvocation(UnbindInvocation unbindInvocation);

	UnbindInvocation encodeUnbindInvocation();
	
}
