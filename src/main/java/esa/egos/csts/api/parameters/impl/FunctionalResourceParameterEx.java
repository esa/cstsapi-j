package esa.egos.csts.api.parameters.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.ICstsValueFactory;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.types.Name;

public class FunctionalResourceParameterEx<T extends BerType, V extends FunctionalResourceValue<T>>
                                          extends FunctionalResourceParameter
{
    /** FR parameter value */
    protected V value;


    /**
     * Constructor
     * 
     * @param identifier The parameter object identifier
     * @param functionalResourceName The functional resource name
     * @param berClass The jASN.1 generate class for the parameter value
     * @param valueClass FR value class
     * @param valueFactories The external value factories
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public FunctionalResourceParameterEx(ObjectIdentifier identifier,
                                         FunctionalResourceName functionalResourceName,
                                         Class<T> berClass,
                                         Class<V> valueClass,
                                         Object... valueFactories) throws InstantiationException,
                                                                   IllegalAccessException,
                                                                   NoSuchMethodException,
                                                                   SecurityException,
                                                                   IllegalArgumentException,
                                                                   InvocationTargetException
    {
        super(identifier, functionalResourceName);
        this.value = createValue(getName(), berClass, valueClass, valueFactories);
    }

    /**
     * Create the value for this parameter
     * 
     * @param name The event name
     * @param berClass The jASN.1 generate class for the event value
     * @param valueClass FR value class
     * @param valueFactories The external value factories
     * @return FR parameter value
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    protected V createValue(Name name,
                            Class<T> berClass,
                            Class<V> valueClass,
                            Object... valueFactories) throws InstantiationException,
                                                      IllegalAccessException,
                                                      NoSuchMethodException,
                                                      SecurityException,
                                                      IllegalArgumentException,
                                                      InvocationTargetException
    {
        Constructor<V> ctor = valueClass.getConstructor(Name.class, Class.class, ICstsValueFactory.class);
        ICstsValueFactory cstsValueFactory = null;
        for (Object obj : valueFactories)
        {
            if (obj instanceof ICstsValueFactory)
            {
                cstsValueFactory = (ICstsValueFactory) obj;
                break;
            }
        }
        if (cstsValueFactory == null)
        {
            throw new IllegalArgumentException("Missing an instance of ICstsValueFactory in the valueFactories argument");
        }
        return valueClass.cast(ctor.newInstance(name, berClass, cstsValueFactory));
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
    public void setCstsValue(ICstsValue value) throws NoSuchFieldException,
                                               IllegalArgumentException,
                                               IllegalAccessException,
                                               InstantiationException
    {
        synchronized (this)
        {
            this.value.setCstsValue(value);
        }

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
     * 
     * @return String
     */
    public String valueToString()
    {
        return this.value.valueToString();
    }
}
