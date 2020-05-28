package esa.egos.csts.api.procedures;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.TestUtils;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;

/**
 * Test CSTS API notification procedure w/ FR events
 */
public abstract class NotificationFrTestBase extends MdCstsTestBase
{

    protected abstract FunctionalResourceType getFunctionalResource();

    protected List<Label> createDefaultLabelList()
    {
        return MdCstsTestBase.testParameters.stream().map(tp -> Label.of(tp.oid, getFunctionalResource())).collect(Collectors.toList());
    }

    /**
     * Test the notification procedure and its START NOTIFY and STOP operations w/ NAME_SET
     */
    @Test
    public void testNotificationReportWithNameSet()
    {
        try
        {
            // set FRs of Fr 0 and Fr 1
            this.providerSi.setFunctionalResources(getFunctionalResource(), getFunctionalResource());

            // create array of names for Fr 0 and 1 and set parameters in provider
            System.out.println("Create names for Fr 0 and 1 and set parameters in provider");
            List<Name> names0 = new ArrayList<Name>(testEvents.size());
            List<Name> names1 = new ArrayList<Name>(testEvents.size());
            for (TestParameter testEvent : testEvents)
            {
                Name name0 = Name.of(testEvent.oid, FunctionalResourceName.of(getFunctionalResource(), 0));
                this.providerSi.setEventValue(name0, testEvent.initValue);
                names0.add(name0);

                Name name1 = Name.of(testEvent.oid, FunctionalResourceName.of(getFunctionalResource(), 1));
                this.providerSi.setEventValue(name1, testEvent.initValue);
                names1.add(name1);
            }

            // check values at provider
            System.out.println("Check values at provider");
            int index_01 = 0;
            for (TestParameter testEvent : testEvents)
            {
                ICstsValue cstsValue_01 = this.providerSi.getEventValue(names0.get(index_01));
                assertTrue("provider(Fr=0): found incorrect value of " + testEvent.name
                           + ", expected  " + testEvent.initValue + ", but was " + cstsValue_01,
                           cstsValue_01.equals(testEvent.initValue));

                ICstsValue cstsValue_02 = this.providerSi.getEventValue(names1.get(index_01));
                assertTrue("provider(Fr=1): found incorrect value of " + testEvent.name
                           + ", expected  " + testEvent.initValue + ", but was " + cstsValue_02,
                           cstsValue_02.equals(testEvent.initValue));
                index_01++;
            }

            System.out.println("BIND...");
            TestUtils.verifyResult(this.userSi.bind(), "BIND");

            // create list of requested event names
            List<Name> namesList = new ArrayList<Name>(2*testEvents.size());
            namesList.addAll(names0);
            namesList.addAll(names1);
            ListOfParameters listOfEvents = ListOfParameters.of(namesList.toArray(new Name[] {}));

            System.out.println("START-NOTIFICATION...");
            TestUtils.verifyResult(this.userSi.startNotification(this.piid_n_secondary, listOfEvents),
                                   "START-NOTIFICATION");

            // fire all events at provider /w initial values
            System.out.println("Fire all events at provider and verify their reception by user");
            int index_02 = 0;
            for (Name name : namesList)
            {
                this.providerSi.getEvent(name).fire();

                Thread.sleep(100);

                FunctionalResourceEvent<?> event_01 = null;
                try
                {
                    event_01 = this.userSi.getEvent(this.piid_n_secondary, name);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    fail("did not received a notification of " + name);
                }

                assertNotNull("did not received a notification of " + name, event_01);
                TestParameter testEvent = testEvents.get(index_02 % testEvents.size());

                ICstsValue cstsValue_03 = event_01.getCstsValue();
                assertTrue("found incorrect value of " + testEvent.name
                           + ", expected  " + testEvent.initValue + ", but was " + cstsValue_03,
                           cstsValue_03.equals(testEvent.initValue));
                index_02++;
            }

            // fire all events at provider /w updated values
            System.out.println("Fire all updated events at provider and verify their reception by user");
            int index_03 = 0;
            for (Name name : namesList)
            {
                this.providerSi.getEvent(name).fire(testEvents.get(index_03  % testEvents.size()).updatedValue);

                Thread.sleep(100);

                FunctionalResourceEvent<?> event_02 = null;
                try
                {
                    event_02 = this.userSi.getEvent(this.piid_n_secondary, name);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    fail("did not received a notification of " + name);
                }

                assertNotNull("did not received a notification of " + name, event_02);
                TestParameter testEvent = testEvents.get(index_03 % testEvents.size());

                ICstsValue cstsValue_04 = event_02.getCstsValue();
                assertTrue("found incorrect value of " + testEvent.name
                           + ", expected  " + testEvent.updatedValue + ", but was " + cstsValue_04,
                           cstsValue_04.equals(testEvent.updatedValue));
                index_03++;
            }

            System.out.println("STOP-NOTIFICATION...");
            TestUtils.verifyResult(this.userSi.stopNotification(this.piid_n_secondary), "STOP-NOTIFICATION");

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
