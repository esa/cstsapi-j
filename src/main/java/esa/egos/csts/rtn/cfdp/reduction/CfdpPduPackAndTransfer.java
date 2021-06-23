package esa.egos.csts.rtn.cfdp.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Wrapper to RtnCfdpPduSiProvider performing the reduction and the packing of PDUs before transferring them
 * @author mrenesto
 *
 */
public abstract class CfdpPduPackAndTransfer {
	
	private static final Logger LOG = Logger.getLogger(CfdpPduPackAndTransfer.class.getName());
	
	private class CfdpPduPackAndTransferConsumer implements Runnable {
		
		private final AtomicBoolean requestStop = new AtomicBoolean(false);
		
		private final AtomicBoolean statusTransferring = new AtomicBoolean(false);
		
		private final ArrayList<CfdpTransferData> currentBatch = new ArrayList<>();
		
		private final ArrayList<Future<byte[]>> currentReducedBatch = new ArrayList<>();
		
		private final ExecutorService executorService = Executors.newFixedThreadPool(8);
		
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
						
						
						final byte[] fullPduData = cfdpTransferData.getData();
						
						Callable<byte[]> callable = new Callable<byte[]>() {

							@Override
							public byte[] call() throws Exception {
								return reducer.createReducedPdu(fullPduData, computeChecksum);
							}
						};
						
						Future<byte[]> future = executorService.submit(callable);
						
						currentBatch.add(cfdpTransferData);
						currentReducedBatch.add(future);

					}					
				}
				while(cfdpTransferData!=null && currentBatch.size() < packingSize);	
				
				if(currentBatch.isEmpty() == false)
				{
					packAndTransfer(currentBatch,currentReducedBatch);
				}
				
				currentBatch.clear();
				currentReducedBatch.clear();
			}
			
			statusTransferring.set(false);
		}
	}
	
	private final int packingSize;
	
	private final int waitingTime;
	
	private final boolean computeChecksum;
	
	private ArrayBlockingQueue<CfdpTransferData> transferQueue = new ArrayBlockingQueue<>(1000);
	
	private CfdpPduPackAndTransferConsumer consumer = new CfdpPduPackAndTransferConsumer();
	
	private CfdpPduReduce reducer = new CfdpPduReduce();

	
	/**
	 * Create the object managing the data reduction and transfer with the default configuration
	 * packing size is 1000
	 * waiting time is 500ms
	 * checksum computation is true
	 * @param rtnCfdpPduSiProvider the service to which reduced and packed data will be sent
	 */
	public CfdpPduPackAndTransfer()
	{
		this(1000,500, true);
	}
	
	/**
	 * Create the object managing the data reduction and transfer
	 * @param rtnCfdpPduSiProvider the service to which reduced and packed data will be sent
	 * @param packingSize the max number of reduced pdus contained in each data unit send out
	 * @param waitingTime (ms) the timeout waiting for an incoming pdu before sending message shorter than max
	 * @param computeChecksum true if the checksum has to be computed (false improve performance)
	 */
	public CfdpPduPackAndTransfer(int packingSize, int waitingTime, boolean computeChecksum)
	{
		this.packingSize = packingSize;
		this.waitingTime = waitingTime;
		this.computeChecksum = computeChecksum;
	}
	
	/**
	 * Request to start the consumer thread transferring the data
	 * Note: this is a non blocking call
	 */
	public void startTransfer()
	{
		if(consumer.isTransferring() == false)
		{
			Thread thread = new Thread(consumer);
			thread.setName("RTN CFDP pack and transfer consumer");
			thread.start();
		}
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
	
	private void packAndTransfer(List<CfdpTransferData> transferBatch,ArrayList<Future<byte[]>> reducedBatch)
	{
		List<byte[]> reducedPdus = reducedBatch.stream().map(future -> futureGetNoThrow(future)).collect(Collectors.toList());
		
		packAndTransfer(transferBatch, reducedPdus);
	}
	
	private byte[] futureGetNoThrow(Future<byte[]> future ) {
		
		try {
			return future.get();
		} 
		catch(Exception ee)
		{
			return null;
		}
	}
	
	protected abstract void packAndTransfer(List<CfdpTransferData> transferBatch,List<byte[]> reducedPdus );
	
}
