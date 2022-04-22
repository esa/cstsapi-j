package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.main.CstsProviderApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.main.ICstsApi;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.MdCstsSiConfig;
import esa.egos.csts.sim.impl.prv.MdCollection;
import esa.egos.csts.sim.impl.prv.MdCstsSiProvider;
import esa.egos.csts.sim.impl.prv.MdCstsSiProviderConfig;
import esa.egos.csts.sim.impl.usr.MdCstsSiUser;
import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.TestBootstrap;
import esa.egos.csts.api.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * Test the CSTS API at the example of the monitored data service
 */
public class NotificationTest
{
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;

    private ICstsApi userApi;
    
    private static final int SERVICE_VERSION_SFW_B1 = 1; 
    
    private static final int SERVICE_VERSION_SFW_B2 = 2; 
    

    // S/C identifier
    private ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);

    // G/S identifier
    private ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

    // prime inform query procedure identifier
    private ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.notification),
                                                                              ProcedureRole.PRIME,
                                                                              0);

    // all procedure identifiers
    private List<ProcedureInstanceIdentifier> piids = Collections.singletonList(piid);

    private long minimumAllowedDeliveryCycle = 50;

    // create provider SI configuration
    private MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(this.minimumAllowedDeliveryCycle,
                                                                                   null,
                                                                                   null,
                                                                                   this.scId,
                                                                                   this.facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   this.piids);

    // create provider SI configuration
    private MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(this.scId,
                                                               this.facilityId,
                                                               0,
                                                               "CSTS_PROVIDER",
                                                               "CSTS_PT1",
                                                               this.piids);

    // non existent parameter - parameter is not added into MD collection
    private ObjectIdentifier antIdId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1, 1, 1, 1 });

    private FunctionalResourceType antIdType = FunctionalResourceType.of(this.antIdId);

    private FunctionalResourceName antIdName = FunctionalResourceName.of(this.antIdType, 1);

    private Name nonExistentParameterName = Name.of(this.antIdId, this.antIdName);
    
    
    @BeforeClass
    public static void setUpClass() throws ApiException
    {
        TestBootstrap.initCs();
    }

    /**
     * Initializes the provider API and the use API and starts the communication
     * server
     * 
     * @throws ApiException
     */
    @Before
    public void setUp() throws ApiException
    {
        File file = new File("src/test/resources/ProviderConfig.xml");
        String providerConfigName = file.getAbsolutePath();

        file = new File("src/test/resources/UserConfig.xml");
        String userConfigName = file.getAbsolutePath();

        System.out.println("provider config: " + providerConfigName);
        System.out.println("user config: " + userConfigName);

        this.providerApi = new CstsProviderApi("Test Service Provider API");
        this.providerApi.initialize(providerConfigName);
        this.providerApi.start();

        this.userApi = new CstsUserApi("Test Service User API");
        this.userApi.initialize(userConfigName);
        this.userApi.start();

        System.out.println("CSTS user and provider API started");
    }

    /**
     * Stops the provider and user API
     */
    @After
    public void tearDown()
    {
        this.providerApi.stop();
        this.userApi.stop();
        System.out.println("CSTS user and provider API stopped");
    }
    
    /**
     * Test notification procedure and its GET operation w/ default list => ListOfParameter /w type EMPTY
     * For B1 standard
     */
    @Test
    public void testNotificationWithDefaultLabelListB1()
    {
         testNotificationWithDefaultLabelList(SERVICE_VERSION_SFW_B1);
    }
    
    /**
     * Test notification procedure and its GET operation w/ default list => ListOfParameter /w type EMPTY
     * For B1 standard
     */
    @Test
    public void testNotificationWithDefaultLabelListB2()
    {
         testNotificationWithDefaultLabelList(SERVICE_VERSION_SFW_B2);
    }
    
    /**
     * Test notification procedure and its GET operation w/ default list => ListOfParameter /w type EMPTY
     */
    public void testNotificationWithDefaultLabelList(int sfwVersion)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, sfwVersion);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            // the secondary procedure w/ instance number 1 has not defined the default list
            System.out.println("START-NOTIFICATION ...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(), ListOfParameters.empty()),
                                   "START-NOTIFICATION",
                                   CstsResult.FAILURE);

            mdCollection.fireAllEvents();
            
            // wait for notification (there should be none)
            userSi.waitTransferData(500, 500);

            assertTrue("got some unexpected notifications from provider", userSi.getNotifiedEventCount() == 0);

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Test notification procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     * For B1 standard
     */
    @Test
    public void testNotificationWithFunctionalResourceNameB1()
    {
         testNotificationWithFunctionalResourceName(SERVICE_VERSION_SFW_B1);
    }
    
    /**
     * Test notification procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     * For B2 standard
     */
    @Test
    public void testNotificationWithFunctionalResourceNameB2()
    {
         testNotificationWithFunctionalResourceName(SERVICE_VERSION_SFW_B2);
    }
    
    /**
     * Test notification procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     */
    public void testNotificationWithFunctionalResourceName(int sfwVersion)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, sfwVersion);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");
            
            Name name = mdCollection.getEventNames().getParameterNames().get(0);

            System.out.println("START-NOTIFICATION ...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(), mdCollection.getEventNames()),
                                   "START-NOTIFICATION");

            mdCollection.fireAllEvents();
            // wait for notification (there should be none)
            userSi.waitTransferData(10, 500);

            assertFalse("did not get any notifications from provider", userSi.getNotifiedEventCount() == 0);

            List<Name> events_01 = userSi.getNotifiedEvents();

            // verify that the user SI received all queried events
            Optional<Name> result_01 = events_01.stream().filter(event -> event.equals(name)).findAny();
            assertTrue("missing event " + name, result_01.isPresent());
            assertTrue("event received is not equal to expected", name.equals(result_01.get()));

            System.out.println("STOP-NOTIFICATION...");
            TestUtils.verifyResult(userSi.stopNotification(piid.getInstanceNumber()), "STOP-NOTIFICATION");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Test notification procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_NAME 
     * For B1 standard
     */
    @Test
    public void testNotificationWithNonExistentFunctionalResourceNameB1()
    {
         testNotificationWithNonExistentFunctionalResourceName(SERVICE_VERSION_SFW_B1);
    }
    
    /**
     * Test notification procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_NAME 
     * For B2 standard
     */
    @Test
    public void testNotificationWithNonExistentFunctionalResourceNameB2()
    {
         testNotificationWithNonExistentFunctionalResourceName(SERVICE_VERSION_SFW_B2);
    }
    /**
     * Test notification procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_NAME 
     */
    public void testNotificationWithNonExistentFunctionalResourceName(int sfwVersion)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, sfwVersion);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(),
                                                            ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName())),
                                   "START-NOTIFICATION",
                                   CstsResult.FAILURE);
            
            mdCollection.fireAllEvents();
            // wait for notification (there should be none)
            userSi.waitTransferData(10, 500);

            // verify that diagnostic contains the OID of the unknown parameter
            System.out.println("diag: "+userSi.getDiagnostic());
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("1.3.112.4.4.2.1.1"));

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test notification procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testNotificationWithFunctionalResourceType()
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name name = mdCollection.getEventNames().getParameterNames().get(0);
            FunctionalResourceType functionalResourceType = name.getFunctionalResourceName().getType();

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(), ListOfParameters.of(functionalResourceType)),
                                   "START-NOTIFICATION");

            mdCollection.fireAllEvents();
            // wait for notification (there should be none)
            userSi.waitTransferData(10, 500);

            List<Name> notificationEvents_01 = userSi.getNotifiedEvents();
            assertFalse("did not get any events from provider", notificationEvents_01.isEmpty());

            // verify that the user SI received all expected events
            Optional<Name> result_01 = notificationEvents_01.stream().filter(event -> event.equals(name)).findAny();
            assertTrue("missing event " + name, result_01.isPresent());
            assertTrue("event received is not equal to expected", name.equals(result_01.get()));

            System.out.println("STOP-NOTIFICATION...");
            TestUtils.verifyResult(userSi.stopNotification(piid.getInstanceNumber()), "STOP-NOTIFICATION");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test notification procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_TYPE
     * For B1 standard
     */
    @Test
    public void testNotificationWithNonExistentFunctionalResourceTypeB1()
    {
         testNotificationWithNonExistentFunctionalResourceType(SERVICE_VERSION_SFW_B1);
    }
    
    /**
     * Test notification procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_TYPE
     * For B2 standard
     */
    @Test
    public void testNotificationWithNonExistentFunctionalResourceTypeB2()
    {
         testNotificationWithNonExistentFunctionalResourceType(SERVICE_VERSION_SFW_B2);
    }
    
    /**
     * Test notification procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_TYPE
     */
    public void testNotificationWithNonExistentFunctionalResourceType(int sfwVersion)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, sfwVersion);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(),
                                                            ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName())),
                                   "START-NOTIFICATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("1.3.112.4.4.2.1.1"));

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test notification procedure and its GET operation w/ LABEL_SET"
     */
    @Test
    public void testNotificationWithLabelSet()
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(), mdCollection.getEventLabelSet()),
                                   "START-NOTIFICATION");

            mdCollection.fireAllEvents();
            // wait for notification (there should be none)
            userSi.waitTransferData(10, 500);

            List<Name> eventNotifications_01 = userSi.getNotifiedEvents();
            assertFalse("did not get any parameters from provider", eventNotifications_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Event event: mdCollection.getEvents())
            {
                Optional<Name> result = eventNotifications_01.stream()
                        .filter(eventName -> eventName.getFunctionalResourceName().getType()
                                .equals(event.getLabel().getFunctionalResourceType()))
                        .findAny();
                assertTrue("missing event parameter " + event, result.isPresent());
            }

            System.out.println("STOP-NOTIFICATION...");
            TestUtils.verifyResult(userSi.stopNotification(piid.getInstanceNumber()), "STOP-NOTIFICATION");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test notification procedure and its GET operation w/ LABEL_SET w/ non-existent LABEL"
     * For B1 standard
     */
    @Test
    public void testNotificationWithLabelSetWithNonExistentLabelB1()
    {
         testNotificationWithLabelSetWithNonExistentLabel(SERVICE_VERSION_SFW_B1);
    }
    
    /**
     * Test notification procedure and its GET operation w/ LABEL_SET w/ non-existent LABEL"
     * For B1 standard
     */
    @Test
    public void testNotificationWithLabelSetWithNonExistentLabelB2()
    {
         testNotificationWithLabelSetWithNonExistentLabel(SERVICE_VERSION_SFW_B2);
    }
    
    /**
     * Test notification procedure and its GET operation w/ LABEL_SET w/ non-existent LABEL"
     */
    public void testNotificationWithLabelSetWithNonExistentLabel(int sfwVersion)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, sfwVersion);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Label nonExistentParameterLabel = Label.of(ObjectIdentifier.of(1, 1, 2), FunctionalResourceType.of(ObjectIdentifier.of(1, 1, 50)));
            Label nonExistentParameterLabel2 = Label.of(ObjectIdentifier.of(1, 1, 3), FunctionalResourceType.of(ObjectIdentifier.of(1, 1, 51)));

            // create list of parameters NAME_SET /w a non existent parameter
            ListOfParameters listOfParameters = ListOfParameters.of(nonExistentParameterLabel,
        		    mdCollection.getEventLabelSet().getParameterLabels().get(0),
        		    nonExistentParameterLabel2);
            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid.getInstanceNumber(), listOfParameters),
                                   "START-NOTIFICATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("unknownParamEventIdentifier"));

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test as a secondary procedure w/ a parallel cyclic report
     */
    @Test
    public void testMultipleNotificationSecondary()
    {
        System.out.println("Test the Notification procedure");

        try
        {
            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid_01 = ProcedureInstanceIdentifier
                    .of(ProcedureType.of(OIDs.ocoCyclicReport), ProcedureRole.PRIME, 0);

            // notification procedure identifier
            ProcedureInstanceIdentifier piid_02 = ProcedureInstanceIdentifier
                    .of(ProcedureType.of(OIDs.notification), ProcedureRole.SECONDARY, 0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Arrays.asList(piid_01, piid_02);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(this.minimumAllowedDeliveryCycle,
                                                                                   null,
                                                                                   null,
                                                                                   this.scId,
                                                                                   this.facilityId,
                                                                                   0,
                                                                                   "CSTS_USER",
                                                                                   "CSTS_PT1",
                                                                                   piids);

            // create provider SI configuration
            MdCstsSiConfig mdSiUserConfig = new MdCstsSiConfig(scId, facilityId, 0, "CSTS_PROVIDER", "CSTS_PT1", piids);

            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, mdSiUserConfig, 1);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(
                                   userSi.startCyclicReport(piid_01.getInstanceNumber(),
                                                            mdCollection.getParameterNameSet(),
                                                            100),
                                   "START-CYCLIC-REPORT");

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(userSi.startNotification(piid_02.getInstanceNumber(), mdCollection.getEventNames()),
                                   "START-NOTIFICATION");

            mdCollection.fireAllEvents();
            // wait for notification (there should be none)
            userSi.waitTransferData(10, 500);

            List<Name> notificationEvents = userSi.getNotifiedEvents();
            assertFalse("did not get any events from provider", notificationEvents.isEmpty());

            List<QualifiedParameter> queriedParameters = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                Optional<QualifiedParameter> result = queriedParameters.stream()
                                .filter(qualifiedParameter -> qualifiedParameter.getName().equals(parameter.getName())).findAny();
                assertTrue("missing FR parameter " + parameter.getName(), result.isPresent());

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result.get(), "provider's", "user's");
            }

            // verify that the user SI received all notifications
            for (Event event: mdCollection.getEvents())
            {
                Optional<Name> result = notificationEvents.stream()
                        .filter(eventName -> eventName.getFunctionalResourceName().getType()
                                .equals(event.getLabel().getFunctionalResourceType()))
                        .findAny();
                assertTrue("missing event parameter " + event, result.isPresent());
            }

            assertTrue("no cyclic report received", userSi.getCyclicReportCount() > 0);

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(0), "STOP-CYCLIC-REPORT");

            System.out.println("STOP-NOTIFICATION...");
            TestUtils.verifyResult(userSi.stopNotification(piid_02.getInstanceNumber()), "STOP-NOTIFICATION");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            providerSi.destroy();
            userSi.destroy();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
