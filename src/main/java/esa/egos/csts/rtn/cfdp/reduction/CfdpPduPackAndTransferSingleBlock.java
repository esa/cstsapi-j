package esa.egos.csts.rtn.cfdp.reduction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.CstsResult;

public class CfdpPduPackAndTransferSingleBlock extends CfdpPduPackAndTransfer {
	
	private static final Logger LOG = Logger.getLogger(CfdpPduPackAndTransferSingleBlock.class.getName());
	
	private final CfdpTransferOperationSingleBlock rtnCfdpPduSiProvider;
	
	public CfdpPduPackAndTransferSingleBlock(CfdpTransferOperationSingleBlock rtnCfdpPduSiProvider) {
		super();
		this.rtnCfdpPduSiProvider = rtnCfdpPduSiProvider;
	}

	public CfdpPduPackAndTransferSingleBlock(CfdpTransferOperationSingleBlock rtnCfdpPduSiProvider, int packingSize, int waitingTime, boolean computeChecksum) {
		super(packingSize,waitingTime,computeChecksum);
		this.rtnCfdpPduSiProvider = rtnCfdpPduSiProvider;
	}
	
	@Override
	protected void packAndTransfer(List<CfdpTransferData> transferBatch,List<byte[]> reducedPdus )
	{
		int totalTransferSize = reducedPdus.stream().mapToInt( reducedPdu -> reducedPdu.length).sum();
		
		byte[] completeTransferMessage = new byte[totalTransferSize];
		
		int last = 0;
		
		for(byte[] reducedPdu: reducedPdus)
		{
			System.arraycopy(reducedPdu,0,completeTransferMessage,last, reducedPdu.length);
			last = last + reducedPdu.length;
		}
		
		if(LOG.isLoggable(Level.FINE))
		{
			LOG.fine("TransferData for " + reducedPdus.size() + " units " 
					+ " for a total of " + completeTransferMessage.length + " bytes");
		}
		
		CstsResult cstsResult = rtnCfdpPduSiProvider.transferData(completeTransferMessage);
		transferBatch.stream().forEach(transferData -> transferData.setCstsResult(cstsResult));

	}

}
