package esa.egos.proxy.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import esa.egos.csts.api.serviceinstance.impl.ServiceType;


public class ProviderConfigurationTest {

	@Test
	public void testParsingProviderConfiguration() throws FileNotFoundException {
		File file = new File("src/test/resources/ProviderConfig1.xml");
		String providerConfigName = file.getAbsolutePath();
		
		FileInputStream configFileStream = new FileInputStream(new File(providerConfigName));
		
		ProviderConfig providerConfig = ProviderConfig.load(configFileStream);
		
		assertFalse(providerConfig.getServiceTypeList().isEmpty());
		
		assertEquals(2, providerConfig.getServiceTypeList().get(0).getServiceVersion().size());
		
		{
			long b1s1 = providerConfig.getServiceTypeList().stream()
					.filter(configServiceType -> configServiceType.getServiceId().equals("[1, 3, 112, 4, 4, 1, 2]"))
					.findAny().get().getServiceVersion().stream()
					.filter(serviceVersionType -> 
					serviceVersionType.sfwVersion.equals(Integer.valueOf(1)) && 
					serviceVersionType.value.equals(Integer.valueOf(1))
							).count();

			assertEquals(1, b1s1);
		}
		
		{
			long b2s2 = providerConfig.getServiceTypeList().stream()
					.filter(configServiceType -> configServiceType.getServiceId().equals("[1, 3, 112, 4, 4, 1, 2]"))
					.findAny().get().getServiceVersion().stream()
					.filter(serviceVersionType -> 
					serviceVersionType.sfwVersion.equals(Integer.valueOf(2)) && 
					serviceVersionType.value.equals(Integer.valueOf(2))
							).count();

			assertEquals(1, b2s2);
		}
		
		{
			long b1s2 = providerConfig.getServiceTypeList().stream()
					.filter(configServiceType -> configServiceType.getServiceId().equals("[1, 3, 112, 4, 4, 1, 2]"))
					.findAny().get().getServiceVersion().stream()
					.filter(serviceVersionType -> 
					serviceVersionType.sfwVersion.equals(Integer.valueOf(1)) && 
					serviceVersionType.value.equals(Integer.valueOf(2))
							).count();

			assertEquals(0, b1s2);
		}
		
		{
			long b2s1 = providerConfig.getServiceTypeList().stream()
					.filter(configServiceType -> configServiceType.getServiceId().equals("[1, 3, 112, 4, 4, 1, 2]"))
					.findAny().get().getServiceVersion().stream()
					.filter(serviceVersionType -> 
					serviceVersionType.sfwVersion.equals(Integer.valueOf(2)) && 
					serviceVersionType.value.equals(Integer.valueOf(1))
							).count();

			assertEquals(0, b2s1);
		}

	}

}
