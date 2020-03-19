package esa.egos.csts.api.frm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.sim.impl.frm.FunctionalResourceMetadata;

public class FunctionalResourceMetadataTest {

	@Rule
	public TestRule testWatcher = new CstsTestWatcher();


	@Test
	public void testFunctionalResourceMetadataLoad() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		FunctionalResourceMetadata.getInstance().load("frm.csts.functional.resource.types");
	}

	@Test
	public void testRemoveSuffix()
	{
		System.out.println(FunctionalResourceMetadata.removeSuffix("antActualAzimuthType", "Type"));
	}

}
