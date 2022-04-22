package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;
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
import org.junit.Ignore;
import org.junit.Rule;

/**
 * Test the CSTS API at the example of the monitored data service
 */
public class InformationQueryTest
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
    private ProcedureInstanceIdentifier piid_prime = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.informationQuery),
                                                                              ProcedureRole.PRIME,
                                                                              0);
    // secondary inform query procedure identifier
    private ProcedureInstanceIdentifier piid_secondary = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.informationQuery),
                                                                              ProcedureRole.SECONDARY,
                                                                              1);

    // all IQ procedure identifiers
    private List<ProcedureInstanceIdentifier> iq_piids = Arrays.asList(piid_prime, piid_secondary);

    // all procedure identifiers
    private List<ProcedureInstanceIdentifier> piids = Arrays.asList(this.piid_prime, this.piid_secondary);

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
     * Test information query procedure and its GET operation w/ default list => ListOfParameter /w type EMPTY
     */
    @Test
    public void testQueryInformationWithDefaultLabelListB1()
    {
    	testQueryInformationWithDefaultLabelList(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithDefaultLabelListB2()
    {
    	testQueryInformationWithDefaultLabelList(2);
    }
    
    public void testQueryInformationWithDefaultLabelList(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            // the secondary procedure w/ instance number 1 has not defined the default list
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(1, ListOfParameters.empty()),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains that the Default list not defined
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("Default list not defined"));

            Exception exc_01 = null;
            try
            {
                    System.out.println("try to set the default label list to bound provider SI");
                    providerSi.setDefaultLabelList(this.piid_prime, defaultLabelList);
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
            providerSi.setDefaultLabelList(this.piid_prime, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            // try to once again w/ the defined default list
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.empty()),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01
                    .stream().filter(qualifiedParameter -> qualifiedParameter.getName().getFunctionalResourceName()
                            .getType().equals(defaultLabelList.get(0).getFunctionalResourceType()))
                    .findAny();
            assertTrue(result_01.isPresent());

            QualifiedParameter qualifiedParameter_01 = result_01.get();
            System.out.println("got: " + qualifiedParameter_01);

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
     * Test information query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     */
    @Test
    public void testQueryInformationWithFunctionalResourceNameB1() {
    	testQueryInformationWithFunctionalResourceName(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithFunctionalResourceNameB2() {
    	testQueryInformationWithFunctionalResourceName(2);
    }
    
    
    public void testQueryInformationWithFunctionalResourceName(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name name = mdCollection.getFirstParameterName();
            FunctionalResourceName functionalResourceName = name.getFunctionalResourceName();

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(functionalResourceName)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue("missing qualified parameter " + name, result_01.isPresent());

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_01.get(), "provider's", "user's");

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 5);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(functionalResourceName)), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_02 = queriedParameters_02.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue("missing qualified parameter " + name, result_02.isPresent());

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_02.get(), "provider's", "user's");

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
     * Test information query procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_NAME 
     */
    @Test
    public void testQueryInformationWithNonExistentFunctionalResourceNameB1()
    {
    	testQueryInformationWithNonExistentFunctionalResourceName(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithNonExistentFunctionalResourceNameB2()
    {
    	testQueryInformationWithNonExistentFunctionalResourceName(2);
    }
    
    public void testQueryInformationWithNonExistentFunctionalResourceName(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0,
                                                           ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName())),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("crossSupportResources(2).crossSupportFunctionalities(1).unknown(1)"));

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
     * Test information query procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testQueryInformationWithFunctionalResourceTypeB1()
    {
    	testQueryInformationWithFunctionalResourceType(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithFunctionalResourceTypeB2()
    {
    	testQueryInformationWithFunctionalResourceType(2);
    }
    
    public void testQueryInformationWithFunctionalResourceType(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name name = mdCollection.getFirstParameterName();
            FunctionalResourceType functionalResourceType = name.getFunctionalResourceName().getType();

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(functionalResourceType)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_01 = queriedParameters_01.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_01.isPresent());

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_01.get(), "provider's", "user's");

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 10);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            Optional<QualifiedParameter> result_02 = queriedParameters_02.stream()
                    .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
            assertTrue(result_02.isPresent());

            TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result_02.get(), "provider's", "user's");

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
     * Test information query procedure and its GET operation w/ non-existent FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testQueryInformationWithNonExistentFunctionalResourceTypeB1()
    {
    	testQueryInformationWithNonExistentFunctionalResourceType(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithNonExistentFunctionalResourceTypeB2()
    {
    	testQueryInformationWithNonExistentFunctionalResourceType(2);
    }
    
    public void testQueryInformationWithNonExistentFunctionalResourceType(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0,
                                                           ListOfParameters.of(this.nonExistentParameterName
                                                                   .getFunctionalResourceName())),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            System.out.println();
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("crossSupportResources(2).crossSupportFunctionalities(1).unknown(1)"));

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
     * Test information query procedure and its GET operation w/ NAME_SET"
     */
    @Test
    public void testQueryInformationWithNameSetB1()
    {
    	testQueryInformationWithNameSet(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithNameSetB2()
    {
    	testQueryInformationWithNameSet(2);
    }
    
    public void testQueryInformationWithNameSet(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result.get(), "provider's", "user's");
            }

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 20);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (Name name : mdCollection.getParameterNameSet().getParameterNames())
            {
                Optional<QualifiedParameter> result = queriedParameters_02.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().equals(name)).findAny();
                assertTrue(result.isPresent());

                TestUtils.verifyEquals(mdCollection.getQualifiedParameter(name), result.get(), "provider's", "user's");
            }

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
     * Test information query procedure and its GET operation w/ NAME_SET w/ non-existent NAME"
     */
    @Test
    public void testQueryInformationWithNameSetWithNonExistentNameB1()
    {
    	testQueryInformationWithNameSetWithNonExistentName(1);
    }
    
    
    @Test
    public void testQueryInformationWithNameSetWithNonExistentNameB2()
    {
    	testQueryInformationWithNameSetWithNonExistentName(2);
    }
    
    
    public void testQueryInformationWithNameSetWithNonExistentName(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Name parameterName = mdCollection.getParameterNameSet().getParameterNames().get(0);

            // create list of parameters NAME_SET /w a non existent parameter
            ListOfParameters listOfParameters = ListOfParameters.of(parameterName, this.nonExistentParameterName);
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, listOfParameters),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("crossSupportResources(2).crossSupportFunctionalities(1).unknown(1)"));

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
     * Test information query procedure and its GET operation w/ LABEL_SET"
     */
    @Test
    public void testQueryInformationWithLabelSetB1()
    {
    	testQueryInformationWithLabelSet(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithLabelSetB2()
    {
    	testQueryInformationWithLabelSet(2);
    }
    
    public void testQueryInformationWithLabelSet(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterLabelSet()),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                        .filter(qualifiedParameter -> qualifiedParameter.getName().getFunctionalResourceName().getType()
                                .equals(parameter.getLabel().getFunctionalResourceType()))
                        .findAny();
                assertTrue("missing FR parameter " + parameter, result.isPresent());

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result.get(), "provider's", "user's");
            }

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
     * Test information query procedure and its GET operation w/ LABEL_SET w/ non-existent LABEL"
     */
    @Test
    public void testQueryInformationWithLabelSetWithNonExistentLabelB1()
    {
    	testQueryInformationWithLabelSetWithNonExistentLabel(1);
    }
    
    
    @Test
    public void testQueryInformationWithLabelSetWithNonExistentLabelB2()
    {
    	testQueryInformationWithLabelSetWithNonExistentLabel(2);
    }
    
    
    public void testQueryInformationWithLabelSetWithNonExistentLabel(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create FR parameters and attach them to the provider SI
            MdCollection mdCollection = MdCollection.createSimpleMdCollection();
            providerSi.setMdCollection(mdCollection);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            Label nonExistentParameterLabel = Label.of(ObjectIdentifier.of(1, 1, 2), FunctionalResourceType.of(ObjectIdentifier.of(1, 1, 50)));
            Label nonExistentParameterLabel2 = Label.of(ObjectIdentifier.of(1, 1, 3), FunctionalResourceType.of(ObjectIdentifier.of(1, 1, 51)));

            // create list of parameters NAME_SET /w a non existent parameter
            ListOfParameters listOfParameters = ListOfParameters.of(nonExistentParameterLabel,
        		    mdCollection.getParameterLabelSet().getParameterLabels().get(0),
        		    nonExistentParameterLabel2);
            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, listOfParameters),
                                   "QUERY-INFORMATION",
                                   CstsResult.FAILURE);

            // verify that diagnostic contains the OID of the unknown parameter
            assertTrue("missing OID of the non-existent parameter in the GET operation diagnostic",
                       userSi.getDiagnostic().contains("UNKNOWN_PARAMETER_IDENTIFIER"));

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
     * Test information query procedure and its GET operation w/ procedure itself identifier"
     * in order to get its configuration parameters"
     * NOTE: The information query procedure has the pIQnamedLabelLists configuration parameter only
     */
    @Test
    public void testQueryInformationWithItSelfProcedureIdentifierB1()
    {
    	testQueryInformationWithItSelfProcedureIdentifier(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithItSelfProcedureIdentifierB2()
    {
    	testQueryInformationWithItSelfProcedureIdentifier(2);
    }
    
    public void testQueryInformationWithItSelfProcedureIdentifier(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(this.piid_prime)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            System.out.println(queriedParameters_01.get(0).toString());
            assertTrue("did not get just one parameter from provider", queriedParameters_01.get(0).toString().contains("pIQnamedLabelLists"));

            assertTrue("got non-empty list of QualifiedValues but the default label list has not been set yet",
        		    queriedParameters_01.get(0).getQualifiedValues().isEmpty());

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_prime, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(this.piid_prime)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            System.out.println(queriedParameters_02.get(0).toString());
            assertTrue("did not get just one parameter from provider", queriedParameters_02.get(0).toString().contains("pIQnamedLabelLists"));

            assertFalse("got empty list of QualifiedValues but the default label list has already been set",
        		    queriedParameters_02.get(0).getQualifiedValues().isEmpty());

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
     * Test information query procedure and its GET operation w/ a foreign procedure identifier
     * in order to get its configuration parameters"
     * NOTE: The information query procedure has the pIQnamedLabelLists configuration parameter only
     */
    @Test
    public void testQueryInformationWithForeignProcedureIdentifierB1()
    {
    	testQueryInformationWithForeignProcedureIdentifier(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithForeignProcedureIdentifierB2()
    {
    	testQueryInformationWithForeignProcedureIdentifier(2);
    }
    
    
    public void testQueryInformationWithForeignProcedureIdentifier(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(this.piid_secondary)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            System.out.println(queriedParameters_01.get(0).toString());
            assertTrue("did not get just one parameter from provider", queriedParameters_01.get(0).toString().contains("pIQnamedLabelLists"));

            assertTrue("got non-empty list of QualifiedValues but the default label list has not been set yet",
        		    queriedParameters_01.get(0).getQualifiedValues().isEmpty());

            System.out.println("UNBIND...");
            TestUtils.verifyResult(userSi.unbind(), "UNBIND");

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_secondary, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(this.piid_secondary)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            System.out.println(queriedParameters_02.get(0).toString());
            assertTrue("did not get just one parameter from provider", queriedParameters_02.get(0).toString().contains("pIQnamedLabelLists"));

            assertFalse("got empty list of QualifiedValues but the default label list has already been set",
        		    queriedParameters_02.get(0).getQualifiedValues().isEmpty());

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
     * Test information query procedure and its GET operation w/  information query procedure type
     * in order to get configuration parameters of all information query procedures"
     * NOTE: The information query procedure has the pIQnamedLabelLists configuration parameter only
     */
    @Test
    public void testQueryInformationWithProcedureTypeB1()
    {
    	testQueryInformationWithProcedureType(1);
    }
    
    @Test
    @Ignore
    public void testQueryInformationWithProcedureTypeB2()
    {
    	testQueryInformationWithProcedureType(2);
    }
    
    
    public void testQueryInformationWithProcedureType(int version)
    {
        try
        {
            // create provider SI
            MdCstsSiProvider providerSi = new MdCstsSiProvider(this.providerApi, this.mdSiProviderConfig);

            // create user SI
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, this.mdSiUserConfig, version);

            System.out.println("set the default label list to provider SI");
            providerSi.setDefaultLabelList(this.piid_prime, defaultLabelList);
            providerSi.setDefaultLabelList(this.piid_secondary, defaultLabelList);

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            ProcedureType procedureType = this.piid_prime.getType();

            System.out.println("QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, ListOfParameters.of(procedureType)),
                                   "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_01 = userSi.getLastQueriedParameters();
            System.out.println(queriedParameters_01.size());
            System.out.println(queriedParameters_01.toString());
            assertFalse("did not get any parameters from provider", queriedParameters_01.isEmpty());

            for (ProcedureInstanceIdentifier piid : this.iq_piids)
            {
                    Optional<QualifiedParameter> result = queriedParameters_01.stream()
                                    .filter(qualifiedParameter -> qualifiedParameter.getName().getProcedureInstanceIdentifier().getType()
                                            .equals(procedureType) && qualifiedParameter.getName().toString().contains("pIQnamedLabelLists"))
                                    .findAny();
                            assertTrue("missing pIQnamedLabelLists parameter of " + piid, result.isPresent());
            }

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
    public void testMultipleInformationQuerySecondaryB1()
    {
    	testMultipleInformationQuerySecondary(1);
    }
    
    @Test
    @Ignore
    public void testMultipleInformationQuerySecondaryB2()
    {
    	testMultipleInformationQuerySecondary(2);
    }
    
    public void testMultipleInformationQuerySecondary(int version)
    {
        System.out.println("Test the Cyclic report procedure");

        try
        {
            // cyclic report procedure identifier
            ProcedureInstanceIdentifier piid_01 = ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.ocoCyclicReport),
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
            MdCstsSiUser userSi = new MdCstsSiUser(this.userApi, mdSiUserConfig, version);

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
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                Optional<QualifiedParameter> result = queriedParameters_01.stream()
                                .filter(qualifiedParameter -> qualifiedParameter.getName().equals(parameter.getName())).findAny();
                assertTrue("missing FR parameter " + parameter.getName(), result.isPresent());

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result.get(), "provider's", "user's");
            }

            // wait for several cyclic reports
            userSi.waitTransferData(10, 100);

            System.out.println("update provider's parameter value");
            mdCollection.updateIntegerParameter(mdCollection.getParameterNameSet().getParameterNames().get(0), 10);

            System.out.println("again QUERY-INFORMATION...");
            TestUtils.verifyResult(userSi.queryInformation(0, mdCollection.getParameterNameSet()), "QUERY-INFORMATION");

            List<QualifiedParameter> queriedParameters_02 = userSi.getLastQueriedParameters();
            assertFalse("did not get any parameters from provider", queriedParameters_02.isEmpty());

            // verify that the user SI received all queried qualified parameters
            for (FunctionalResourceParameter parameter : mdCollection.getParameters())
            {
                Optional<QualifiedParameter> result = queriedParameters_02.stream()
                                .filter(qualifiedParameter -> qualifiedParameter.getName().equals(parameter.getName())).findAny();
                assertTrue("missing FR parameter " + parameter.getName(), result.isPresent());

                TestUtils.verifyEquals(parameter.toQualifiedParameter(), result.get(), "provider's", "user's");
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
