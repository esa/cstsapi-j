package esa.egos.csts.sicf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestSicfUtil {
	
	@Test
	public void test_createSTCParamName_whenNoSubsystem() {
		esa.egos.csts.sicf.model.ParameterDefinitions.Parameter paramter =
				new esa.egos.csts.sicf.model.ParameterDefinitions.Parameter();
		paramter.setDescription("any");
		paramter.setFunctionalResourceType("/SYS");
		paramter.setFunctionalResourceInstance(0);
		paramter.setParamOrEventId("1.3.112.4.4.2.1.5000");
		String name = SicfUtil.createSTCParamName(paramter);
		assertEquals("/SYS/1.3.112.4.4.2.1.5000",name);
	}
	
	
	@Test
	public void test_createSTCParamName_whenSubsystem() {
		esa.egos.csts.sicf.model.ParameterDefinitions.Parameter paramter =
				new esa.egos.csts.sicf.model.ParameterDefinitions.Parameter();
		paramter.setDescription("any");
		paramter.setFunctionalResourceType("/SYS/SUBS/OTHER");
		paramter.setFunctionalResourceInstance(1);
		paramter.setParamOrEventId("1.3.112.4.4.2.1.5000");
		String name = SicfUtil.createSTCParamName(paramter);
		assertEquals("/SYS/1/SUBS/OTHER/1.3.112.4.4.2.1.5000",name);
	}

}
