package esa.egos.csts.test.bdd;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsProviderApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.app.si.SiConfig;

public class TestBdd {
    
	private ICstsApi providerApi;
    private ICstsApi userApi;
	private SiConfig siProviderConfig;
	private SiConfig siUserConfig;
	private final int TEST_BDD_SRV_VERSION = 1;

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
		initProviderApi();
		initUserApi();
		
		siProviderConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
				ObjectIdentifier.of(1,3,112,4,6,0),
				1, 
				"CSTS-USER", 
				"CSTS_PT1");
				
		siUserConfig = new SiConfig(ObjectIdentifier.of(1,3,112,4,7,0),
				ObjectIdentifier.of(1,3,112,4,6,0),
				1, 
				"CSTS-PROVIDER", // TODO: test correct behavior (return code) for wrong peer identifier
				"CSTS_PT1");
		
	}
	
	/**
	 * Initialize an API instance for use with the provider SIs
	 * @throws ApiException
	 */
	private void initUserApi() throws ApiException {
		File file = new File("src/test/resources/UserConfig1.xml");
		String userConfigName = file.getAbsolutePath();		
		
		System.out.println("user config: "  + userConfigName);
		
		userApi = new CstsUserApi("Test Service User API");
		userApi.initialize(userConfigName);
		userApi.start();
		
		System.out.println("CSTS user API started");
	}
	
	/**
	 * Initialize an API instance for use with the user SIs
	 * @throws ApiException
	 */
	private void initProviderApi() throws ApiException {
		File file = new File("src/test/resources/ProviderConfig1.xml");
		String providerConfigName = file.getAbsolutePath();
		
		file = new File("src/test/resources/log.properties");
		System.setProperty("java.util.logging.config.file", file.getAbsolutePath());	
		
		System.out.println("provider config: "  + providerConfigName);
		
		providerApi = new CstsProviderApi("Test Service Provider API");
		providerApi.initialize(providerConfigName);
		providerApi.start();		
		System.out.println("CSTS provider API started");
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
	
	
	/**
	 * Test nominal transfer with BDD
	 * @throws ApiException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testNominalTransfer() throws ApiException, InterruptedException {
		final long numDataItems = 500000;
		final int dataSize = 1115;
		final AtomicLong numDataRecvd = new AtomicLong(0);
		final Lock dataItemsCountLock = new ReentrantLock();
		final Condition dataItemsCountCond = dataItemsCountLock.newCondition();
		
		BddProviderSi providerSi = new BddProviderSi(providerApi, siProviderConfig, true, TEST_BDD_SRV_VERSION);

		BddUserSi userSi = new BddUserSi(userApi, siUserConfig, TEST_BDD_SRV_VERSION, new DataReceiver() {
			
			@Override
			public void dataReceived(byte[] data) {				
				dataItemsCountLock.lock();
				if(numDataRecvd.incrementAndGet() == numDataItems) {
					dataItemsCountCond.signal();
				}
				dataItemsCountLock.unlock();
			}

			@Override
			public void notifyReceived(INotify notify) {
				System.out.println("BDD test: Received notification: "  + notify);				
			}
		});
		
		CstsResult res = userSi.bind();
		assert(res == CstsResult.SUCCESS);
		
		res = userSi.start();
		assert(res == CstsResult.SUCCESS);
		
		long startTime = System.currentTimeMillis();
		
		byte[] data = new byte[dataSize];
		for(long idx=0; idx<numDataItems; idx++) {
			res = providerSi.sendData(data);
			assert(res == CstsResult.SUCCESS);
		}
		
		dataItemsCountLock.lock();
		if(numDataRecvd.get() != numDataItems) {
			dataItemsCountCond.await(120, TimeUnit.SECONDS);
		}
		dataItemsCountLock.unlock();
		
		long stopTime = System.currentTimeMillis();
		
		assert(numDataRecvd.get() == numDataItems);
		
		
		res = userSi.stop();
		assert(res == CstsResult.SUCCESS);
		
		res = userSi.unbind();
		assert(res == CstsResult.SUCCESS);
		
		float durationUSec = (stopTime - startTime) * 1000; 
		float rate = (numDataItems*dataSize*8) / durationUSec;
		System.out.println("** BDD test: Received " + numDataItems + " transfer data operations within " + (stopTime - startTime) + " ms, rate: " + rate + " Mbit/s **");
	}

	/**
	 * Test nominal transfer with BDD
	 * @throws ApiException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testBackpressureTransfer() throws ApiException, InterruptedException {
		final long numDataItems = 500000;
		final int dataSize = 1115;
		final long waitMs = 0;
		final int waitNanos = 60000;		
		final long numWait = 600000;
		final AtomicLong numActualWait = new AtomicLong(0);
		final AtomicLong numDataRecvd = new AtomicLong(0);
		final Lock dataItemsCountLock = new ReentrantLock();
		final Condition dataItemsCountCond = dataItemsCountLock.newCondition();
		//final int sendDelay = 10;
		
		BddProviderSi providerSi = new BddProviderSi(providerApi, siProviderConfig, true, TEST_BDD_SRV_VERSION);

		BddUserSi userSi = new BddUserSi(userApi, siUserConfig, TEST_BDD_SRV_VERSION, new DataReceiver() {
			
			@Override
			public void dataReceived(byte[] data) {
				if(numDataRecvd.get() % 100 == 0) {
					if(numActualWait.incrementAndGet() < numWait) {
						try {
							synchronized (this) {
								if(waitMs > 0 || waitNanos > 0) {
									//System.out.println("Wait " + waitMs + " ms " + waitNanos + "  nano seconds");
									this.wait(waitMs, waitNanos);
								}
							}
							
						} catch (InterruptedException e) {
							// OK
						}
					} else if(numActualWait.get() == numWait) {
						System.out.println(numWait + " of " + waitMs + "ms / " + waitNanos + " ns waits completed");
					}					
				}
				
				dataItemsCountLock.lock();
				if(numDataRecvd.incrementAndGet() == numDataItems) {
					dataItemsCountCond.signal();
				}
				dataItemsCountLock.unlock();
			}

			@Override
			public void notifyReceived(INotify notify) {
				System.out.println("BDD test: Received notification: "  + notify);				
			}
		});
		
		CstsResult res = userSi.bind();
		assert(res == CstsResult.SUCCESS);
		
		res = userSi.start();
		assert(res == CstsResult.SUCCESS);
		
		long startTime = System.currentTimeMillis();
		
		byte[] data = new byte[dataSize];
		for(long idx=0; idx<numDataItems; idx++) {
//			long startSend = System.currentTimeMillis();
			res = providerSi.sendData(data);
//			if(idx % 100000 == 0) {
//				synchronized (this) {
//					wait(1000);
//				}
//			}
//			long endSend = System.currentTimeMillis();
//			if(endSend - startSend > sendDelay) {
//				System.out.println("Send delay above threshold: " + (endSend-startSend));
//			}
			assert(res == CstsResult.SUCCESS);
		}
		
		boolean timeout = false;
		dataItemsCountLock.lock();
		if(numDataRecvd.get() != numDataItems) {
			timeout = dataItemsCountCond.await(720, TimeUnit.SECONDS);
		}
		dataItemsCountLock.unlock();
		
		long stopTime = System.currentTimeMillis();
		
		if(numDataRecvd.get() != numDataItems) {
			System.err.println("Data recevied " + numDataRecvd.get() + " does not match expected value: " + numDataItems + " timeout: " + timeout);
		}
		assert(numDataRecvd.get() == numDataItems);
		
		
		res = userSi.stop();
		assert(res == CstsResult.SUCCESS);
		
		res = userSi.unbind();
		assert(res == CstsResult.SUCCESS);
		
		float durationUSec = (stopTime - startTime) * 1000; 
		float rate = (numDataItems*dataSize*8) / durationUSec;
		System.out.println("** BDD backpressure test: Received " + numDataItems + " transfer data operations within "
		+ (stopTime - startTime) + " ms, rate: " + rate + " Mbit/s. Delay: " + waitMs + " ms " + waitNanos + " nano seconds **");
	}	
	
}
