package esa.egos.csts.api.functionalresources.values.impl;

import com.beanit.jasn1.ber.types.BerNull;

public class CstsNullValue extends CstsSimpleValue<BerNull>
{
    
    protected CstsNullValue(String name) {
        super(name);
        //value = new BerNull();
    }
    
    public static CstsNullValue of(String name) {
        return new CstsNullValue(name);
    }
    
    @Override
    public String toString() {
        return "CstsNullValue [name=" + getName() + ", quality=" + getQuality() + "]";
    }

}
