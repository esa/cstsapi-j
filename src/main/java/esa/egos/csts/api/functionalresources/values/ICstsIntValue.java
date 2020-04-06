package esa.egos.csts.api.functionalresources.values;

import java.math.BigInteger;

public interface ICstsIntValue extends ICstsSimpleValue<BigInteger>
{
    long getLongValue();
}
