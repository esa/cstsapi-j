package esa.egos.csts.sicf;

import org.junit.Test;

import esa.egos.csts.sicf.model.MDSicf;
import esa.egos.csts.sicf.model.ParameterDefinitions;
import esa.egos.csts.sicf.model.ParameterDefinitions.Parameter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;


public class TestSicfUtil {
	
	@Test
	public void test_createSTCParamName_whenNoSubsystem() {
		Parameter paramter = new Parameter();
		paramter.setDescription("any");
		paramter.setFunctionalResourceType("/SYS");
		paramter.setFunctionalResourceInstance(0);
		paramter.setParamOrEventId("1.3.112.4.4.2.1.5000");
		String name = SicfUtil.createSTCParamName(paramter);
		assertEquals("/SYS/1.3.112.4.4.2.1.5000",name);
	}
	
	
	@Test
	public void test_createSTCParamName_whenSubsystem() {
		Parameter paramter = new Parameter();
		paramter.setDescription("any");
		paramter.setFunctionalResourceType("/SYS/SUBS/OTHER");
		paramter.setFunctionalResourceInstance(1);
		paramter.setParamOrEventId("1.3.112.4.4.2.1.5000");
		String name = SicfUtil.createSTCParamName(paramter);
		assertEquals("/SYS1/SUBS/OTHER/1.3.112.4.4.2.1.5000",name);
	}
	
	@Test
	public void test_createSTCParamName_fromXml() {
	     MDSicf sicfObject = SicfReader.createSicf("src/test/resources/sicf_test_configurations.xml");
	     List<ParameterDefinitions.Parameter> parameterList = sicfObject.getParameterDefinitions().getParameter();
	     List<String> expectedStcParamNames = Arrays.asList(
	               "/COR1/TST1/1",
	               "/SYS/SUBS/OTHER/2",
	               "/SYS2/SUBS/OTHER/3",
	               "/SYS42/SUBS6/OTHER5/OTHER_OTHER/4"
	               );
	     
	     for(int currentListIndex = 0; currentListIndex<parameterList.size(); currentListIndex++) {
	          String stcParamName = SicfUtil.createSTCParamName(parameterList.get(currentListIndex));
	          assertEquals(expectedStcParamNames.get(currentListIndex), stcParamName);
	     }
	     
	}

}
