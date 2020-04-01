package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.enumerations.AppRole;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.main.CstsApi;
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
public class CyclicReportTest
{
    @Rule
    public TestRule testWatcher = new CstsTestWatcher();

    private ICstsApi providerApi;

    private ICstsApi userApi;

    // S/C identifier
    private ObjectIdentifier scId = ObjectIdentifier.of(1, 3, 112, 4, 7, 0);

    // G/S identifier
    private ObjectIdentifier facilityId = ObjectIdentifier.of(1, 3, 112, 4, 6, 0);

    // prime inform query procedure identifier
    private ProcedureInstanceIdentifier piid = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                                              ProcedureRole.PRIME,
                                                                              0);

    // secondary inform query procedure identifier
    private ProcedureInstanceIdentifier piid_secondary = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                                              ProcedureRole.SECONDARY,
                                                                              1);
    // all procedure identifiers
    private List<ProcedureInstanceIdentifier> piids = Arrays.asList(piid, piid_secondary);

    private long minimumAllowedDeliveryCycle = 50;

    static List<Label> defaultLabelList = new ArrayList<Label>();
    {
        ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1000, 1, 3, 1});
        FunctionalResourceType antActualAzimuthType = FunctionalResourceType.of(antActualAzimuthId);

        defaultLabelList = Collections.singletonList(Label.of(antActualAzimuthId, antActualAzimuthType));
    }

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

//    private FunctionalResourceIntegerParameter antActualAzimuth = new FunctionalResourceIntegerParameter(this.antIdId,
//                                                                                                 this.antIdName);
    private Name nonExistentParameterName = Name.of(this.antIdId, this.antIdName);

//    private Label nonExistentParameterLabel = Label.of(this.antIdId, this.antIdType);

    
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

        this.providerApi = new CstsApi("Test Service Provider API", AppRole.PROVIDER);
        this.providerApi.initialize(providerConfigName);
        this.providerApi.start();

        this.userApi = new CstsApi("Test Service User API", AppRole.USER);
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
     * Test cyclic report request procedure and its GET operation w/ default list => ListOfParameter /w type EMPTY
     */
    @Test
    public void testCyclicReportWithDefaultLabelList()
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

            // the secondary procedure w/ instance number 1 has not defined the default list
            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid.getInstanceNumber(),
                                                            ListOfParameters.empty(),
                                                            100),
                                   "START-CYCLIC-REPORT",
                                   CstsResult.FAILURE);
            
            // wait for several cyclic reports
            Thread.sleep(300);

            // verify that empty report was returned
            System.out.println("DIAG: "+userSi.getDiagnostic());
            assertTrue("non-empty cyclic report returned", userSi.getCyclicReportCount() == 0);

            Exception exc_01 = null;
            try
            {
                System.out.println("try to set the default label list to bound provider SI");
                providerSi.setDefaultLabelList(this.piid, defaultLabelList);
            }
            catch (ConfigurationParameterNotModifiableException e)
            {
        	    exc_01 = e;
            }
            assertNotNull("setDefaultLabelList() did not throw ConfigurationParameterNotModifiableException on bound SI", exc_01); 

            // the default list parameter is not dynamically modifiable so unbound is needed
            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            // try to once again w/ the defined default list
            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid.getInstanceNumber(),
                                                            ListOfParameters.empty(),
                                                            100),
                                   "START-CYCLIC-REPORT");

            // wait for several cyclic reports
            Thread.sleep(300);

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().getFunctionalResourceName().getType()
                            .equals(defaultLabelList.get(0).getFunctionalResourceType()))
                    .findAny();
            assertTrue(result_01.isPresent());

            QualifiedParameter qualifiedParameter_01 = result_01.get();
            System.out.println("got: " + qualifiedParameter_01);
          
            System.out.println("STOP-CYCLIC-REPORT ...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     */
    @Test
    public void testCyclicReportWithFunctionalResourceName()
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

            Name name = mdCollection.getParameterNameSet().getParameterNames().get(0);
            FunctionalResourceName functionalResourceName = name.getFunctionalResourceName();

            System.out.println("START-CYCLIC-REPORT ...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            ListOfParameters.of(functionalResourceName),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue("missing qualified parameter " + name, result_01.isPresent());

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_01.get(), "provider's", "user's");

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 5);

            System.out.println("again START-CYCLIC-REPORT ...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            ListOfParameters.of(functionalResourceName),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            // (results are appended to the end of list in each cycle so that search must be in reverse order)
            ListIterator<QualifiedParameter> listIterator = queriedParameters_02.listIterator(queriedParameters_02.size());
            QualifiedParameter result_02 = null;
            while (listIterator.hasPrevious()) {
                QualifiedParameter qualifiedParameter = listIterator.previous();
                if (qualifiedParameter.getName().equals(name)) {
                    result_02 = qualifiedParameter;
                    break;
                }
            }
            assertFalse("missing qualified parameter " + name, result_02 == null);

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_02, "provider's", "user's");

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_NAME 
     */
    @Test
    public void testCyclicReportWithNonExistentFunctionalResourceName()
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

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            ListOfParameters.of(
                                                                 this.nonExistentParameterName.getFunctionalResourceName()),
                                                            100),
                                   "START-CYCLIC-REPORT",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("iso(1).identifiedOrganisation(3).standard(112).ccsds(4).css(4)"
                       		+ ".crossSupportResources(2).crossSupportFunctionalities(1).unknown(1)"));

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
     * Test cyclic report procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testCyclicReportWithFunctionalResourceType()
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

            Name name = mdCollection.getParameterNameSet().getParameterNames().get(0);
            FunctionalResourceType functionalResourceType = name.getFunctionalResourceName().getType();

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            ListOfParameters.of(functionalResourceType),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports
            
            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_01.isPresent());

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_01.get(), "provider's", "user's");

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 10);

            System.out.println("again START-CYCLIC-REPORT ...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            mdCollection.getParameterNameSet(),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            // (results are appended to the end of list in each cycle so that search must be in reverse order)
            ListIterator<QualifiedParameter> listIterator = queriedParameters_02.listIterator(queriedParameters_02.size());
            QualifiedParameter result_02 = null;
            while (listIterator.hasPrevious()) {
                QualifiedParameter qualifiedParameter = listIterator.previous();
                if (qualifiedParameter.getName().equals(name)) {
                    result_02 = qualifiedParameter;
                    break;
                }
            }
            assertFalse("missing qualified parameter " + name, result_02 == null);

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_02, "provider's", "user's");

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testCyclicReportWithNonExistentFunctionalResourceType()
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

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                           ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName()),
                                                           100),
                                   "START-CYCLIC-REPORT",
                                   CstsResult.FAILURE);

            Thread.sleep(300); // wait for several cyclic reports

            // verify that diagnostic contains the OID of the unknown parameter
            System.out.println();
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("iso(1).identifiedOrganisation(3).standard(112).ccsds(4).css(4)"
                       		+ ".crossSupportResources(2).crossSupportFunctionalities(1).unknown(1)"));

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
     * Test cyclic report procedure and its GET operation w/ NAME_SET"
     */
    @Test
    public void testCyclicReportWithNameSet()
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

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            mdCollection.getParameterNameSet(),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result.get(), "provider's", "user's");
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 20);

            System.out.println("again START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(),
                                                            mdCollection.getParameterNameSet(),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            // (results are appended to the end of list in each cycle so that search must be in reverse order)
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                QualifiedParameter result_02 = null;
                ListIterator<QualifiedParameter> listIterator = queriedParameters_02.listIterator(queriedParameters_02.size());
                while (listIterator.hasPrevious()) {
                    QualifiedParameter qualifiedParameter = listIterator.previous();
                    if (qualifiedParameter.getName().equals(name)) {
                        result_02 = qualifiedParameter;
                        break;
                    }
                }
                assertFalse("missing qualified parameter " + name, result_02 == null);

                TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_02, "provider's", "user's");
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ NAME_SET w/ non-existent NAME"
     */
    @Test
    public void testCyclicReportWithNameSetWithNonExistentName()
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

            Name parameterName = mdCollection.getParameterNameSet().getParameterNames().get(0);

            // create list of parameters NAME_SET /w a non existent parameter
            ListOfParameters listOfParameters = ListOfParameters.of(parameterName, this.nonExistentParameterName);
            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid.getInstanceNumber(),
                                                            listOfParameters,
                                                            100),
                                   "START-CYCLIC-REPORT",
                                   CstsResult.FAILURE);

            Thread.sleep(300); // wait for several cyclic reports

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("iso(1).identifiedOrganisation(3).standard(112).ccsds(4).css(4)"
                       		+ ".crossSupportResources(2).crossSupportFunctionalities(1).unknown(1)"));

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
     * Test cyclic report procedure and its GET operation w/ LABEL_SET"
     */
    @Test
    public void testCyclicReportWithLabelSet()
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

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid.getInstanceNumber(),
                                                            mdCollection.getParameterLabelSet(),
                                                            100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            // (results are appended to the end of list in each cycle so that search must be in reverse order)
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                QualifiedParameter result = null;
                ListIterator<QualifiedParameter> listIterator = queriedParameters_01.listIterator(queriedParameters_01.size());
                while (listIterator.hasPrevious()) {
                    QualifiedParameter qualifiedParameter = listIterator.previous();
                    if (qualifiedParameter.getName().getFunctionalResourceName().getType()
                            .equals(parameter.getLabel().getFunctionalResourceType())) {
                        result = qualifiedParameter;
                        break;
                    }
                }
                assertFalse("missing FR parameter " + parameter, result == null);

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result, "provider's", "user's");
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ LABEL_SET w/ non-existent LABEL"
     */
    @Test
    public void testCyclicReportWithLabelSetWithNonExistentLabel()
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

            Label nonExistentParameterLabel = Label.of(ObjectIdentifier.of(1, 1, 2), FunctionalResourceType.of(ObjectIdentifier.of(1, 1, 50)));
            Label nonExistentParameterLabel2 = Label.of(ObjectIdentifier.of(1, 1, 3), FunctionalResourceType.of(ObjectIdentifier.of(1, 1, 51)));

            // create list of parameters NAME_SET /w a non existent parameter
            ListOfParameters listOfParameters = ListOfParameters.of(nonExistentParameterLabel,
        		    mdCollection.getParameterLabelSet().getParameterLabels().get(0),
        		    nonExistentParameterLabel2);
            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid.getInstanceNumber(), listOfParameters, 100),
                                   "START-CYCLIC-REPORT",
                                   CstsResult.FAILURE);

            Thread.sleep(300); // wait for several cyclic reports

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("iso(1).unknown(1)"));

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
     * Test cyclic report procedure and its GET operation w/ procedure itself identifier"
     * in order to get its configuration parameters"
     * NOTE: The information query procedure has the pCRnamedLabelLists configuration parameter only
     */
    @Test
    public void testCyclicReportWithItSelfProcedureIdentifier()
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(), ListOfParameters.of(this.piid), 100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

//            System.out.println("Q1: " + queriedParameters_01.get(0).toString());
            System.out.println("Q1: " + queriedParameters_01.toString());
            assertTrue("did not get just one parameter from provider", queriedParameters_01.get(0)
                       .toString().contains("pCRnamedLabelLists"));

            assertTrue("got non-empty list of QualifiedValues but the default label list has not been set yet",
        		    queriedParameters_01.get(0).getQualifiedValues().isEmpty());

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(), ListOfParameters.of(this.piid), 100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            int lastqpind = queriedParameters_02.size()-1;

            System.out.println("Q2: " + queriedParameters_02.get(lastqpind).toString());
            Optional<QualifiedParameter> result = queriedParameters_02.stream()
                    .filter(qp -> qp.toString().equals("pCRnamedLabelLists")).findAny();
            assertFalse("did not get just one parameter from provider", result.isPresent());

            assertFalse("got empty list of QualifiedValues but the default label list has already been set",
        		    queriedParameters_02.get(lastqpind).getQualifiedValues().isEmpty());

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ a foreign procedure identifier
     * in order to get its configuration parameters"
     * NOTE: The cyclic report procedure has the pCRnamedLabelLists configuration parameter only
     */
    @Test
    public void testCyclicReportWithForeignProcedureIdentifier()
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid_secondary.getInstanceNumber(),
                                                            ListOfParameters.of(this.piid_secondary), 100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            System.out.println("Q1: " + queriedParameters_01.get(0).toString());
            assertTrue("did not get just one parameter from provider", queriedParameters_01.get(0)
                       .toString().contains("pCRnamedLabelLists"));

            assertTrue("got non-empty list of QualifiedValues but the default label list has not been set yet",
        		    queriedParameters_01.get(0).getQualifiedValues().isEmpty());

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid_secondary.getInstanceNumber()), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_secondary, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid_secondary.getInstanceNumber(),
                                                            ListOfParameters.of(this.piid_secondary), 100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastCyclicParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            int lastqpind = queriedParameters_02.size()-1;

            System.out.println("Q2: " + queriedParameters_02.get(lastqpind).toString());
            Optional<QualifiedParameter> result = queriedParameters_02.stream()
                    .filter(qp -> qp.toString().equals("pCRnamedLabelLists")).findAny();
            assertFalse("did not get just one parameter from provider", result.isPresent());

            assertFalse("got empty list of QualifiedValues but the default label list has already been set",
        		    queriedParameters_02.get(lastqpind).getQualifiedValues().isEmpty());

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid_secondary.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
     * Test cyclic report procedure and its GET operation w/ cyclic report procedure type
     * in order to get configuration parameters of all cyclic report procedures"
     * NOTE: The cyclic report procedure has the pCRnamedLabelLists configuration parameter only
     */
    @Test
    public void testCyclicReportWithProcedureType()
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, 1);

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid, defaultLabelList);
            providerSi.setDefaultLabelList(this.piid_secondary, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            ProcedureType procedureType = this.piid.getType();

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(piid.getInstanceNumber(), ListOfParameters.of(procedureType), 100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastCyclicParameters();
            System.out.println(queriedParameters_01.size());
            System.out.println(queriedParameters_01.toString());
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            for (ProcedureInstanceIdentifier piid : this.piids)
            {
                    Optional<QualifiedParameter> result = queriedParameters_01.stream()
                                    .filter(qualifiedParameter -> qualifiedParameter.getName().getProcedureInstanceIdentifier().getType()
                                            .equals(procedureType) && qualifiedParameter.getName().toString().contains("pCRnamedLabelLists"))
                                    .findAny();
                            assertTrue("missing pCRnamedLabelLists parameter of " + piid, result.isPresent());
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(piid.getInstanceNumber()), "STOP-CYCLIC-REPORT");

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
    public void testMultiplenformationQuerySecondary()
    {
        System.out.println("Test the Cyclic report procedure");

        try
        {
            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid_01 = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                                                 ProcedureRole.PRIME,
                                                                                 0);
            // inform query procedure identifier
            ProcedureInstanceIdentifier piid_02 = ProcedureInstanceIdentifier
                    .of(ProcedureType.of(OIDs.informationQuery), ProcedureRole.SECONDARY, 0);

            // all procedure identifiers
            List<ProcedureInstanceIdentifier> piids = Arrays.asList(piid_01, piid_02);

            // create provider SI configuration
            MdCstsSiProviderConfig mdSiProviderConfig = new MdCstsSiProviderConfig(this.minimumAllowedDeliveryCycle,
                                                                                   defaultLabelList,
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
                                                            3000),
                                   "START-CYCLIC-REPORT");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            // (results are appended to the end of list in each cycle so that search must be in reverse order)
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                QualifiedParameter result = null;
                ListIterator<QualifiedParameter> listIterator = queriedParameters_01.listIterator(queriedParameters_01.size());
                while (listIterator.hasPrevious()) {
                    QualifiedParameter qualifiedParameter = listIterator.previous();
                    if (qualifiedParameter.getName().equals(parameter.getName())) {
                        result = qualifiedParameter;
                        break;
                    }
                }
                assertFalse("missing FR parameter " + parameter.getName(), result == null);

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result, "provider's", "user's");
            }

            // wait for several cyclic reports
            Thread.sleep(4000);

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 10);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                QualifiedParameter result = null;
                ListIterator<QualifiedParameter> listIterator = queriedParameters_02.listIterator(queriedParameters_02.size());
                while (listIterator.hasPrevious()) {
                    QualifiedParameter qualifiedParameter = listIterator.previous();
                    if (qualifiedParameter.getName().equals(parameter.getName())) {
                        result = qualifiedParameter;
                        break;
                    }
                }
                assertFalse("missing FR parameter " + parameter.getName(), result == null);

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result, "provider's", "user's");
            }

            assertTrue("no cyclic report received", userSi.getCyclicReportCount() > 0);

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(0), "STOP-CYCLIC-REPORT");

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
