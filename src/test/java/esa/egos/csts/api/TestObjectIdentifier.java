package esa.egos.csts.api;

import org.junit.Assert;
import org.junit.Test;

import esa.egos.csts.api.oids.ObjectIdentifier;


public class TestObjectIdentifier {

	@Test
	public void testOidFromString() {
		
		ObjectIdentifier oneTwoThreeFour = ObjectIdentifier.of("1.2.3.4");
		
		Assert.assertEquals(oneTwoThreeFour, ObjectIdentifier.of(1, 2, 3, 4)); // test against integer creator
	}
}
