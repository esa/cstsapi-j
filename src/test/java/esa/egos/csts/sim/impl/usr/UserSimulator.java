package esa.egos.csts.sim.impl.usr;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import esa.egos.csts.sicf.SicfReader;
import esa.egos.csts.sicf.model.MDSicf;
import esa.egos.csts.sicf.model.ServiceInstances.ServiceInstance;

public class UserSimulator {
	private static CstsUserManager cstsUserManager;
	private static final String SICF = "src/test/resources/client/SICF-CEB.Example.xml";
	private static final String USER_CONFIG_1 = "src/test/resources/client/UserConfig.xml";
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cstsUserManager = new CstsUserManager("CSTS_USER", USER_CONFIG_1);
		cstsUserManager.init();

		HashMap<ServiceInstance, String> serviceInstances = getFirstServiceInstance(SICF);
		MdCstsSiUser siUser = createSIUser(serviceInstances);
		siUser.bind();
		siUser.startDefaultCyclicReport();
	}

	@Test
	public void test() throws InterruptedException {
		while (true) {
			Thread.sleep(10000);
		}
	}

	private static HashMap<ServiceInstance, String> getFirstServiceInstance(String sicfPath) {
		HashMap<ServiceInstance, String> serviceInstances = new HashMap<ServiceInstance, String>();
		MDSicf sicf = SicfReader.createSicf(sicfPath);
		ServiceInstance serviceInstance = sicf.getServiceInstances().getServiceInstance().get(0);
		serviceInstances.put(serviceInstance, SICF);
		return serviceInstances;
	}

	private static MdCstsSiUser createSIUser(Map<ServiceInstance, String> serviceInstances) {
		ServiceInstance si = serviceInstances.keySet().stream().findFirst().get();
		return cstsUserManager.createSIUser(si, SICF);
	}	
}
