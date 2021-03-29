package esa.egos.csts.rtn.cfdp.reduction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiProvider;

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
						cfdpTransferData = transferQueue.poll(1, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						//We do not care, the value would be null and we proceed
					}
					
					if(cfdpTransferData!=null) {
						currentBatch.add(cfdpTransferData);
					}					
				}
				while(cfdpTransferData!=null && currentBatch.size() < 1000);	
				
				packAndTransfer(currentBatch);
				
				currentBatch.clear();
			}
			
			statusTransferring.set(false);
		}
	}
	
	private final RtnCfdpPduSiProvider rtnCfdpPduSiProvider;
	
	private ArrayBlockingQueue<CfdpTransferData> transferQueue = new ArrayBlockingQueue<>(1000);
	
	private CfdpPduPackAndTransferConsumer consumer = new CfdpPduPackAndTransferConsumer();
	
	private CfdpPduReduce reducer = new CfdpPduReduce();

	
	public CfdpPduPackAndTransfer(RtnCfdpPduSiProvider rtnCfdpPduSiProvider)
	{
		this.rtnCfdpPduSiProvider = rtnCfdpPduSiProvider;
		
		
	}
	
	public void startTransfer()
	{
		Thread thread = new Thread(consumer);
		thread.start();
	}
	
	
	public void stopTransfer()
	{
		consumer.requestStop();
	}
	
	public boolean isTransferring()
	{
		return consumer.isTransferring();
	}
	
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
		List<byte[]> reducedPdus = transferBatch.parallelStream().map( 
				transferData -> reducer.createReducedPdu(transferData.getData(), true) )
				.collect(Collectors.toList());
		
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


}
