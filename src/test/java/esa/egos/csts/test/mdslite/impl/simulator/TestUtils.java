package esa.egos.csts.test.mdslite.impl.simulator;

import org.junit.Assert;

import esa.egos.csts.api.enumerations.CstsResult;

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
            System.out.println(what + res + " OK ");
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
            System.out.println(what + " OK " + res);
        }
    }
}
