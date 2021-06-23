package esa.egos.csts.rtn.cfdp.reduction;

import esa.egos.csts.api.enumerations.CstsResult;

public interface CfdpTransferOperationSingleBlock {
	
	public CstsResult transferData(byte[] data) ;

}
