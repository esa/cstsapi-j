package esa.egos.csts.rtn.cfdp.reduction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import esa.egos.csts.api.enumerations.CstsResult;

public class CfdpPduPackAndTransferSequence extends CfdpPduPackAndTransfer {
	
	private static final Logger LOG = Logger.getLogger(CfdpPduPackAndTransferSequence.class.getName());

	private final CfdpTransferOperationSequence rtnCfdpPduSiProvider;
	
	public CfdpPduPackAndTransferSequence(CfdpTransferOperationSequence rtnCfdpPduSiProvider) {
		super();
		this.rtnCfdpPduSiProvider = rtnCfdpPduSiProvider;
	}

	public CfdpPduPackAndTransferSequence(CfdpTransferOperationSequence rtnCfdpPduSiProvider, int packingSize, int waitingTime, boolean computeChecksum) {
		super(packingSize,waitingTime,computeChecksum);
		this.rtnCfdpPduSiProvider = rtnCfdpPduSiProvider;
	}

	@Override
	protected void packAndTransfer(List<CfdpTransferData> transferBatch, List<byte[]> reducedPdus) {
		if(LOG.isLoggable(Level.FINE))
		{
			int totalTransferSize = reducedPdus.stream().mapToInt( reducedPdu -> reducedPdu.length).sum();
			LOG.fine("TransferData for " + reducedPdus.size() + " units " 
					+ " for a total of " +totalTransferSize + " bytes plus format data");
		}
		CstsResult cstsResult = rtnCfdpPduSiProvider.transferData(reducedPdus);
		transferBatch.stream().forEach(transferData -> transferData.setCstsResult(cstsResult));
	}
}
