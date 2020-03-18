package esa.egos.csts.api.frm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.beanit.jasn1.ber.types.BerObjectIdentifier;

import esa.egos.csts.api.CstsTestWatcher;
import esa.egos.csts.api.oids.OidTree;
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
		System.out.println("number of OIDs: " + oids.size());
		assertFalse("no OIDs found", oids.isEmpty());

		Map<String, int[]> frOids = FunctionalResourceMetadata.findCrossSupportFunctionalities(oids);
		frOids.entrySet().stream().forEach(e-> System.out.println(e.getKey() + ", " + Arrays.toString(e.getValue())));
		System.out.println("number of FR OIDs: " + frOids.size());
		assertFalse("no FR OIDs found", oids.isEmpty());

		Map<String, int[]> frParameterOids = FunctionalResourceMetadata.findFunctionalResource(oids, OidTree.PARAM_BIT_VALUE);
		frParameterOids.entrySet().stream().forEach(e-> System.out.println(e.getKey() + ", " + Arrays.toString(e.getValue())));
		System.out.println("number of FR parameter OIDs: " + frParameterOids.size());
		assertFalse("no FR parameter OIDs found", frParameterOids.isEmpty());

		Map<String, int[]> frEventOids = FunctionalResourceMetadata.findFunctionalResource(oids, OidTree.EVENT_BIT_VALUE);
		frEventOids.entrySet().stream().forEach(e-> System.out.println(e.getKey() + ", " + Arrays.toString(e.getValue())));
		System.out.println("number of FR event OIDs: " + frEventOids.size());
		assertFalse("no FR event OIDs found", frEventOids.isEmpty());
	}

	@Test
	public void testRemoveSuffix()
	{
		System.out.println(FunctionalResourceMetadata.removeSuffix("antActualAzimuthType", "Type"));
	}

}
