package esa.egos.csts.rtn.cfdp.reduction;

import esa.egos.csts.api.enumerations.CstsResult;

public class CfdpTransferData {

	private final byte[] data;
	
	private CstsResult cstsResult;
	
	public CfdpTransferData(byte[] data) 
	{
		this.data = data;
		this.cstsResult = CstsResult.NOT_APPLICABLE;
	}
	
	
	public byte[] getData()
	{
		return data;
	}
	
	public CstsResult getCstsResult()
	{
		return cstsResult;
	}
	
	public void setCstsResult(CstsResult cstsResult)
	{
		this.cstsResult = cstsResult;
	}
	

}
