package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    protected List<Label> createDefaultLabelList()
    {
        return MdCstsTestBase.testParameters.stream().map(tp -> Label.of(tp.oid, getFunctionalResource())).collect(Collectors.toList());
    }

    protected abstract FunctionalResourceType getFunctionalResource();

    /**
     * Test cyclic report procedure and its GET operation w/ NAME_SET
     */
    @Test
    public void testCyclicReportWithNameSet()
    {
        try
        {
            // set FRs of instances 0 and 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("Create names for Fr instances 0 and 1 and set parameters in provider");
            Name[] names0 = new Name[testParameters.size()];
            Name[] names1 = new Name[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                names0[index] = name0;
                this.providerSi.setParameterValue(name0, testParameter.initValue);

                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                names1[index] = name1;
                this.providerSi.setParameterValue(name1, testParameter.initValue);
                
                index++;
            }

            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                TestUtils.verifyNameValue("provider", testParameter.initValue,
                                          this.providerSi.getParameterValue(names0[index]), names0[index]);
                TestUtils.verifyNameValue("provider", testParameter.initValue,
                                          this.providerSi.getParameterValue(names1[index]), names1[index]);
                index++;
            }
            
            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            Name[] namesList = new Name[2*testParameters.size()];
            System.arraycopy(names0, 0, namesList, 0, testParameters.size());
            System.arraycopy(names1, 0, namesList, testParameters.size(), testParameters.size());
            ListOfParameters listOfParameters = ListOfParameters.of(namesList);

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                                   "START-CYCLIC-REPORT");

            this.userSi.waitTransferData(10, 1000);

            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                TestUtils.verifyNameValue("user", testParameter.initValue,
                                          this.userSi.getParameterValue(this.piid_ocr_prime, names0[index]), names0[index]);
                TestUtils.verifyNameValue("user", testParameter.initValue,
                                          this.userSi.getParameterValue(this.piid_ocr_prime, names1[index]), names1[index]);
                index++;
            }

            System.out.println("Update values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(names0[index], testParameter.updatedValue);
                this.providerSi.setParameterValue(names1[index], testParameter.updatedValue);
                index++;
            }

            this.userSi.waitTransferData(5, 500);

            System.out.println("Check updated values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                TestUtils.verifyNameValue("user", testParameter.updatedValue,
                                          this.userSi.getParameterValue(this.piid_ocr_prime, names0[index]), names0[index]);
                TestUtils.verifyNameValue("user", testParameter.updatedValue,
                                          this.userSi.getParameterValue(this.piid_ocr_prime, names1[index]), names1[index]);
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
            // set FRs of instances 0 and 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("Create names for instances 0 and 1 and set parameters in provider");
            Name[] names0 = new Name[testParameters.size()];
            Name[] names1 = new Name[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                names0[index] = name0;
                this.providerSi.setParameterValue(name0, testParameter.initValue);

                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                names1[index] = name1;
                this.providerSi.setParameterValue(name1, testParameter.initValue);
                
                index++;
            }

            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                TestUtils.verifyNameValue("provider", testParameter.initValue,
                                          this.providerSi.getParameterValue(names0[index]), names0[index]);
                TestUtils.verifyNameValue("provider", testParameter.initValue,
                                          this.providerSi.getParameterValue(names1[index]), names1[index]);
                index++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            Name[] namesList = new Name[2*testParameters.size()];
            System.arraycopy(names0, 0, namesList, 0, testParameters.size());
            System.arraycopy(names1, 0, namesList, testParameters.size(), testParameters.size());
            ListOfParameters listOfParameters = ListOfParameters.of(namesList);

            boolean onChange_01 = true;
            boolean onChange_02 = true;
            boolean onChange_03 = false;

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100, onChange_01),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_prime);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_01, listOfParameters, 100, onChange_02),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_01);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_02, listOfParameters, 100, onChange_03),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_02);

            this.userSi.waitTransferData(5, 500);

            ProcedureInstanceIdentifier piids[] = new ProcedureInstanceIdentifier[] { this.piid_ocr_prime,
                                                                                      this.piid_ocr_secondary_01,
                                                                                      this.piid_ocr_secondary_02 };

            System.out.println("Check values at user for all started procedures");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                System.out.println("piid: " + piid);
                for (index = 0; index < testParameters.size(); index++)
                {
                    TestParameter testParameter = testParameters.get(index);

                    ICstsValue userValue0 = this.userSi.getParameterValue(piid, names0[index]);
                    TestUtils.verifyNameValue("provider", testParameter.initValue, userValue0, names0[index]);

                    ICstsValue userValue1 = this.userSi.getParameterValue(piid, names1[index]);
                    TestUtils.verifyNameValue("provider", testParameter.initValue, userValue1, names1[index]);
                }
            }

            this.userSi.waitTransferData(5, 500);

            // verify that all user's procedures received all parameters updates for the first time no matter the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
                       2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

            // reset procedure's parameter counters
            this.userSi.resetParameterUpdateCount(this.piid_ocr_prime);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_01);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_02);

            System.out.println("Update just subset of parameters at provider");
            int nParams = 0; // number of changed parameters
            for (index = 0; index < testParameters.size(); index++)
            {
                if (index % 4 != 0) continue; // change every fourth parameter
                this.providerSi.setParameterValue(names0[index], testParameters.get(index).updatedValue);
                nParams++;
                this.providerSi.setParameterValue(names1[index], testParameters.get(index).updatedValue);
                nParams++;
            }

            this.userSi.waitTransferData(10, 500);

            System.out.println("Check values at user for all started procedures");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                System.out.println("piid: " + piid);
                for (index = 0; index < testParameters.size(); index+=4) // check every fourth parameter
                {
                    TestParameter testParameter = testParameters.get(index);

                    ICstsValue userValue0 = this.userSi.getParameterValue(piid, names0[index]);
                    TestUtils.verifyNameValue("user", testParameter.updatedValue, userValue0, names0[index]);

                    ICstsValue userValue1 = this.userSi.getParameterValue(piid, names1[index]);
                    TestUtils.verifyNameValue("user", testParameter.updatedValue, userValue1, names1[index]);
                }
            }

            this.userSi.waitTransferData(5, 500);

            // verify that user's procedures received expected parameters updates depending on the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         nParams, this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         nParams, this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
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
            // set FRs of instances 0 and 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("Create array of labels for both instance numbers 0,1 and set parameters in provider");
            Label[] labels = new Label[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = Label.of(testParameter.oid, FunctionalResourceType.of(getFunctionalResource().getOid()));
                labels[index++] = label;
                this.providerSi.setParameterValue(label, testParameter.initValue);
            }

            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Map<Long, ICstsValue> providerValues = this.providerSi.getParameterValues(labels[index]);

                assertTrue("provider: didn't get 2 values for " + testParameter.name, providerValues.size() == 2);

                TestUtils.verifyLabelValues("provider", testParameter.initValue, providerValues, labels[index]);

                index++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            ListOfParameters listOfParameters = ListOfParameters.of(labels);

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                    "START-CYCLIC-REPORT");

            this.userSi.waitTransferData(10, 1000);

            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Map<Long, ICstsValue> userValues = this.userSi.getParameterValues(this.piid_ocr_prime, labels[index]);

                assertTrue("user: didn't get 2 values for " + testParameter.name, userValues.size() == 2);

                TestUtils.verifyLabelValues("user", testParameter.initValue, userValues, labels[index]);
                
                index++;
            }

            System.out.println("Change values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(labels[index++], testParameter.updatedValue);
            }

            this.userSi.waitTransferData(10, 500);

            System.out.println("Check modified values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Map<Long, ICstsValue> userValues = this.userSi.getParameterValues(this.piid_ocr_prime, labels[index]);

                assertTrue("user: didn't get 2 values for " + testParameter.name, userValues.size() == 2);

                TestUtils.verifyLabelValues("user", testParameter.updatedValue, userValues, labels[index]);
                
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
     * Test on change cyclic report procedure and its START, STOP and TRANSFER-DATA operations w/ LABEL_SET
     */
    @Test
    public void testOnChangeCyclicReportWithLabelSet()
    {
        try
        {
            // set FRs of instances 0 and 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("Create array of labels for both instance numbers 0,1 and set parameters in provider");
            Label[] labels = new Label[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = Label.of(testParameter.oid, FunctionalResourceType.of(getFunctionalResource().getOid()));
                labels[index++] = label;
                this.providerSi.setParameterValue(label, testParameter.initValue);
            }

            System.out.println("Check values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Map<Long, ICstsValue> providerValues = this.providerSi.getParameterValues(labels[index]);

                assertTrue("provider: didn't get 2 values for " + testParameter.name, providerValues.size() == 2);

                TestUtils.verifyLabelValues("provider", testParameter.initValue, providerValues, labels[index]);

                index++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested parameters
            ListOfParameters listOfParameters = ListOfParameters.of(labels);

            boolean onChange_01 = true;
            boolean onChange_02 = true;
            boolean onChange_03 = false;

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100, onChange_01),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_prime);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_01, listOfParameters, 100, onChange_02),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_01);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_02, listOfParameters, 100, onChange_03),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_02);

            this.userSi.waitTransferData(5, 500);

            ProcedureInstanceIdentifier piids[] = new ProcedureInstanceIdentifier[] { this.piid_ocr_prime,
                                                                                      this.piid_ocr_secondary_01,
                                                                                      this.piid_ocr_secondary_02 };

            System.out.println("Check values at user for all started procedures");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                System.out.println("piid: " + piid);
                for (index = 0; index < testParameters.size(); index++)
                {
                    TestParameter testParameter = testParameters.get(index);
                    Map<Long, ICstsValue> userValues = this.userSi.getParameterValues(piid, labels[index]);
                    assertTrue("user: didn't get 2 values for " + testParameter.name, userValues.size() == 2);
                    TestUtils.verifyLabelValues("user", testParameter.initValue, userValues, labels[index]);
                }
            }

            this.userSi.waitTransferData(5, 500);

            // verify that all user's procedures received all parameters updates for the first time no matter the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
                       2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

            // reset procedure's parameter counters
            this.userSi.resetParameterUpdateCount(this.piid_ocr_prime);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_01);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_02);

            System.out.println("Update just subset of parameters at provider");
            int nParams = 0; // number of changed parameters
            for (index = 0; index < testParameters.size(); index++)
            {
                if (index % 4 != 0) continue; // change every fourth parameter
                this.providerSi.setParameterValue(labels[index], testParameters.get(index).updatedValue);
                nParams++;
            }

            this.userSi.waitTransferData(10, 500);

            System.out.println("Check values at user for all started procedures");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                System.out.println("piid: " + piid);
                for (index = 0; index < testParameters.size(); index+=4) // check every fourth parameter
                {
                    TestParameter testParameter = testParameters.get(index);
                    Map<Long, ICstsValue> userValues = this.userSi.getParameterValues(piid, labels[index]);
                    TestUtils.verifyLabelValues("user", testParameter.updatedValue, userValues, labels[index]);
                }
            }

            this.userSi.waitTransferData(5, 500);

            // verify that user's procedures received expected parameters updates depending on the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         2*nParams, this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         2*nParams, this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
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
     * Test cyclic report procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     */
    @Test
    public void testCyclicReportWithFunctionalResourceName()
    {
        try
        {
            // set FRs of instance 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            FunctionalResourceName frn = FunctionalResourceName.of(getFunctionalResource(), 1);

            System.out.println("Create functional resource names for instance 1 and set parameters in provider");
            Name[] names = new Name[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                names[index++] = name;
                this.providerSi.setParameterValue(name, testParameter.initValue);
            }
            
            System.out.println("Check values at provider");
            Map<Name, ICstsValue> providerValues = this.providerSi.getParameterValues(frn);

            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name = names[index++];
                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues.get(name), name);
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for both instances");
            ListOfParameters listOfParameters = ListOfParameters.of(frn);

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                    "START-CYCLIC-REPORT");

            this.userSi.waitTransferData(10, 1000);

            System.out.println("Check values at user");
            Map<Name, ICstsValue> userValues = this.userSi.getParameterValues(this.piid_ocr_prime, frn);
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name = names[index++];
                TestUtils.verifyNameValue("user", testParameter.initValue, userValues.get(name), name);
            }

            System.out.println("Change values at provider");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(names[index++], testParameter.updatedValue);
            }

            this.userSi.waitTransferData(10, 500);

            System.out.println("Check updated values at user");
            userValues = this.userSi.getParameterValues(this.piid_ocr_prime, frn);
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name = names[index++];
                TestUtils.verifyNameValue("user", testParameter.updatedValue, userValues.get(name), name);
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
     * Test on change cyclic report procedure and its GET operation w/ FUNCTIONAL_RESOURCE_NAME
     */
    @Test
    public void testOnChangeCyclicReportWithFunctionalResourceName()
    {
        try
        {
            // set FRs of instances 0, 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            FunctionalResourceName frn0 = FunctionalResourceName.of(getFunctionalResource(), 0);
            FunctionalResourceName frn1 = FunctionalResourceName.of(getFunctionalResource(), 1);

            System.out.println("Create functional resource names for all instances and set parameters in provider");
            Name[] names0 = new Name[testParameters.size()];
            Name[] names1 = new Name[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                names0[index] = name0;
                this.providerSi.setParameterValue(name0, testParameter.initValue);

                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                names1[index] = name1;
                this.providerSi.setParameterValue(name1, testParameter.initValue);

                index++;
            }
            
            System.out.println("Check values at provider for all instances");
            Map<Name, ICstsValue> providerValues0 = this.providerSi.getParameterValues(frn0);
            Map<Name, ICstsValue> providerValues1 = this.providerSi.getParameterValues(frn1);

            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Name name0 = names0[index];
                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues0.get(name0), name0);
                Name name1 = names1[index];
                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues1.get(name1), name1);
                index++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for all instances");
            ListOfParameters listOfParameters0 = ListOfParameters.of(frn0);
            ListOfParameters listOfParameters1 = ListOfParameters.of(frn1);

            boolean onChange_01 = true;
            boolean onChange_02 = true;
            boolean onChange_03 = false;

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters0, 100, onChange_01),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_prime);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_01, listOfParameters1, 100, onChange_02),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_01);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_02, listOfParameters1, 100, onChange_03),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_02);

            this.userSi.waitTransferData(10, 60000);

            System.out.println("check values at user for all started procedures");
            Map<Name, ICstsValue> userValues;
            System.out.println("piid: " + this.piid_ocr_prime);
            userValues = this.userSi.getParameterValues(this.piid_ocr_prime, frn0);
            for (index = 0; index < testParameters.size(); index++)
            {
                Name name = names0[index];
                TestUtils.verifyNameValue("provider", testParameters.get(index).initValue, userValues.get(name), name);
            }
            System.out.println("piid: " + this.piid_ocr_secondary_01);
            userValues = this.userSi.getParameterValues(this.piid_ocr_secondary_01, frn1);
            for (index = 0; index < testParameters.size(); index++)
            {
                Name name = names1[index];
                TestUtils.verifyNameValue("provider", testParameters.get(index).initValue, userValues.get(name), name);
            }
            System.out.println("piid: " + this.piid_ocr_secondary_02);
            userValues = this.userSi.getParameterValues(this.piid_ocr_secondary_02, frn1);
            for (index = 0; index < testParameters.size(); index++)
            {
                Name name = names1[index];
                TestUtils.verifyNameValue("provider", testParameters.get(index).initValue, userValues.get(name), name);
            }

            this.userSi.waitTransferData(5, 500);

            // verify that all user's procedures received all parameters updates for the first time no matter the onChange value
            if (testParameters.size() != this.userSi.getParameterUpdateCount(this.piid_ocr_prime))
            {
                System.out.println(this.userSi.getParameterValues(this.piid_ocr_prime, frn0));
            }
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
                       testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

            // reset procedure's parameter counters
            this.userSi.resetParameterUpdateCount(this.piid_ocr_prime);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_01);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_02);

            System.out.println("Update just subset of parameters at provider");
            int nParams0 = 0;
            int nParams1 = 0;
            for (index = 0; index < testParameters.size(); index++)
            {
                if (index % 4 == 0) // change every fourth parameter
                {
                    this.providerSi.setParameterValue(names0[index], testParameters.get(index).updatedValue);
                    nParams0++;
                }
                if (index % 3 == 0) // change every third parameter
                {
                    this.providerSi.setParameterValue(names1[index], testParameters.get(index).updatedValue);
                    nParams1++;
                }
            }
            
            this.userSi.waitTransferData(10, 500);

            System.out.println("Check updated values at user for all started procedures");

            System.out.println("piid: " + this.piid_ocr_prime);
            userValues = this.userSi.getParameterValues(this.piid_ocr_prime, frn0);
            for (index = 0; index < testParameters.size(); index+=4) // check every fourth parameter
            {
                TestUtils.verifyNameValue("user", testParameters.get(index).updatedValue, userValues.get(names0[index]), names0[index]);
            }

            System.out.println("piid: " + this.piid_ocr_secondary_01);
            userValues = this.userSi.getParameterValues(this.piid_ocr_secondary_01, frn1);
            for (index = 0; index < testParameters.size(); index+=3) // check every third parameter
            {
                TestUtils.verifyNameValue("user", testParameters.get(index).updatedValue, userValues.get(names1[index]), names1[index]);
            }

            System.out.println("piid: " + this.piid_ocr_secondary_02);
            userValues = this.userSi.getParameterValues(this.piid_ocr_secondary_02, frn1);
            for (index = 0; index < testParameters.size(); index+=3) // check every third parameter
            {
                TestUtils.verifyNameValue("user", testParameters.get(index).updatedValue, userValues.get(names1[index]), names1[index]);
            }

            this.userSi.waitTransferData(5, 500);

            // verify that user's procedures received expected parameters updates depending on the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         nParams0, this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         nParams1, this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
                         testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

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
     * Test cyclic report procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testCyclicReportWithFunctionalResourceType()
    {
        try
        {
            // set FRs of both instances 0 and 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            FunctionalResourceType frt = FunctionalResourceType.of(getFunctionalResource().getOid());
            
            System.out.println("Set values of parameters in provider for both instances");
            Label[] labels = new Label[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = Label.of(testParameter.oid, frt);
                labels[index++] = label;
                this.providerSi.setParameterValue(label, testParameter.initValue);
            }

            System.out.println("Check values at provider for all instances");
            Map<Long, Map<Label, ICstsValue>> providerValues = this.providerSi.getParameterValues(getFunctionalResource());
            assertTrue("provider: didn't get 2 sets of parameters for " + getFunctionalResource(), providerValues.size() == 2);

            // should contain 2 instances
            Map<Label, ICstsValue> values0 = providerValues.get(Long.valueOf(0));
            Map<Label, ICstsValue> values1 = providerValues.get(Long.valueOf(1));
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = labels[index++];
                assertTrue("provider(instNum=0): value not found: " + label, values0.containsKey(label));
                assertTrue("provider(instNum=1): value not found: " + label, values1.containsKey(label));
                Map<Long, ICstsValue> values = new HashMap<>();
                values.put(Long.valueOf(0), values0.get(label));
                values.put(Long.valueOf(1), values1.get(label));
                TestUtils.verifyLabelValues("provider", testParameter.initValue, values, label);
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for both instances");
            ListOfParameters listOfParameters = ListOfParameters.of(getFunctionalResource());

            System.out.println("START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100),
                    "START-CYCLIC-REPORT");

            this.userSi.waitTransferData(10, 1000);

            System.out.println("Check values at user for both instances");
            Map<Long, Map<Label, ICstsValue>> userValues = this.userSi.getParameterValues(this.piid_ocr_prime, getFunctionalResource());
            assertTrue("user: didn't get 2 sets of parameters for " + getFunctionalResource(), userValues.size() == 2);
            values0 = userValues.get(Long.valueOf(0));
            values1 = userValues.get(Long.valueOf(1));
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = labels[index++];
                assertTrue("user(instNum=0): value not found: " + label, values0.containsKey(label));
                assertTrue("user(instNum=1): value not found: " + label, values1.containsKey(label));
                Map<Long, ICstsValue> values = new HashMap<>();
                values.put(Long.valueOf(0), values0.get(label));
                values.put(Long.valueOf(1), values1.get(label));
                TestUtils.verifyLabelValues("user", testParameter.initValue, values, label);
            }

            System.out.println("Change values at provider");
            for (TestParameter testParameter : testParameters)
            {
                this.providerSi.setParameterValue(Label.of(testParameter.oid, getFunctionalResource()), testParameter.updatedValue);
            }

            System.out.println("Query updated parameters for both instances");
            this.userSi.waitTransferData(5, 500);

            System.out.println("Check updated values at user for both instances");
            userValues = this.userSi.getParameterValues(this.piid_ocr_prime, getFunctionalResource());
            assertTrue("user: didn't get 2 sets of parameters for " + getFunctionalResource(), userValues.size() == 2);
            values0 = userValues.get(Long.valueOf(0));
            values1 = userValues.get(Long.valueOf(1));
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = labels[index++];
                assertTrue("user(instNum=0): value not found: " + label, values0.containsKey(label));
                assertTrue("user(instNum=1): value not found: " + label, values1.containsKey(label));
                Map<Long, ICstsValue> values = new HashMap<>();
                values.put(Long.valueOf(0), values0.get(label));
                values.put(Long.valueOf(1), values1.get(label));
                TestUtils.verifyLabelValues("user", testParameter.updatedValue, values, label);
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
     * Test on change cyclic report procedure and its GET operation w/ FUNCTIONAL_RESOURCE_TYPE
     */
    @Test
    public void testOnChangeCyclicReportWithFunctionalResourceType()
    {
        try
        {
            // set FRs of both instances 0 and 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            System.out.println("Set values of parameters in provider for both instances");
            Label[] labels = new Label[testParameters.size()];
            int index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = Label.of(testParameter.oid, FunctionalResourceType.of(getFunctionalResource().getOid()));
                labels[index++] = label;
                this.providerSi.setParameterValue(label, testParameter.initValue);
            }

            System.out.println("Check values at provider for all instances");
            Map<Long, Map<Label, ICstsValue>> providerValues = this.providerSi.getParameterValues(getFunctionalResource());
            assertTrue("provider: didn't get 2 sets of parameters for " + getFunctionalResource(), providerValues.size() == 2);

            Map<Label, ICstsValue> values0 = providerValues.get(Long.valueOf(0));
            Map<Label, ICstsValue> values1 = providerValues.get(Long.valueOf(1));
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Label label = labels[index++];
                assertTrue("provider(instNum=0): value not found: " + label, values0.containsKey(label));
                assertTrue("provider(instNum=1): value not found: " + label, values1.containsKey(label));
                Map<Long, ICstsValue> values = new HashMap<>();
                values.put(Long.valueOf(0), values0.get(label));
                values.put(Long.valueOf(1), values1.get(label));
                TestUtils.verifyLabelValues("provider", testParameter.initValue, values, label);
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            System.out.println("Query parameters for both instances");
            ListOfParameters listOfParameters = ListOfParameters.of(getFunctionalResource());

            boolean onChange_01 = true;
            boolean onChange_02 = true;
            boolean onChange_03 = false;

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters, 100, onChange_01),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_prime);

            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_01, listOfParameters, 100, onChange_02),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_01);
            
            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_02, listOfParameters, 100, onChange_03),
                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_02);

            this.userSi.waitTransferData(10, 1000);
            
            ProcedureInstanceIdentifier piids[] = new ProcedureInstanceIdentifier[] { this.piid_ocr_prime,
                                                                                      this.piid_ocr_secondary_01,
                                                                                      this.piid_ocr_secondary_02 };

            System.out.println("Check values at user for all started procedures");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                System.out.println("piid: " + piid);
                Map<Long, Map<Label, ICstsValue>> userValues = this.userSi.getParameterValues(piid, getFunctionalResource());
                values0 = userValues.get(Long.valueOf(0));
                values1 = userValues.get(Long.valueOf(1));
                index = 0;
                for (TestParameter testParameter : testParameters)
                {
                    Label label = labels[index++];
                    assertTrue("user(instNum=0): value not found: " + label, values0.containsKey(label));
                    assertTrue("user(instNum=1): value not found: " + label, values1.containsKey(label));
                    Map<Long, ICstsValue> values = new HashMap<>();
                    values.put(Long.valueOf(0), values0.get(label));
                    values.put(Long.valueOf(1), values1.get(label));
                    TestUtils.verifyLabelValues("user", testParameter.initValue, values, label);
                }
            }

            this.userSi.waitTransferData(5, 500);

            // verify that all user's procedures received all parameters updates for the first time no matter the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
                       2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));

            // reset procedure's parameter counters
            this.userSi.resetParameterUpdateCount(this.piid_ocr_prime);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_01);
            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_02);

            System.out.println("Update just subset of parameters at provider");
            int nParams = 0; // number of changed parameters
            for (index = 0; index < testParameters.size(); index++)
            {
                if (index % 4 != 0) continue; // change every fourth parameter
                this.providerSi.setParameterValue(labels[index], testParameters.get(index).updatedValue);
                nParams++;
            }

            this.userSi.waitTransferData(10, 1000);

            System.out.println("Check updated values at user for all started procedures");
            for (ProcedureInstanceIdentifier piid : piids)
            {
                System.out.println("piid: " + piid);
                Map<Long, Map<Label, ICstsValue>> userValues = this.userSi.getParameterValues(piid, getFunctionalResource());
                values0 = userValues.get(Long.valueOf(0));
                values1 = userValues.get(Long.valueOf(1));
                for (index = 0; index < testParameters.size(); index++)
                {
                    if (index % 4 != 0) continue;
                    Label label = labels[index];
                    assertTrue("user(instNum=0): value not found: " + label, values0.containsKey(label));
                    assertTrue("user(instNum=1): value not found: " + label, values1.containsKey(label));
                    Map<Long, ICstsValue> values = new HashMap<>();
                    values.put(Long.valueOf(0), values0.get(label));
                    values.put(Long.valueOf(1), values1.get(label));
                    TestUtils.verifyLabelValues("user", testParameters.get(index).updatedValue, values, label);
                }
            }

            this.userSi.waitTransferData(5, 500);

            // verify that user's procedures received expected parameters updates depending on the onChange value
            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
                         2*nParams, this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
                         2*nParams, this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
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

//    /**
//     * Test cyclic report procedure and its GET operation w/ PROCEDURE_INSTANCE_IDENTIFIER
//     */
//    @Test
//    public void testCyclicReportWithProcedureInstanceIdentifier()
//    {
//        try
//        {
//            // set FRs of instances 0 and 1
//            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());
//
//            ProcedureInstanceIdentifier piid = this.piid_ocr_prime;
//            
//            System.out.println("Create functional resource names for instances 0 and 1 and set parameters in provider");
//            Name[] names = new Name[testParameters.size()];
//            int index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = Name.of(testParameter.oid, piid);
//                names[index++] = name;
//                this.providerSi.setParameterValue(name, testParameter.initValue);
//            }
//            
//            System.out.println("Check values at provider for both instances");
//            Map<Name, ICstsValue> providerValues = this.providerSi.getParameterValues(piid);
//
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = names[index++];
//                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues.get(name), name);
//            }
//
//            System.out.println("BIND...");
//            TestUtils.verifyResult(this.userSi.bind(), "BIND");
//
//            System.out.println("Query parameters for both instances");
//            ListOfParameters listOfParameters = ListOfParameters.of(piid);
//
//            System.out.println("START-CYCLIC-REPORT...");
//            TestUtils.verifyResult(this.userSi.startCyclicReport(piid, listOfParameters, 100),
//                    "START-CYCLIC-REPORT");
//
//            userSi.waitTransferData(5, 500);
//
//            System.out.println("Check values at user for instance 0 and 1");
//            Map<Name, ICstsValue> userValues = this.userSi.getParameterValues(piid);
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = names[index++];
//                TestUtils.verifyNameValue("user", testParameter.initValue, userValues.get(name), name);
//            }
//
//            System.out.println("Change values at provider");
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                this.providerSi.setParameterValue(names[index++], testParameter.updatedValue);
//            }
//
//            userSi.waitTransferData(5, 500);
//
//            System.out.println("Check updated values at user for both instances");
//            userValues = this.userSi.getParameterValues(piid);
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = names[index++];
//                TestUtils.verifyNameValue("user", testParameter.updatedValue, userValues.get(name), name);
//            }
//
//            System.out.println("STOP-CYCLIC-REPORT...");
//            TestUtils.verifyResult(userSi.stopCyclicReport(piid), "STOP-CYCLIC-REPORT");
//
//            System.out.println("UNBIND...");
//            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");
//
//            this.providerSi.destroy();
//            this.userSi.destroy();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * Test on change cyclic report procedure and its GET operation w/ PROCEDURE_INSTANCE_IDENTIFIER
//     */
//    @Test
//    public void testOnChangeCyclicReportWithProcedureInstanceIdentifier()
//    {
//        try
//        {
//            // set FRs of instances 0 and 1
//            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());
//
//            System.out.println("Create functional resource names for instances 0 and 1 and set parameters in provider");
//            Name[] names = new Name[2*testParameters.size()];
//            int index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name0 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
//                names[index++] = name0;
//                this.providerSi.setParameterValue(name0, testParameter.initValue);
//
//                Name name1 = Name.of(testParameter.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
//                names[index++] = name1;
//                this.providerSi.setParameterValue(name1, testParameter.initValue);
//            }
//            
//            System.out.println("Check values at provider for both instances");
//            Map<Name, ICstsValue> providerValues = this.providerSi.getParameterValues(this.piid_ocr_prime);
//
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = names[index++]; // instNum=0
//                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues.get(name), name);
//                name = names[index++]; // instNum=1
//                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues.get(name), name);
//            }
//
//            System.out.println("BIND...");
//            TestUtils.verifyResult(this.userSi.bind(), "BIND");
//
//            System.out.println("Query parameters for both instances");
//            ListOfParameters listOfParameters0 = ListOfParameters.of(this.piid_ocr_prime);
//            ListOfParameters listOfParameters1 = ListOfParameters.of(this.piid_ocr_secondary_01);
//            ListOfParameters listOfParameters2 = ListOfParameters.of(this.piid_ocr_secondary_02);
//
//            boolean onChange_01 = true;
//            boolean onChange_02 = true;
//            boolean onChange_03 = false;
//
//            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
//            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, listOfParameters0, 100, onChange_01),
//                                   "START-CYCLIC-REPORT " + this.piid_ocr_prime);
//
//            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
//            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_01, listOfParameters1, 100, onChange_02),
//                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_01);
//
//            System.out.println("START-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
//            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_secondary_02, listOfParameters2, 100, onChange_03),
//                                   "START-CYCLIC-REPORT " + this.piid_ocr_secondary_02);
//
//            userSi.waitTransferData(5, 500);
//
//            ProcedureInstanceIdentifier piids[] = new ProcedureInstanceIdentifier[] { this.piid_ocr_prime,
//                                                                                      this.piid_ocr_secondary_01,
//                                                                                      this.piid_ocr_secondary_02 };
//
//            System.out.println("check values at user for all started procedures");
//            for (ProcedureInstanceIdentifier piid : piids)
//            {
//                System.out.println("piid: " + piid);
//                Map<Name, ICstsValue> userValues0 = this.userSi.getParameterValues(piid);
//                for (index = 0; index < testParameters.size(); index++)
//                {
//                    Name name = names[index];
//                    TestUtils.verifyNameValue("provider", testParameters.get(index).initValue, userValues0.get(name), name);
//                }
//            }
//
//            userSi.waitTransferData(5, 500);
//
//            // verify that all user's procedures received all parameters updates for the first time no matter the onChange value
//            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
//                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
//            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
//                         2*testParameters.size(), this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
//            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
//                       2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));
//
//            // reset procedure's parameter counters
//            this.userSi.resetParameterUpdateCount(this.piid_ocr_prime);
//            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_01);
//            this.userSi.resetParameterUpdateCount(this.piid_ocr_secondary_02);
//
//            System.out.println("Update just subset of parameters at provider");
//            int nParams0 = 0;
//            int nParams1 = 0;
//            for (index = 0; index < testParameters.size(); index++)
//            {
//                if (index % 4 != 0) // change every fourth parameter
//                {
//                    this.providerSi.setParameterValue(names[index], testParameters.get(index).updatedValue);
//                    nParams0++;
//                }
//                if (index % 3 != 0) // change every third parameter
//                {
//                    this.providerSi.setParameterValue(names[index], testParameters.get(index).updatedValue);
//                    nParams1++;
//                }
//                this.providerSi.setParameterValue(names[index], testParameters.get(index).updatedValue);
//            }
//
//            userSi.waitTransferData(5, 500);
//
//            System.out.println("Check values at user for all started procedures");
//            System.out.println("piid: " + piids[0]);
//            Map<Name, ICstsValue> userValues0 = this.userSi.getParameterValues(piids[0]);
//            for (index = 0; index < testParameters.size(); index+=4) // check every fourth parameter
//            {
//                TestUtils.verifyNameValue("user", testParameters.get(index).updatedValue, userValues0.get(names[index]), names[index]);
//            }
//            System.out.println("piid: " + piids[1]);
//            Map<Name, ICstsValue> userValues1 = this.userSi.getParameterValues(piids[1]);
//            for (index = 0; index < testParameters.size(); index+=3) // check every third parameter
//            {
//                TestUtils.verifyNameValue("user", testParameters.get(index).updatedValue, userValues1.get(names[index]), names[index]);
//            }
//            System.out.println("piid: " + piids[2]);
//            Map<Name, ICstsValue> userValues2 = this.userSi.getParameterValues(piids[2]);
//            for (index = 0; index < testParameters.size(); index++) // check every parameter
//            {
//                TestUtils.verifyNameValue("user", testParameters.get(index).updatedValue, userValues2.get(names[index]), names[index]);
//            }
//
//            userSi.waitTransferData(5, 500); (onChange=false)
//
//            // verify that user's procedures received expected parameters updates depending on the onChange value
//            assertEquals("procedure " + this.piid_ocr_prime + " started w/ onChange=true received more parameters than expected",
//                         nParams0, this.userSi.getParameterUpdateCount(this.piid_ocr_prime));
//            assertEquals("procedure " + this.piid_ocr_secondary_01 + " started w/ onChange=true received more parameters than expected",
//                         nParams1, this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_01));
//            assertTrue("procedure " + this.piid_ocr_secondary_02 + " started w/ onChange=true received more parameters than expected",
//                         2*testParameters.size() < this.userSi.getParameterUpdateCount(this.piid_ocr_secondary_02));
//
//            System.out.println("STOP-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
//            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_prime),
//                                   "STOP-CYCLIC-REPORT " + this.piid_ocr_prime + "...");
//            
//            System.out.println("STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
//            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_secondary_01),
//                                   "STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_01 + "...");
//
//            System.out.println("STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
//            TestUtils.verifyResult(userSi.stopCyclicReport(this.piid_ocr_secondary_02),
//                                   "STOP-CYCLIC-REPORT " + this.piid_ocr_secondary_02 + "...");
//
//            System.out.println("UNBIND...");
//            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");
//
//            this.providerSi.destroy();
//            this.userSi.destroy();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//    /**
//     * Test cyclic report procedure and its GET operation w/ PROCEDURE_TYPE
//     */
//    @Test
//    public void testCyclicReportWithProcedureType()
//    {
//        try
//        {
//            // set FRs of instances 0 and 1
//            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());
//
//            ProcedureType procTyp = ProcedureType.of(OIDs.ocoCyclicReport);
//            
//            System.out.println("Create functional resource names for instances 0 and 1 and set parameters in provider");
//            Label[] labels = new Label[testParameters.size()];
//            int index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Label label = Label.of(testParameter.oid, procTyp);
//                labels[index++] = label;
//                this.providerSi.setParameterValue(label, testParameter.initValue);
//            }
//            
//            System.out.println("Check values at provider for both instances");
//            Map<esa.egos.csts.api.enumerations.ProcedureRole, Map<Long, Map<Label, ICstsValue>>> providerValues = this.providerSi.getParameterValues(procTyp);
//
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Label label = labels[index++];
//                TestUtils.verifyNameValue("provider", testParameter.initValue, providerValues.get(name), name);
//            }
//
//            System.out.println("BIND...");
//            TestUtils.verifyResult(this.userSi.bind(), "BIND");
//
//            System.out.println("Query parameters for both instances");
//            ListOfParameters listOfParameters = ListOfParameters.of(piid);
//
//            System.out.println("START-CYCLIC-REPORT...");
//            TestUtils.verifyResult(this.userSi.startCyclicReport(piid, listOfParameters, 100),
//                    "START-CYCLIC-REPORT");
//
//            userSi.waitTransferData(5, 500);
//
//            System.out.println("Check values at user for instance 0 and 1");
//            Map<Name, ICstsValue> userValues = this.userSi.getParameterValues(piid);
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = names[index++];
//                TestUtils.verifyNameValue("user", testParameter.initValue, userValues.get(name), name);
//            }
//
//            System.out.println("Change values at provider");
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                this.providerSi.setParameterValue(names[index++], testParameter.updatedValue);
//            }
//
//            userSi.waitTransferData(5, 500);
//
//            System.out.println("Check updated values at user for both instances");
//            userValues = this.userSi.getParameterValues(piid);
//            index = 0;
//            for (TestParameter testParameter : testParameters)
//            {
//                Name name = names[index++];
//                TestUtils.verifyNameValue("user", testParameter.updatedValue, userValues.get(name), name);
//            }
//
//            System.out.println("STOP-CYCLIC-REPORT...");
//            TestUtils.verifyResult(userSi.stopCyclicReport(piid), "STOP-CYCLIC-REPORT");
//
//            System.out.println("UNBIND...");
//            TestUtils.verifyResult(this.userSi.unbind(), "UNBIND");
//
//            this.providerSi.destroy();
//            this.userSi.destroy();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }

    /**
     * Test cyclic report procedure and its GET operation w/ EMPTY
     */
    @Test
    public void testCyclicReportWithEmpty()
    {
        try
        {
            // set empty default label list
            providerSi.setDefaultLabelList(this.piid_ocr_prime, null);

            // set FRs of both instances 0 and 1
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
                Map<Long, ICstsValue> providerValues = this.providerSi.getParameterValues(labels.get(index));
                assertTrue("provider: didn't get 2 values for " + testParameter.name, providerValues.size() == 2);
                TestUtils.verifyLabelValues("provider", testParameter.initValue, providerValues, labels.get(index));
                index++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(userSi.bind(), "BIND");

            System.out.println("again START-CYCLIC-REPORT...");
            TestUtils.verifyResult(this.userSi.startCyclicReport(this.piid_ocr_prime, ListOfParameters.empty(), 100),
                    "START-CYCLIC-REPORT");
            
            this.userSi.waitTransferData(10, 1000);

            // check values at user
            System.out.println("Check values at user");
            index = 0;
            for (TestParameter testParameter : testParameters)
            {
                Map<Long, ICstsValue> userValues = this.userSi.getParameterValues(labels.get(index));
                assertTrue("user: didn't get 2 values for " + testParameter.name, userValues.size() == 2);
                TestUtils.verifyLabelValues("user", testParameter.initValue, userValues, labels.get(index));
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

}
