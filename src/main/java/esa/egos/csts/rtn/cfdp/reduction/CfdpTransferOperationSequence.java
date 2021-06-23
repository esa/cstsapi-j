package esa.egos.csts.rtn.cfdp.reduction;

import java.util.List;

import esa.egos.csts.api.enumerations.CstsResult;

public interface CfdpTransferOperationSequence {
	
	public CstsResult transferData(List<byte[]> data) ;

}
