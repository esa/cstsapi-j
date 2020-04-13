package esa.egos.csts.api.parameters.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.functionalresources.values.impl.CstsBoolValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsComplexValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsEmptyValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsIntValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOctetStringValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsOidValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsRealValue;
import esa.egos.csts.api.functionalresources.values.impl.CstsStringValue;
import esa.egos.csts.api.oids.ObjectIdentifier;

/**
 * test equals() and hashCode() methods for CstsValues's types
 */
public class CstsValuesTest
{

    @Test
    public void testEqualsHashCode()
    {
        // CstsIntValue
        System.out.println("Checking CstsIntValue");
        CstsIntValue intValue = CstsIntValue.of(123L);
        CstsIntValue eqIntValue1 = CstsIntValue.of("value", 123L);
        CstsIntValue neqIntValue1 = CstsIntValue.of("my_string", 123L);
        CstsIntValue eqIntValue2 = CstsIntValue.of("value", new BigInteger("123"));
        CstsIntValue neqIntValue2 = CstsIntValue.of("value", BigInteger.TEN);
        assertTrue(intValue.equals(eqIntValue1));
        assertFalse(intValue.equals(neqIntValue1));
        assertTrue(intValue.hashCode() == eqIntValue1.hashCode());
        assertFalse(intValue.hashCode() == neqIntValue1.hashCode());
        assertTrue(intValue.equals(eqIntValue2));
        assertFalse(intValue.equals(neqIntValue2));
        assertTrue(intValue.hashCode() == eqIntValue2.hashCode());
        assertFalse(intValue.hashCode() == neqIntValue2.hashCode());
        System.out.println(eqIntValue2.toString());

        // CstsBoolValue
        System.out.println("Checking CstsBoolValue");
        CstsBoolValue boolValue = CstsBoolValue.of(true);
        CstsBoolValue eqBoolValue = CstsBoolValue.of("value", true);
        CstsBoolValue neqBoolValue = CstsBoolValue.of("value", false);
        assertTrue(boolValue.equals(eqBoolValue));
        assertFalse(boolValue.equals(neqBoolValue));
        assertTrue(boolValue.hashCode() == eqBoolValue.hashCode());
        assertFalse(boolValue.hashCode() == neqBoolValue.hashCode());
        System.out.println(eqBoolValue.toString());
        
        // CstsEmptyValue
        System.out.println("Checking CstsEmptyValue");
        CstsEmptyValue emptyValue = CstsEmptyValue.empty();
        CstsEmptyValue eqEmptyValue = CstsEmptyValue.empty();
        assertTrue(emptyValue.equals(eqEmptyValue));
        assertTrue(emptyValue.hashCode() == eqEmptyValue.hashCode());
        System.out.println(emptyValue);
        
        // CstsComplexValue
        System.out.println("Checking CstsComplexValue");
        CstsComplexValue complexValue = CstsComplexValue.of(intValue, boolValue, emptyValue);
        CstsComplexValue eqComplexValue = CstsComplexValue.of("value", ParameterQualifier.VALID, intValue, boolValue, emptyValue);
        CstsComplexValue neqComplexValue = CstsComplexValue.of(boolValue, intValue, emptyValue);
        assertTrue(complexValue.equals(eqComplexValue));
        assertFalse(complexValue.equals(neqComplexValue));
        assertTrue(complexValue.hashCode() == eqComplexValue.hashCode());
        assertFalse(complexValue.hashCode() == neqComplexValue.hashCode());
        System.out.println(complexValue);
        
        // CstsOctetStringValue
        System.out.println("Checking CstsOctetStringValue");
        CstsOctetStringValue octetStringValue = CstsOctetStringValue.of(new byte[] {1,2,3,4,5});
        CstsOctetStringValue eqOctetStringValue = CstsOctetStringValue.of("value", new byte[] {1,2,3,4,5});
        CstsOctetStringValue neqOctetStringValue = CstsOctetStringValue.of(new byte[] {});
        assertTrue(octetStringValue.equals(eqOctetStringValue));
        assertFalse(octetStringValue.equals(neqOctetStringValue));
        assertTrue(octetStringValue.hashCode() == eqOctetStringValue.hashCode());
        assertFalse(octetStringValue.hashCode() == neqOctetStringValue.hashCode());
        System.out.println(octetStringValue);
        
        // CstsOidValue
        System.out.println("Checking CstsOidValue");
        ObjectIdentifier oid = ObjectIdentifier.of(1,2,3,4);
        CstsOidValue oidValue = CstsOidValue.of(oid);
        CstsOidValue eqOidValue1 = CstsOidValue.of("value", oid);
        CstsOidValue neqOidValue1 = CstsOidValue.of("my_string", oid);
        CstsOidValue eqOidValue2 = CstsOidValue.of("value", new int[] {1,2,3,4});
        CstsOidValue neqOidValue2 = CstsOidValue.of("value", new int[] {1,2,3,5});
        assertTrue(oidValue.equals(eqOidValue1));
        assertFalse(oidValue.equals(neqOidValue1));
        assertTrue(oidValue.hashCode() == eqOidValue1.hashCode());
        assertFalse(oidValue.hashCode() == neqOidValue1.hashCode());
        assertTrue(oidValue.equals(eqOidValue2));
        assertFalse(oidValue.equals(neqOidValue2));
        assertTrue(oidValue.hashCode() == eqOidValue2.hashCode());
        assertFalse(oidValue.hashCode() == neqOidValue2.hashCode());
        System.out.println(eqOidValue2.toString());
        
        // CstsRealValue
        System.out.println("Checking CstsRealValue");
        CstsRealValue realValue = CstsRealValue.of(1.2);
        CstsRealValue eqRealValue = CstsRealValue.of("value", 1.2);
        CstsRealValue neqRealValue = CstsRealValue.of("value", 1.3);
        assertTrue(realValue.equals(eqRealValue));
        assertFalse(realValue.equals(neqRealValue));
        assertTrue(realValue.hashCode() == eqRealValue.hashCode());
        assertFalse(realValue.hashCode() == neqRealValue.hashCode());
        System.out.println(realValue.toString());
        
        // CstsStringValue
        System.out.println("Checking CstsStringValue");
        CstsStringValue stringValue1 = CstsStringValue.of(new byte[] {1,2,3,4,5});
        CstsStringValue eqStringValue1 = CstsStringValue.of("value", new byte[] {1,2,3,4,5});
        CstsStringValue neqStringValue1 = CstsStringValue.of(new byte[] {});
        CstsStringValue stringValue2 = CstsStringValue.of("abcd");
        CstsStringValue eqStringValue2 = CstsStringValue.of("value", "abcd");
        CstsStringValue neqStringValue2 = CstsStringValue.of("value", "");
        assertTrue(stringValue1.equals(eqStringValue1));
        assertFalse(stringValue1.equals(neqStringValue1));
        assertTrue(stringValue1.hashCode() == eqStringValue1.hashCode());
        assertFalse(stringValue1.hashCode() == neqStringValue1.hashCode());
        assertTrue(stringValue2.equals(eqStringValue2));
        assertFalse(stringValue2.equals(neqStringValue2));
        assertTrue(stringValue2.hashCode() == eqStringValue2.hashCode());
        assertFalse(stringValue2.hashCode() == neqStringValue2.hashCode());
        // special case
        assertTrue(neqStringValue1.equals(neqStringValue2));
        assertTrue(neqStringValue1.hashCode() == neqStringValue2.hashCode());
        System.out.println(stringValue1);
        
    }

}
