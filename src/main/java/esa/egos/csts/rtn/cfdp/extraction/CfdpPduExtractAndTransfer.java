package esa.egos.csts.rtn.cfdp.extraction;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame;
import esa.egos.csts.rtn.cfdp.ccsds.frames.FrameConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.PacketConfigData;
import esa.egos.csts.rtn.cfdp.reduction.CfdpPduPackAndTransfer;
import esa.egos.csts.rtn.cfdp.reduction.CfdpTransferData;

/**
 * Wrapper to RtnCfdpPduSiProvider performing the reduction and the packing of PDUs before transferring them
 * @author mrenesto
 *
 */
public class CfdpPduExtractAndTransfer {
	
	private static final Logger LOG = Logger.getLogger(CfdpPduExtractAndTransfer.class.getName());
	
	private class CfdpPduExtractConsumer implements Runnable {
		
		private final AtomicBoolean requestStop = new AtomicBoolean(false);
		
		private final AtomicBoolean statusTransferring = new AtomicBoolean(false);
		
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

			packAndTransfer.startTransfer();

			//Continue consume element till when requested to terminate AND all element consumed
			while(requestStop.get()==false || extractionQueue.isEmpty()==false ) {

				try {
					Future<CcsdsTmFrame>  futureTransferData = 
							extractionQueue.poll(10, TimeUnit.SECONDS);

					if(futureTransferData!=null) {
						
						extractPackAndTransfer(futureTransferData.get());
					}

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					//We do not care, the value would be null and we proceed
				} catch (ExecutionException e) {
					//this could actually be a data loss
					e.printStackTrace();
				}
			}

			packAndTransfer.stopTransfer();
			statusTransferring.set(false);
		}
	}
	
	private final CfdpPduExtract extractor;
	
	private final CfdpPduPackAndTransfer packAndTransfer;
	
	private ArrayBlockingQueue<Future<CcsdsTmFrame>> extractionQueue = new ArrayBlockingQueue<>(1000);
	
	private CfdpPduExtractConsumer consumer = new CfdpPduExtractConsumer();
	
	private final ExecutorService executorService = Executors.newFixedThreadPool(8);
	

	public CfdpPduExtractAndTransfer(CfdpPduPackAndTransfer packAndTransfer, FrameConfigData frameConfigData,PacketConfigData packetConfigData)
	{
		this.packAndTransfer = packAndTransfer;
		extractor = new CfdpPduExtract(frameConfigData,packetConfigData);
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
	 * 
	 * @param cfdpExtractData
	 */
	public void transferData(final byte[] frame)
	{
		try 
		{
			Callable<CcsdsTmFrame> cfdpTransferData = new Callable<CcsdsTmFrame>() {

				@Override
				public  CcsdsTmFrame call() throws Exception {
					return extractor.reconstructTmFrames(frame);
				}};
	
			Future<CcsdsTmFrame>  futureTransferData = 
					executorService.submit(cfdpTransferData);
			
			extractionQueue.put(futureTransferData);
		} 
		catch (InterruptedException e) 
		{
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
	
	private void extractPackAndTransfer(CcsdsTmFrame frame )
	{
		List<CfdpTransferData> transferData = 
				extractor.extractTransferData(frame);
		for(CfdpTransferData dataUnit: transferData)
		{
			packAndTransfer.transferData(dataUnit);
		}
		
	}
	
}
