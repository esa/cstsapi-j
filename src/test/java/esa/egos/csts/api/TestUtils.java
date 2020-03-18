package esa.egos.csts.api;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;

public class TestUtils
{
    public static void verifyResult(CstsResult res, String what)
    {
        Assert.assertEquals(CstsResult.SUCCESS, res);
        if (res != CstsResult.SUCCESS)
        {
            System.err.println("Failed to " + what + " " + res);
        }
        else
        {
            System.out.println(what + " " + res + " OK ");
        }
    }

    public static void verifyResult(CstsResult res, String what, CstsResult expectedRes)
    {
        Assert.assertEquals(expectedRes, res);
        if (res != expectedRes)
        {
            System.err.println("Failed to " + what + " " + res + " but expected was " + expectedRes);
        }
        else
        {
            System.out.println(what + " " + res + " OK ");
        }
    }


	public static void verifyEquals(QualifiedParameter expected, QualifiedParameter actual,
			String... prefixes) {

		String expectedPrefix = "expected";
		String actualPrefix = "actual";
		if (prefixes.length > 0 && prefixes[0] != null) {
			expectedPrefix = prefixes[0];
		}
		if (prefixes.length > 1 && prefixes[1] != null) {
			actualPrefix = prefixes[1];
		}

		if (expectedPrefix.length() > actualPrefix.length()) {
			actualPrefix = String.format("%1$-" + expectedPrefix.length() + "s", actualPrefix);
		}
		else {
			expectedPrefix = String.format("%1$-" + actualPrefix.length() + "s", expectedPrefix);
		}

		System.out.println(expectedPrefix + " qualified parameter = " + expected);
		System.out.println(actualPrefix + " qualified parameter = " + actual);
		assertEquals("qualified parameter " + expected.getName() + " is different", expected, actual);
	}
}
