package esa.egos.csts.api.events.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.ICstsValueFactory;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Name;

/**
 * Implements the functional resource event
 *
 * @param <T> The jASN.1 BER encoding/decoding class
 * @param <V> The FR value container
 */
public class FunctionalResourceEvent<T extends BerType, V extends FunctionalResourceValue<T>> extends Event
{
    /** The logger */
    private static final Logger LOG = Logger.getLogger(FunctionalResourceEvent.class.getName());

    /** FR event value */
    protected V value;


    /**
     * Constructor
     * 
     * @param identifier The event object identifier
     * @param functionalResourceName The functional resource name
     * @param berClass The jASN.1 generate class for the event value
     * @param valueClass FR value class
     * @param valueFactories The external value factories
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public FunctionalResourceEvent(ObjectIdentifier identifier,
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
     * Create the value for this event
     * 
     * @param name The event name
     * @param berClass The jASN.1 generate class for the event value
     * @param valueClass FR value class
     * @param valueFactories The external value factories
     * @return FR event value
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
    public synchronized void setValue(EventValue eventValue)
    {
        super.setValue(eventValue);
        try
        {
            this.value.setValue(this.getName(), eventValue);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Failed to set " + eventValue, e);
        }
    }

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
    }

    public synchronized ICstsValue getCstsValue() throws IllegalArgumentException, IllegalAccessException
    {
        return this.value.getCstsValue();
    }

    @Override
    public void fire()
    {
        // convert CSTS value to the EventValue and set it to the base Event
        // class
        setValue(this.value.getEventValue());
        super.fire();
    }

    @Override
    public void fire(EventValue eventValue)
    {
        try
        {
            this.value.setValue(this.getName(), eventValue);
            super.fire(eventValue);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Failed to fire " + eventValue, e);
        }
    }

    public void fire(ICstsValue cstsValue)
    {
        try
        {
            this.value.setCstsValue(cstsValue);
            fire();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Failed to fire " + cstsValue, e);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FunctionalResourceEvent [");
        sb.append(super.toString());
        sb.append(", value=");
        sb.append(this.value.toString());
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
