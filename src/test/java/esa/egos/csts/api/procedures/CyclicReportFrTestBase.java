package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.TestUtils;
import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.exceptions.ConfigurationParameterNotModifiableException;

/**
 * Test CSTS API cyclic report procedure w/ Fr parameters
 */
public abstract class CyclicReportFrTestBase extends MdCstsTestBase
{

    protected abstract List<Label> createDefaultLabelList();

    protected abstract FunctionalResourceType getFunctionalResource();

    /**
     * Test cyclic report procedure and its GET operation w/ NAME_SET
     */
    @Test
    public void testCyclicReportWithNameSet()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of names for Fr 0 and 1 and set parameters in provider
            System.out.println("Create names for Fr 0 and 1 and set parameters in provider");
            List<Name> names0 = new ArrayList<Name>(testParameters.size());
            List<Name> names1 = new ArrayList<Name>(testParameters.size());
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                this.providerSi.setParameterValue(name0, testParameter.initValue);
                names0.add(name0);

                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                this.providerSi.setParameterValue(name1, testParameter.initValue);
                names1.add(name1);
            }

            // check values at provider
            System.out.println("Check values at provider");
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("provider(Fr=0): found incorrect value of " + testParameter.name,
                           this.providerSi.getParameterValue(names0.get(index)).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + testParameter.name,
                           this.providerSi.getParameterValue(names1.get(index)).equals(testParameter.initValue));
                index++;
            }
            
            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            List<Name> namesList = new ArrayList<Name>(2*testParameters.size());
            namesList.addAll(names0);
            namesList.addAll(names1);
            ListOfParameters listOfParameters = ListOfParameters.of(namesList.toArray(new Name[] {}));

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                                   "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user
            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("user(Fr=0): found incorrect value of " + testParameter.name,
                           this.userSi.getParameterValue(names0.get(index)).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name,
                           this.userSi.getParameterValue(names1.get(index)).equals(testParameter.initValue));
                index++;
            }

            // update values at provider
            System.out.println("Update values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(names0.get(index), testParameter.updatedValue);
                this.providerSi.setParameterValue(names1.get(index), testParameter.updatedValue);
                index++;
            }

            Thread.sleep(300); // wait for several cyclic reports

            // check updated values at user
            System.out.println("Check updated values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("user(Fr=0): found incorrect updated value of " + testParameter.name,
                           this.userSi.getParameterValue(names0.get(index)).equals(testParameter.updatedValue));
                assertTrue("user(Fr=1): found incorrect updated value of " + testParameter.name,
                           this.userSi.getParameterValue(names1.get(index)).equals(testParameter.updatedValue));
                index++;
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test on change cyclic report procedure and its START, STOP and TRANSFER-DATA operations w/ NAME_SET
     */
    @Test
    public void testOnChangeCyclicReportWithNameSet()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of names for Fr 0 and 1 and set parameters in provider
            System.out.println("Create names for Fr 0 and 1 and set parameters in provider");
            List<Name> names0 = new ArrayList<Name>(testParameters.size());
            List<Name> names1 = new ArrayList<Name>(testParameters.size());
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                this.providerSi.setParameterValue(name0, testParameter.initValue);
                names0.add(name0);

                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                this.providerSi.setParameterValue(name1, testParameter.initValue);
                names1.add(name1);
            }

            // check values at provider
            System.out.println("Check values at provider");
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                assertTrue("provider(Fr=0): found incorrect value of " + testParameter.name,
                           this.providerSi.getParameterValue(names0.get(index)).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + testParameter.name,
                           this.providerSi.getParameterValue(names1.get(index)).equals(testParameter.initValue));
                index++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            List<Name> namesList = new ArrayList<Name>(2*testParameters.size());
            namesList.addAll(names0);
            namesList.addAll(names1);
            ListOfParameters listOfParameters = ListOfParameters.of(namesList.toArray(new Name[] {}));

            boolean onChange_01 = true;
            boolean onChange_02 = true;
            boolean onChange_03 = false;

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 300, onChange_01),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_prime);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_01, listOfParameters, 300, onChange_02),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_01);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_02, listOfParameters, 300, onChange_03),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_02);

            Thread.sleep(1000); // wait for the initial complete

            ProcedureInstanceIdentifier piids[] = new ProcedureInstanceIdentifier[] { this.piid_ocr_prime,
                                                                                    this.piid_ocr_secondary_01,
                                                                                    this.piid_ocr_secondary_02 };

            // check values at user for all started procedures
            System.out.println("Check values at user");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                for (index = 0; index < testParameters.size(); index++)
                {
                    TestParameter testParameter = testParameters.get(index);

                    ICstsValue userValue0 = this.userSi.getParameterValue(piid, names0.get(index));
                    assertTrue("user(Fr=0): found incorrect value of " + testParameter.name + ", expected "
                            + testParameter.initValue + " but was " + userValue0 + " for " + piid,
                               userValue0.equals(testParameter.initValue));

                    ICstsValue userValue1 = this.userSi.getParameterValue(piid, names1.get(index));
                    assertTrue("user(Fr=1): found incorrect value of " + testParameter.name + ", expected "
                            + testParameter.initValue + " but was " + userValue1 + " for " + piid,
                               userValue1.equals(testParameter.initValue));
                }
            }

            Thread.sleep(1000); // wait for several cyclic reports

            // verify that all user's procedures received all parameters updates for the first time no matter the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more than one report",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more than one report",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more than one report",
                       2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

            // reset procedure's parameter counters
            this.userSi.resetParameterUpdateCount(this.piid_ocr_prime);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_01);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_02);

            // update just subset of parameters
            int nParams = testParameters.size();
            int nFirst = nParams - nParams / 5;
            int nLast = nParams - 1;
            // update values at provider
            System.out.println("Update values at provider");
            for (index = nFirst; index <= nLast; index++)
            {
                this.providerSi.setParameterValue(names0.get(index), testParameters.get(index).updatedValue);
                this.providerSi.setParameterValue(names1.get(index), testParameters.get(index).updatedValue);
            }

            Thread.sleep(1000); // wait for several cyclic reports

            // check values at user for all started procedures
            System.out.println("Check values at user");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                for (index = nFirst; index <= nLast; index++)
                {
                    TestParameter testParameter = testParameters.get(index);

                    ICstsValue userValue0 = this.userSi.getParameterValue(piid, names0.get(index));
                    assertTrue("user(Fr=0): found incorrect value of " + testParameter.name + ", expected "
                            + testParameter.updatedValue + " but was " + userValue0 + " for " + piid,
                               userValue0.equals(testParameter.updatedValue));

                    ICstsValue userValue1 = this.userSi.getParameterValue(piid, names1.get(index));
                    assertTrue("user(Fr=1): found incorrect value of " + testParameter.name + ", expected "
                            + testParameter.updatedValue + " but was " + userValue1 + " for " + piid,
                               userValue1.equals(testParameter.updatedValue));
                }
            }

            Thread.sleep(300); // wait for several cyclic reports (onChange=false)

            // verify that user's procedures received expected parameters updates depending on the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more than two reports",
                         2*(nLast-nFirst+1), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more than two reports",
                         2*(nLast-nFirst+1), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more than one report",
                         2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

            System.out.println("STOP-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime),
                                   "STOP-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            
            System.out.println("STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_secondary_01),
                                   "STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");

            System.out.println("STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_secondary_02),
                                   "STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test cyclic report procedure and its GET operation w/ LABEL_SET
     */
    @Test
    public void testCyclicReportWithLabelSet()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of Labels for both Fr and set parameters at provider
            System.out.println("Create labels for both Fr and set parameters in provider");
            List<Label> labels = createDefaultLabelList();
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.initValue);
            }

            // check values at provider
            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> providerValue = this.providerSi.getParameterValue(labels.get(index++));
                assertTrue("provider: didn't get 2 values for " + testParameter.name, providerValue.size() == 2);
                assertTrue("provider(Fr=0): found incorrect value of " + testParameter.name,
                           providerValue.get(0).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + testParameter.name,
                           providerValue.get(1).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            ListOfParameters listOfParameters = ListOfParameters.of(labels.toArray(new Label[] {}));

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                    "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user
            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> userValue = this.userSi.getParameterValue(labels.get(index++));
                assertTrue("user: didn't get 2 values for " + testParameter.name, userValue.size() == 2);
                assertTrue("user(Fr=0): found incorrect value of " + testParameter.name,
                           userValue.get(0).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name,
                           userValue.get(1).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.updatedValue);
            }

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user
            System.out.println("Check modified values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> userValue = this.userSi.getParameterValue(labels.get(index++));
                assertTrue("user: didn't get 2 values for " + testParameter.name, userValue.size() == 2);
                assertTrue("user(Fr=0): found incorrect value of " + testParameter.name + ", expected "
                        + testParameter.updatedValue + " but was " + userValue.get(0),
                           userValue.get(0).equals(testParameter.updatedValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name + ", expected "
                        + testParameter.updatedValue + " but was " + userValue.get(1),
                           userValue.get(1).equals(testParameter.updatedValue));
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
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
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create functional resource names for Fr 0 and 1 and set parameters at provider
            System.out.println("Create functional resource names for Fr 0 and 1 and set parameters in provider");
            FunctionalResourceName frn0 = FunctionalResourceName.of(getFunctionalResource(), 0);
            FunctionalResourceName frn1 = FunctionalResourceName.of(getFunctionalResource(), 1);
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Label.of(testParameter.oid, getFunctionalResource()), testParameter.initValue);
            }
            
            // create lists of names of parameters set at provider
            List<Name> names0 = new ArrayList<Name>(testParameters.size());
            List<Name> names1 = new ArrayList<Name>(testParameters.size());
            for (TestParameter testParameter : testParameters)
            {
                names0.add(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0)));
                names1.add(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1)));
            }

            // check values at provider for both Fr
            System.out.println("Check values at provider for both Fr");
            Map<Name, ICstsValue> providerValues0 = this.providerSi.getParameterValues(frn0);
            Map<Name, ICstsValue> providerValues1 = this.providerSi.getParameterValues(frn1);
            Iterator<Name> it0 = names0.iterator();
            Iterator<Name> it1 = names1.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = it0.next();
                assertTrue("provider(Fr=0): found incorrect value of " + name0,
                           providerValues0.get(name0).equals(testParameter.initValue));
                Name name1 = it1.next();
                assertTrue("provider(Fr=1): found incorrect value of " + name1,
                           providerValues1.get(name1).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for Fr 0");
            ListOfParameters listOfParameters0 = ListOfParameters.of(frn0);

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters0, 100),
                    "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user for Fr 0
            System.out.println("Check values at user for Fr 0");
            Map<Name, ICstsValue> userValues = this.userSi.getParameterValues(frn0);
            it0 = names0.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it0.next();
                assertTrue("user(Fr=0): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0)), testParameter.updatedValue);
            }

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user for Fr 0
            System.out.println("Check values at user for Fr 0");
            userValues = this.userSi.getParameterValues(frn0);
            it0 = names0.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it0.next();
                assertTrue("user(Fr=0): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.updatedValue));
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime), "STOP-CYCLIC-REPORT");

            System.out.println("Query parameters for Fr 1");
            ListOfParameters listOfParameters1 = ListOfParameters.of(frn1);

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters1, 100),
                    "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user for Fr 1
            System.out.println("Check values at user for Fr 1");
            userValues = this.userSi.getParameterValues(frn1);
            it1 = names1.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it1.next();
                assertTrue("user(Fr=1): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1)), testParameter.updatedValue);
            }

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user for Fr 1
            System.out.println("Check values at user for Fr 1");
            userValues = this.userSi.getParameterValues(frn1);
            it1 = names1.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Name name = it1.next();
                assertTrue("user(Fr=1): found incorrect value of " + name,
                           userValues.get(name).equals(testParameter.updatedValue));
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
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
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // set parameters at provider and create lists of Labels of parameters set at provider
            System.out.println("Set values of parameters in provider for both Fr");
            List<Label> labels = createDefaultLabelList();
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.initValue);
            }

            // check values at provider for both Fr
            System.out.println("Check values at provider for both Fr");
            Map<Integer, Map<Label, ICstsValue>> providerValues = this.providerSi.getParameterValues(getFunctionalResource());
            assertTrue("provider: didn't get 2 sets of parameters for " + getFunctionalResource(), providerValues.size() == 2);
            Map<Label, ICstsValue> values0 = providerValues.get(Integer.valueOf(0));
            Map<Label, ICstsValue> values1 = providerValues.get(Integer.valueOf(1));
            Iterator<Label> it = labels.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Label label = it.next();
                assertTrue("provider(Fr=0): value not found: " + label, values0.containsKey(label));
                assertTrue("provider(Fr=1): value not found: " + label, values1.containsKey(label));
                assertTrue("provider(Fr=0): found incorrect value of " + label,
                           values0.get(label).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + label,
                           values1.get(label).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for both Fr");
            ListOfParameters listOfParameters = ListOfParameters.of(getFunctionalResource());

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                    "START-CYCLIC-REPORT");

            Thread.sleep(300); // wait for several cyclic reports

            // check values at user for both Fr
            System.out.println("Check values at user for both Fr");
            Map<Integer, Map<Label, ICstsValue>> userValues = this.userSi.getParameterValues(getFunctionalResource());
            assertTrue("user: didn't get 2 sets of parameters for " + getFunctionalResource(), userValues.size() == 2);
            values0 = userValues.get(Integer.valueOf(0));
            values1 = userValues.get(Integer.valueOf(1));
            it = labels.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Label label = it.next();
                assertTrue("user(Fr=0): value not found: " + label, values0.containsKey(label));
                assertTrue("user(Fr=1): value not found: " + label, values1.containsKey(label));
                assertTrue("user(Fr=0): found incorrect value of " + label,
                           values0.get(label).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + label,
                           values1.get(label).equals(testParameter.initValue));
            }

            // change values at provider
            System.out.println("Change values at provider");
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Label.of(testParameter.oid, getFunctionalResource()), testParameter.updatedValue);
            }

            System.out.println("Query updated parameters for Fr 0");
            Thread.sleep(300); // wait for several cyclic reports

            // check updated values at user for both Fr
            System.out.println("Check updated values at user for both Fr");
            userValues = this.userSi.getParameterValues(getFunctionalResource());
            assertTrue("user: didn't get 2 sets of parameters for " + getFunctionalResource(), userValues.size() == 2);
            values0 = userValues.get(Integer.valueOf(0));
            values1 = userValues.get(Integer.valueOf(1));
            it = labels.iterator();
            for (TestParameter testParameter : testParameters)
            {
                Label label = it.next();
                assertTrue("user(Fr=0): value not found: " + label, values0.containsKey(label));
                assertTrue("user(Fr=1): value not found: " + label, values1.containsKey(label));
                assertTrue("user(Fr=0): found incorrect value of " + label,
                           values0.get(label).equals(testParameter.updatedValue));
                assertTrue("user(Fr=1): found incorrect value of " + label,
                           values1.get(label).equals(testParameter.updatedValue));
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test cyclic report procedure and its GET operation w/ EMPTY
     */
    @Test
    public void testCyclicReportWithEmpty()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, ListOfParameters.empty(), 100),
                    "START-CYCLIC-REPORT",
                    CstsResult.FAILURE);

            // verify that diagnostic contains that the Default list not defined
            assertTrue("missing Default list not defined in the diagnostic",
                       userSi.getDiagnostic().contains("Default list not defined"));

            System.out.println("DIAG: "+ this.userSi.getDiagnostic());

            Exception exc_01 = null;
            try
            {
                System.out.println("try to set the default label list to bound provider SI");
                providerSi.setDefaultLabelList(this.piid_ocr_prime, defaultLabelList);
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
            providerSi.setDefaultLabelList(this.piid_ocr_prime, defaultLabelList);

            // create array of Labels for both Fr and set parameters at provider
            System.out.println("Create labels for both Fr and set parameters in provider");
            List<Label> labels = createDefaultLabelList();
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels.get(index++), testParameter.initValue);
            }

            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> providerValue = this.providerSi.getParameterValue(labels.get(index++));
                assertTrue("provider: didn't get 2 values for " + testParameter.name, providerValue.size() == 2);
                assertTrue("provider(Fr=0): found incorrect value of " + testParameter.name,
                           providerValue.get(0).equals(testParameter.initValue));
                assertTrue("provider(Fr=1): found incorrect value of " + testParameter.name,
                           providerValue.get(1).equals(testParameter.initValue));
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("again START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, ListOfParameters.empty(), 10000),
                    "START-CYCLIC-REPORT");
            
            Thread.sleep(300); // wait for several cyclic reports

            // check values at user
            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                List<ICstsValue> userValue = this.userSi.getParameterValue(labels.get(index++));
                assertTrue("user: didn't get 2 values for " + testParameter.name, userValue.size() == 2);
                assertTrue("user(Fr=0): found incorrect value of " + testParameter.name,
                           userValue.get(0).equals(testParameter.initValue));
                assertTrue("user(Fr=1): found incorrect value of " + testParameter.name,
                           userValue.get(1).equals(testParameter.initValue));
            }

            System.out.println("STOP-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime), "STOP-CYCLIC-REPORT");

            System.out.println("UNBIND...");
            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");

            this.providerSi.destroy();
            this.userSi.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
