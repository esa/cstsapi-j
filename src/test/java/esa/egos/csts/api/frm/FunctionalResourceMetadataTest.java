package esa.egos.csts.api.frm;

import java.util.Arrays;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.beanit.jasn1.ber.types.BerObjectIdentifier;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.sim.impl.frm.FunctionalResourceMetadata;
import frm.csts.functional.resource.types.OidValues;

public class FunctionalResourceMetadataTest {

	@Rule
	public TestRule testWatcher = new CstsTestWatcher();


	@Test
	public void testFunctionalResourceMetadataLoad() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		FunctionalResourceMetadata.getInstance().load("frm.csts.functional.resource.types");
	}

	@Test
	public void testCollectOids() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		Map<String, int[]> oids = FunctionalResourceMetadata.collectOids(OidValues.class, BerObjectIdentifier.class);
		Map<String, int[]> frOids = FunctionalResourceMetadata.findCrossSupportFunctionalities(oids);
		frOids.entrySet().stream().forEach(e-> System.out.println(e.getKey() + ", " + Arrays.toString(e.getValue())));
		System.out.println(frOids.size());
		System.out.println(frOids);
	}

	@Test
	public void testRemoveSuffix()
	{
		System.out.println(FunctionalResourceMetadata.removeSuffix("antActualAzimuthType", "Type"));
	}

}
