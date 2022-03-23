package esa.egos.csts.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.main.CstsApi;
import esa.egos.csts.api.main.CstsUserApi;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.SfwVersion;

public class TestCstsApi {
	
	@Test
	public void testSetupUser() throws ApiException {

		File file = new File("src/test/resources/UserConfig3.xml");
		String userConfigName = file.getAbsolutePath();
		
		CstsApi userApi = new CstsUserApi("Test Service User API");
		userApi.initialize(userConfigName);
		
		assertTrue(userApi.getFrameworkConfig().getVersion(SfwVersion.B1)
				.getServiceVersion(ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 2)).contains(1));
		
		assertTrue(userApi.getFrameworkConfig().getVersion(SfwVersion.B2)
				.getServiceVersion(ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 2)).contains(2));
		
	}
	
	
	@Test
	public void testGetFrameworkForServiceVersion()  throws ApiException {
		
		File file = new File("src/test/resources/UserConfig3.xml");
		String userConfigName = file.getAbsolutePath();
		
		CstsApi userApi = new CstsUserApi("Test Service User API");
		userApi.initialize(userConfigName);
		
		assertEquals(SfwVersion.B1, userApi.getFrameworkConfig().getFrameworkVersion(ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 2), 1));
		
		assertEquals(SfwVersion.B2, userApi.getFrameworkConfig().getFrameworkVersion(ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 2), 2));
		
	}

}
