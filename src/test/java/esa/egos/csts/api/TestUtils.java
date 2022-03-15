package esa.egos.csts.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Assert;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

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
    
    public static void verifyResultsWithTimeout(CstsResult res, String what, long timeout) 
    {
    	long startTime = System.currentTimeMillis();
    	long currentTime = 0;
    	long timeElapsed = 0;
    	
    	boolean isTimeout = false;
    	boolean isTestStarted = false;
    	
    	while(!isTimeout) {
    		currentTime = System.currentTimeMillis();
    		timeElapsed = currentTime - startTime;
    		isTimeout = (timeElapsed > timeout)?  true : false;
    		
    		if (!isTestStarted) {
    			System.out.println("STARTED");
    			verifyResult(res, what);
    			isTestStarted = true;
    		}
    		   		
    		if (isTimeout) {
    			System.out.println("FAILING");
    			Assert.fail(what + " failed with timeout!");
    		}
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
	
	public static void verifyNameValue(String info, ICstsValue refVal, ICstsValue value, Name name)
	{
	    if (refVal.equals(value)) return;
	    System.out.println(info + ": found incorrect value of Name: " + name);
	    System.out.println("refVal: " + refVal);
	    System.out.println("value : " + value);
	    fail();
	}
	
	public static void verifyLabelValues(String info, ICstsValue refVal, Map<? extends Number, ICstsValue> values, Label label)
	{
	    for (Number instNum : values.keySet())
	    {
	        ICstsValue value = values.get(instNum);
            if (refVal.equals(value)) continue;
            System.out.println(info + "(" + instNum.longValue() + "): found incorrect value of Label: " + label);
            System.out.println("refVal: " + refVal);
            System.out.println("value : " + values.get(instNum));
            fail();
	    }
	}
	
}
