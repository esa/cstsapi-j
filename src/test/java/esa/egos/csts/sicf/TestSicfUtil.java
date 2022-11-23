package esa.egos.csts.sicf;

import org.junit.Ignore;
import org.junit.Test;

import esa.egos.csts.sicf.model.MDSicf;
import esa.egos.csts.sicf.model.ParameterDefinitions;
import esa.egos.csts.sicf.model.ParameterDefinitions.Parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

public class TestSicfUtil {

     @Test
     public void test_createSTCParamName_whenNoSubsystem() {
          Parameter parameter = new Parameter();
          parameter.setDescription("any");
          parameter.setFunctionalResourceType("/SYS");
          parameter.setFunctionalResourceInstance(0);
          parameter.setParamOrEventId("1.3.112.4.4.2.1.5000");
          String name = SicfUtil.createSTCParamName(parameter);
          assertEquals("/SYS/1.3.112.4.4.2.1.5000", name);
     }

     @Test
     public void test_createSTCParamName_whenSubsystem() {
          Parameter parameter = new Parameter();
          parameter.setDescription("any");
          parameter.setFunctionalResourceType("/SYS/SUBS/OTHER");
          parameter.setFunctionalResourceInstance(1);
          parameter.setParamOrEventId("1.3.112.4.4.2.1.5000");
          String name = SicfUtil.createSTCParamName(parameter);
          assertEquals("/SYS1/SUBS/OTHER/1.3.112.4.4.2.1.5000", name);
     }

     @Test
     public void test_createSTCParamName_fromXml() {
          MDSicf sicfObject = SicfReader.createSicf("src/test/resources/sicf_test_configurations.xml");
          List<ParameterDefinitions.Parameter> parameterList = sicfObject.getParameterDefinitions().getParameter();
          List<String> expectedStcParamNames = Arrays.asList("/COR1/TST1/1", "/SYS/SUBS/OTHER/2", "/SYS2/SUBS/OTHER/3",
                    "/SYS42/SUBS6/OTHER5/OTHER_OTHER/4");

          for (int currentListIndex = 0; currentListIndex < parameterList.size(); currentListIndex++) {
               String stcParamName = SicfUtil.createSTCParamName(parameterList.get(currentListIndex));
               assertEquals(expectedStcParamNames.get(currentListIndex), stcParamName);
          }

     }

     @Test
     public void test_createSicfParameter_withSanaOidPrefix() {
          try {
               String oidStringSana = "1.3.112.4.4.2.1.1000.1.1.1";
               String expectedOidPrefixSana = "1.3.112.4.4.2.1.";

               SicfParameter sicfParameterTest = new SicfParameter("name", oidStringSana, 1, "description", "INTEGER");

               String oidPrefix = sicfParameterTest.getOidPrefix();

               assertEquals(expectedOidPrefixSana, oidPrefix);
          } catch (Exception e) {
               fail(e.getMessage());
          }
     }

     @Test
     public void test_createSicfParameter_withAgencyOidPrefix() {
          try {
               String oidStringAgency = "1.3.112.4.4.2.2.1000.1.1.1";
               String expectedOidPrefixAgency = "1.3.112.4.4.2.2.";

               SicfParameter sicfParameterTest = new SicfParameter("name", oidStringAgency, 1, "description",
                         "INTEGER");

               String oidPrefix = sicfParameterTest.getOidPrefix();

               assertEquals(expectedOidPrefixAgency, oidPrefix);
          } catch (Exception e) {
               fail(e.getMessage());
          }
     }

     @Test
     public void test_createSicfParameter_withWrongOidPrefix() {
          try {
               String oidStringError = "1.0.000.0.0.0.0.1000.1.1.1";

               SicfParameter sicfParameterTest = new SicfParameter("name", oidStringError, 1, "description", "INTEGER");
          } catch (Exception e) {
               assertTrue(e.getMessage().contains("OID string does not contain a valid prefix"));
          }
     }

}
