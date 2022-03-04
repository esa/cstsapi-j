package esa.egos.csts.test.cyclicreport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.main.CstsProviderApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.main.Slecsexe;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.IParameter;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.LabelList;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.CSTS_LOG;
import esa.egos.csts.app.si.SiConfig;
import esa.egos.csts.app.si.md.CyclicReportProcedureConfig;
import esa.egos.csts.app.si.md.IMonitoringDataSiProvider;
import esa.egos.csts.app.si.md.IMonitoringDataSiUser;
import esa.egos.csts.app.si.md.MonitoringDataSiProvider;
import esa.egos.csts.app.si.md.MonitoringDataSiUser;
import esa.egos.csts.app.si.md.NotificationProcedureConfig;
import esa.egos.csts.sim.impl.frm.FunctionalResourceIntegerParameter;
import frm.csts.functional.resource.types.OidValues;

public class TestNotification {
	
	public static final long TEN_SECONDS = 10000;
	
	public static final long ONE_SECOND = 1000;
	
	public static final long HALF_SECOND = 500;
	
	
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;
    private ICstsApi userApi;
    

    @Before
    public void init() throws ApiException {
    	//General initialization of the application
    	File file = new File("src/test/resources/ProviderConfig1.xml");
    	String providerConfigName = file.getAbsolutePath();

    	file = new File("src/test/resources/UserConfig1.xml");
    	String userConfigName = file.getAbsolutePath();

    	file = new File("src/test/resources/log.properties");
    	System.setProperty("java.util.logging.config.file", file.getAbsolutePath());

    	System.out.println("provider config: " + providerConfigName);
    	System.out.println("user config: " + userConfigName);

    	//setup the framework - never to forget
    	Result res = Slecsexe.comServer(providerConfigName, AppRole.PROVIDER, false);
    	if (res != Result.S_OK)
    	{
    		System.err.println("Failed to initialise the communication server: " + res);
    	}

    	//Create and start user and provider
    	providerApi = new CstsProviderApi("Test Service Provider API",providerConfigName);
    	providerApi.start();

    	userApi = new CstsUserApi("Test Service User API",userConfigName);
    	userApi.start();

    	System.out.println("CSTS user and provider API started");
    }
    
    @After
    public void finalize() {
         providerApi.stop();
        // userApi.stop();
         System.out.println("CSTS user and provider API stopped");
    }
    
    @Test
    public void testNotification() {
    	int serviceVersion = 1;
    	//initialize the configuration for the procedures and the service instances
    	NotificationProcedureConfig procedureConfig = createProcedureConfiguration();
    	
        SiConfig providerConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-USER", "CSTS_PT1");
        
        SiConfig userConfig = new SiConfig(ObjectIdentifier.of(1, 3, 112, 4, 7, 0),
                ObjectIdentifier.of(1, 3, 112, 4, 6, 0), 0, "CSTS-PROVIDER", "CSTS_PT1");
    	
    	try {
  		
    		CountDownLatch eventsCounter = new CountDownLatch(3); // after 3 events, terminate
    		 
    		IMonitoringDataSiProvider providerSi = MonitoringDataSiProvider.builder(providerApi, providerConfig)
    			.addNotificationProcedure(procedureConfig,0)
    			.build();
    
    		IMonitoringDataSiUser userSi = MonitoringDataSiUser.builder(userApi, userConfig, serviceVersion)
    			.addNotificationProcedure(procedureConfig,0)
    			.setParameterListener((pii, event) -> {
    				//setup a simple callback when a report is received
    				if(pii.getRole().equals(ProcedureRole.PRIME)) {
        				System.out.println("Received list of size: " + event.size());
    				}
    				else {
    					System.out.println("Received on change report of size: " + event.size());
    				}

    				})
    			.setEventListener((pii, event) -> {
    				System.out.println("Event fired");
    				eventsCounter.countDown();
    			})
    			.build();
    			
            CSTS_LOG.CSTS_API_LOGGER.info("BIND...");
            verifyResult(userSi.bind(), "BIND");
            
            //Create procedure identifier of the procedure to be used
            ProcedureInstanceIdentifier notificationInstance = 
            		ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.notification),ProcedureRole.PRIME,0);
            
            
            //add the events instead of the parameters
            IEvent event1 = createFunctionalResourceEvent(321);
            IEvent event2 = createFunctionalResourceEvent(456);
            providerSi.addEvents(Arrays.asList(event1,event2));
           
            
            //Note: parameters have to be set before the START or the onChange report will not contain them.
            
            CSTS_LOG.CSTS_API_LOGGER.info("START...");
            verifyResult(userSi.startNotification(notificationInstance),"START");
           
            event1.fire();
            event2.fire();
            event1.fire();
            
            waitOrFail(eventsCounter, TEN_SECONDS); // wait for 3 events
            
            CSTS_LOG.CSTS_API_LOGGER.info("STOP...");
            verifyResult(userSi.stopNotification(notificationInstance),"STOP");
            
            CSTS_LOG.CSTS_API_LOGGER.info("UNBIND...");
            verifyResult(userSi.unbind(), "UNBIND");
            
    	} 
    	catch(Exception ee) {
    		ee.printStackTrace();
    		fail("Failed because of unexpected exception");
    	}
    }
    
    
    public NotificationProcedureConfig createProcedureConfiguration() {
    	
        //ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1000, 1, 3, 1});
    	ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(OidValues.antennaFrOid.value);
        FunctionalResourceType antActualAzimuthType = FunctionalResourceType.of(antActualAzimuthId);

        List<Label> labels = Collections.singletonList(Label.of(antActualAzimuthId, antActualAzimuthType));
    	
    	LabelList testLabels = new LabelList("test-list-1", true);
    	testLabels.getLabels().addAll(labels);
    	return new NotificationProcedureConfig(testLabels, ListOfParameters.empty());
    }
    

    public IEvent createFunctionalResourceEvent(double value) {
        //ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1000, 1, 3, 1});
        ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(OidValues.antennaFrOid.value);
        FunctionalResourceType antActualAzimuthType = FunctionalResourceType.of(antActualAzimuthId);
        FunctionalResourceName antActualAzimuthName = FunctionalResourceName.of(antActualAzimuthType, 1);
        //This is a B1 only approach
        FunctionalResourceIntegerParameter antActualAzimuth =
                new FunctionalResourceIntegerParameter(antActualAzimuthId, antActualAzimuthName);
        antActualAzimuth.setValue(value);
        IEvent event = new Event(antActualAzimuthId,antActualAzimuthName);
        event.setTime(Time.now());
        event.setValue(new EventValue(EventValueType.QUALIFIED_VALUES));
        antActualAzimuth.toQualifiedParameter().getQualifiedValues().stream().forEach(qv -> event.getValue().getQualifiedValues().add(qv));
        return event;
    }
    
    public void setFunctionalResourceValue(IParameter parameter, double value) {
    	if(parameter instanceof FunctionalResourceIntegerParameter) {
    		FunctionalResourceIntegerParameter frip = (FunctionalResourceIntegerParameter)parameter;
    		frip.setValue(value);    		
    	}
    }
    
    private void verifyResult(CstsResult res, String what) {
        Assert.assertEquals(CstsResult.SUCCESS, res);
        if (res != CstsResult.SUCCESS) {
             CSTS_LOG.CSTS_API_LOGGER.severe("Failed to " + what + " " + res);
        } else {
             CSTS_LOG.CSTS_API_LOGGER.info(what + " OK " + res);
        }
   }
    
    private void waitOrFail(CountDownLatch latch, long ms) {
        try {
        	latch.await(ms, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
        	fail("Failed because of unexpected exception");
        } finally {
        	assertEquals(0, latch.getCount());
		}
    }

}
