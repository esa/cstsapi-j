package esa.egos.csts.test.rtn.cfdp.pdu.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.diagnostics.PeerAbortDiagnostics;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.app.si.rtn.cfdp.pdu.ICfpdPduReceiver;
import esa.egos.csts.app.si.rtn.cfdp.pdu.IRtnCfdpPduProduction;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduDeliveryProcedureConfig;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiProvider;
import esa.egos.csts.app.si.rtn.cfdp.pdu.RtnCfdpPduSiUser;
import esa.egos.csts.rtn.cfdp.ccsds.frames.CcsdsTmFrame;
import esa.egos.csts.rtn.cfdp.ccsds.frames.FrameConfigData;
import esa.egos.csts.rtn.cfdp.ccsds.frames.PacketConfigData;
import esa.egos.csts.rtn.cfdp.extraction.CfdpPduExtractAndTransfer;
import esa.egos.csts.rtn.cfdp.reduction.CfdpPduPackAndTransfer;
import esa.egos.csts.rtn.cfdp.reduction.CfdpPduPackAndTransferSingleBlock;
import esa.egos.csts.rtn.cfdp.reduction.CfdpTransferOperationSingleBlock;


public class TestStressRtnCfdpWithFramesProcessing {
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;
    private ICstsApi userApi;
    
    Lock testConditionLock = new ReentrantLock();
    Condition testCondition = testConditionLock.newCondition();

	private long numDataTransferReceived;
	
	private long numBytesReceived;
	
	
	private static class StopWatcher {
		public Instant startTransmit;
		public Instant stopTransmit;
		public Instant startReceive;
		public Instant stopReceive;
	}
	
	private StopWatcher swatcher;

    @BeforeClass
    public static void setUpClass() throws ApiException
    {
        TestBootstrap.initCs();
    }

	/**
	 * Initializes the provider API and the use API and starts the communication server
	 * @throws ApiException
	 */
	@Before
	public void init() throws ApiException {
		File file = new File("src/test/resources/ProviderConfig2.xml");
		String providerConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/UserConfig2.xml");
		String userConfigName = file.getAbsolutePath();

		file = new File("src/test/resources/log.properties");
		System.setProperty("java.util.logging.config.file", file.getAbsolutePath());
		
		System.out.println("provider config: "  + providerConfigName);
		System.out.println("user config: "  + userConfigName);
		
		providerApi = new CstsApi("Test Service Provider API", AppRole.PROVIDER);
		providerApi.initialize(providerConfigName);
		providerApi.start();

		userApi = new CstsApi("Test Service User API", AppRole.USER);
		userApi.initialize(userConfigName);
		userApi.start();
		
		System.out.println("CSTS user and provider API started");
		
		 swatcher = new StopWatcher();
	}

	/**
	 * Stops the provider and user API
	 */
	@After
	public void finalize() {
		providerApi.stop();
		userApi.stop();
		System.out.println("CSTS user and provider API stopped");		
	}

	//-agentlib:hprof=cpu=times -XX:+UseG1GC
	//-XX:+UseG1GC -DlistRcvDataCapacity=25000
	
	@Test
	@Ignore
	public void testOneFrameMaxSize() {
		implementRtnCfdpPduDataProcess(1, 16384);
	}
	
	@Test
	@Ignore
	public void testTwoPdusMaxSize() {
		implementRtnCfdpPduDataProcess(2, 16384);
	}
	
	@Test
	@Ignore
	public void test1kPdusMaxSize() {
		implementRtnCfdpPduDataProcess(1000, 16384);
	}
	
	@Test
	@Ignore
	public void test2kPdusMaxSize() {
		implementRtnCfdpPduDataProcess(2000, 16384);
	}
	
	@Test
	@Ignore
	public void test10kPdusMaxSize() {
		implementRtnCfdpPduDataProcess(10,1000, 16384);
	}
	
	@Test
	@Ignore
	public void test20kPdusMaxSize() {
		implementRtnCfdpPduDataProcess(20,1000, 16384);
	}
	
	@Test
	@Ignore
	public void test200kPdusMaxSize() {
		implementRtnCfdpPduDataProcess(200,1000, 16384);
	}
	
	@Test
	public void test1000kPdusMaxSize() {
		implementRtnCfdpPduDataProcess(1000,1000, 16384);
	}
	
	public void implementRtnCfdpPduDataProcess(int nPdus, int fullPduSize)
	{
		implementRtnCfdpPduDataProcess(1,nPdus,fullPduSize); 
	}
	
	public void implementRtnCfdpPduDataProcess(int repetitions, int nPdus, int fullPduSize) {
		
		
			RtnCfdpPduDeliveryProcedureConfig rtnCfdpProcedureConfig = new RtnCfdpPduDeliveryProcedureConfig();
			rtnCfdpProcedureConfig.setLatencyLimit(1);
			rtnCfdpProcedureConfig.setReturnBufferSize(5);
		
			System.out.println("Test return Cfdp Pdu Service Data Transfer");			
			
			try {
				
				FrameGenerator frameGenerator = new FrameGenerator(FrameGenerator.generateTestFrameConfigData(),
						FrameGenerator.generatePacketconfigData());
				List<CcsdsTmFrame> frames = frameGenerator.prepareDataFrames(nPdus, fullPduSize);
				

				
				numDataTransferReceived = 0;
				numBytesReceived = 0;
			
				doDataTransfer(data -> {
					testConditionLock.lock();
					numDataTransferReceived++;
					numBytesReceived = numBytesReceived + data.length;
					if(numBytesReceived == ((nPdus*18 + 64)*repetitions))
					{
						swatcher.stopReceive = Instant.now();
					}
					testConditionLock.unlock();
					return CstsResult.SUCCESS;
				},frames, repetitions, false);	

				testConditionLock.lock();

				while(numBytesReceived < ((nPdus*18 + 64)*repetitions)) {
					testCondition.await(10, TimeUnit.SECONDS);
				}
				testConditionLock.unlock();
				
				if(Objects.isNull(swatcher.startTransmit)) {
					CSTS_LOG.CSTS_API_LOGGER.info("Start is null");
				}
				
				if(Objects.isNull( swatcher.stopReceive)) {
					CSTS_LOG.CSTS_API_LOGGER.info("Stop is null");
				}
				
				Duration interval = Duration.between(swatcher.startTransmit, swatcher.stopReceive);
				
				double timeSpan = interval.getSeconds() + interval.getNano()/(1000.0*1000*1000);
				
				//bits /s
				long totalDataSize = ((long)repetitions)*nPdus*fullPduSize;//Byte
				double rate = (totalDataSize*8.0)/(timeSpan); 
				double rateReduced = (repetitions*nPdus*18*8.0)/(timeSpan); 
				
				CSTS_LOG.CSTS_API_LOGGER.info("Transmission Time: " + timeSpan);	
				CSTS_LOG.CSTS_API_LOGGER.info("Total full PDUs size: " + totalDataSize/(1000*1000) + " MByte, rate: "+ rate/1_000_000  +  " Mbit/s");
				CSTS_LOG.CSTS_API_LOGGER.info("Total reduced PDUs size: " + repetitions*nPdus*18/(1000*1000) + " MByte, rate: "+ rateReduced/1000_000  +  " Mbit/s");
				CSTS_LOG.CSTS_API_LOGGER.info("Full PDUs: " + repetitions*nPdus + " rate: " + repetitions*nPdus/timeSpan + " Units/s");
				
				String recordPerformance = System.getProperty("recordPerformance");
				
				if(Objects.nonNull(recordPerformance)) {
					FileWriter fw = new FileWriter("FrameProcessingPerformanceLog.txt",true);
					BufferedWriter bw = new BufferedWriter(fw);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					bw.write(timestamp + ", " + repetitions*nPdus +
							", " + fullPduSize +
							", " + timeSpan +
							", " + rate/1000_000_000 +
							", " + repetitions*nPdus/timeSpan);
					bw.newLine();
					bw.close();
				}

							
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	private void doDataTransfer(CfdpTransferOperationSingleBlock transferOp,List<CcsdsTmFrame> frames, int repetitions, boolean withTime) throws InterruptedException {
				
		
		CfdpPduPackAndTransfer packAndTransfer = new CfdpPduPackAndTransferSingleBlock(transferOp,1000,50,true);
		PacketConfigData packetConfigData = FrameGenerator.generatePacketconfigData();
		FrameConfigData frameConfigData = FrameGenerator.generateTestFrameConfigData();
		CfdpPduExtractAndTransfer extractAndTransfer = 
				new CfdpPduExtractAndTransfer(packAndTransfer,frameConfigData,packetConfigData);
		
		extractAndTransfer.startTransfer();
		
		while(packAndTransfer.isTransferring()==false) {
			Thread.currentThread().sleep(100);
		}
		
		swatcher.startTransmit = Instant.now();
		
		for(int r = 0; r < repetitions; r++)
		{
			for(CcsdsTmFrame ccsdsFrame:frames) {
				extractAndTransfer.transferData(ccsdsFrame.GetTransferFrame());
			} 
		}
				
		extractAndTransfer.stopTransfer();
		CSTS_LOG.CSTS_API_LOGGER.info("Waiting for transfer completion...");
		
		while(packAndTransfer.isTransferring()) {
			Thread.currentThread().sleep(100);
		}	
	}
}
