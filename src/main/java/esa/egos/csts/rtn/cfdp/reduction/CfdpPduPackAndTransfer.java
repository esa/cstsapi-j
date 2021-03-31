package esa.egos.csts.rtn.cfdp.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiProvider;

/**
 * Wrapper to RtnCfdpPduSiProvider performing the reduction and the packing of PDUs before transferring them
 * @author mrenesto
 *
 */
public class CfdpPduPackAndTransfer {
	
	private class CfdpPduPackAndTransferConsumer implements Runnable {
		
		private final AtomicBoolean requestStop = new AtomicBoolean(false);
		
		private final AtomicBoolean statusTransferring = new AtomicBoolean(false);
		
		private final ArrayList<CfdpTransferData> currentBatch = new ArrayList<>();
		
		public void requestStop()
		{
			requestStop.set(true);
		}
		
		public boolean isTransferring()
		{
			return statusTransferring.get();
		}
		
		@Override
		public void run() {
			
			statusTransferring.set(true);
			
			//Continue consume element till when requested to terminate AND all element consumed
			while(requestStop.get()==false || transferQueue.isEmpty()==false ) {
				
				CfdpTransferData cfdpTransferData = null;
				
				do
				{
					try {
						cfdpTransferData = transferQueue.poll(waitingTime, TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						//We do not care, the value would be null and we proceed
					}
					
					if(cfdpTransferData!=null) {
						currentBatch.add(cfdpTransferData);
					}					
				}
				while(cfdpTransferData!=null && currentBatch.size() < packingSize);	
				
				packAndTransfer(currentBatch);
				
				currentBatch.clear();
			}
			
			statusTransferring.set(false);
		}
	}
	
	private final RtnCfdpPduSiProvider rtnCfdpPduSiProvider;
	
	private final int packingSize;
	
	private final int waitingTime;
	
	private final boolean outOfOrder;
	
	private final boolean computeChecksum;
	
	private ArrayBlockingQueue<CfdpTransferData> transferQueue = new ArrayBlockingQueue<>(1000);
	
	private CfdpPduPackAndTransferConsumer consumer = new CfdpPduPackAndTransferConsumer();
	
	private CfdpPduReduce reducer = new CfdpPduReduce();

	
	/**
	 * Create the object managing the data reduction and transfer with the default configuration
	 * packing size is 1000
	 * waiting time is 500ms
	 * out of order transfer is false
	 * checksum computation is true
	 * @param rtnCfdpPduSiProvider the service to which reduced and packed data will be sent
	 */
	public CfdpPduPackAndTransfer(RtnCfdpPduSiProvider rtnCfdpPduSiProvider)
	{
		this(rtnCfdpPduSiProvider,1000,500, false, true);
	}
	
	/**
	 * Create the object managing the data reduction and transfer
	 * @param rtnCfdpPduSiProvider the service to which reduced and packed data will be sent
	 * @param packingSize the max number of reduced pdus contained in each data unit send out
	 * @param waitingTime (ms) the timeout waiting for an incoming pdu before sending message shorter than max
	 * @param outOfOrder true if reduction of PDUs can be done out of order (improve performance)
	 * @param computeChecksum true if the checksum has to be computed (false improve performance)
	 */
	public CfdpPduPackAndTransfer(RtnCfdpPduSiProvider rtnCfdpPduSiProvider, int packingSize, int waitingTime, boolean outOfOrder, boolean computeChecksum)
	{
		this.rtnCfdpPduSiProvider = rtnCfdpPduSiProvider;
		this.packingSize = packingSize;
		this.waitingTime = waitingTime;
		this.outOfOrder = outOfOrder;
		this.computeChecksum = computeChecksum;
	}
	
	/**
	 * Start the consumer thread transferring the data
	 */
	public void startTransfer()
	{
		Thread thread = new Thread(consumer);
		thread.start();
	}
	
	/**
	 * Request the termination of the consumer thread transferring the data
	 * Note: termination occurs when the transfer queue is emptied
	 */
	public void stopTransfer()
	{
		consumer.requestStop();
	}
	
	/**
	 * Check the status of the consumer
	 * @return true if the consumer is transferring data or waiting for incoming data to transfer
	 * false if is not started or it is stopped
	 */
	public boolean isTransferring()
	{
		return consumer.isTransferring();
	}
	
	/**
	 * Request to transfer data - this operation is asynchronous status flag on the data units
	 * will be updated when transfer operation has be completed 
	 * @param cfdpTransferData the data to be transferred paired with its status
	 */
	public void transferData(CfdpTransferData cfdpTransferData)
	{
		try 
		{
			transferQueue.put(cfdpTransferData);
		} 
		catch (InterruptedException e) 
		{
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
	
	private void packAndTransfer(List<CfdpTransferData> transferBatch)
	{
		List<byte[]> reducedPdus = reducedPdus(transferBatch);
		
		int totalTransferSize = reducedPdus.stream().mapToInt( reducedPdu -> reducedPdu.length).sum();
		
		byte[] completeTransferMessage = new byte[totalTransferSize];
		
		int last = 0;
		
		for(byte[] reducedPdu: reducedPdus)
		{
			System.arraycopy(reducedPdu,0,completeTransferMessage,last, reducedPdu.length);
			last = last + reducedPdu.length;
		}
		
		CstsResult cstsResult = rtnCfdpPduSiProvider.transferData(completeTransferMessage);
		transferBatch.stream().forEach(transferData -> transferData.setCstsResult(cstsResult));
	}
	
	private List<byte[]> reducedPdus(List<CfdpTransferData> transferBatch)
	{
		if(outOfOrder)
		{
			return transferBatch.parallelStream().map( 
					transferData -> reducer.createReducedPdu(transferData.getData(), computeChecksum) )
					.collect(Collectors.toList());
		}
		else
		{
			return transferBatch.stream().map( 
					transferData -> reducer.createReducedPdu(transferData.getData(), computeChecksum) )
					.collect(Collectors.toList());
		}
	}


}
