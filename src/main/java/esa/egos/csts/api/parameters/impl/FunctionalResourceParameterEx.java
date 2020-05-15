package esa.egos.csts.api.parameters.impl;

import java.io.IOException;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.ICstsValueFactory;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.oids.ObjectIdentifier;


public class FunctionalResourceParameterEx<T extends BerType> extends FunctionalResourceParameter
{
    /** FR parameter value */
    private FunctionalResourceValue<T> value;

    /**
     * Constructor
     * 
     * @param identifier The parameter object identifier
     * @param functionalResourceName The functional resource name
     * @param berClass The jASN.1 generate class for the parameter value
     * @param cstsValueFactory The external value factory
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public FunctionalResourceParameterEx(ObjectIdentifier identifier,
                                       FunctionalResourceName functionalResourceName,
                                       Class<T> berClass,
                                       ICstsValueFactory cstsValueFactory) throws InstantiationException, IllegalAccessException
    {
        super(identifier, functionalResourceName);
        this.value = new FunctionalResourceValue<>(getName(), berClass, cstsValueFactory);
    }

    /**
     * Set the value of a {@QualifiedParameter} instance to the FR parameter
     * 
     * @param qualifiedParameter The qualified parameter
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public synchronized void setValue(QualifiedParameter qualifiedParameter) throws InstantiationException,
                                                                             IllegalAccessException,
                                                                             IOException
    {
        this.value.setValue(qualifiedParameter);
    }

    @Override
    public synchronized QualifiedParameter toQualifiedParameter()
    {
        return this.value.getQualifiedParameterValue();
    }

    /**
     * Set the value of a {@CstsValue} instance to the FR parameter
     * 
     * @param value The value
     * @throws NoSuchFieldException In case the BER class field does not exist
     * @throws IllegalArgumentException In case the provided value is wrong
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public synchronized void setCstsValue(ICstsValue value) throws NoSuchFieldException,
                                                           IllegalArgumentException,
                                                           IllegalAccessException,
                                                           InstantiationException
    {
        this.value.setCstsValue(value);

        // indicate the change
        setChanged();
        notifyObservers();
        clearChanged();
    }

    public synchronized ICstsValue getCstsValue() throws IllegalArgumentException, IllegalAccessException
    {
        return this.value.getCstsValue();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FunctionalResourceParameterEx [");
        sb.append(super.toString());
        sb.append(", value=");
        sb.append(this.value);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Convert FR parameter value to string
     * @return String
     */
    public String valueToString()
    {
        return this.value.valueToString();
    }
}
