package esa.egos.csts.sim.impl.prv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.sim.impl.frm.FunctionalResourceIntegerParameter;

public class MdCollection
{
    /** FR parameters provided by this SI*/
    protected Map<Name, FunctionalResourceParameter> parameters;

    /** FR event provided by this SI*/
    protected Map<Name, Event> events;


    /**
     * C-tor
     */
    private MdCollection()
    {
        this.parameters = new LinkedHashMap<Name, FunctionalResourceParameter>();
        this.events = new LinkedHashMap<Name, Event>();
    }

    /**
     * Create MD collection w/ functional resource parameters and events for each provided {@FunctionalResourceType}.
     * In case several same {@FunctionalResourceType} provided then this methods creates parameters and events w/
     * different instance numbers
     * 
     * @param functionalResourceTypes The array of {@FunctionalResourceType}
     * @return MD collection
     * @throws Exception
     */
    public static MdCollection createCollection(FunctionalResourceType... functionalResourceTypes) throws Exception
    {
        // create an empty collection
        MdCollection ret = new MdCollection();

        // keep instance numbers in a map, they might be several FRs w/ the same type
        HashMap<FunctionalResourceType, Integer> frTypeInstanceNumbers = new HashMap<>();
        for (FunctionalResourceType frType : functionalResourceTypes)
        {
            Integer instanceNumber = frTypeInstanceNumbers.get(frType);
            if (instanceNumber == null)
            {
                // FR type for the first time
                instanceNumber = 0;
            }
            else
            {
                // FR type for the second or more time, increment its instance number
                instanceNumber++;
            }

            // create FR's parameters
            // e.g. for FR Antenna: antAccumulatedPrecipitation, antActualAzimuth and etc 
            List<FunctionalResourceParameterEx<?>> frParameters =
                    FunctionalResourceMetadata.getInstance().createParameters(frType, instanceNumber);

            // add FR's parameters to the MD collection
            for (FunctionalResourceParameter frParameter : frParameters)
            {
                ret.addParameter(frParameter);
            }

            // create FR's events
            // e.g. for FR Antenna: antEventTrackingRxLockStat, antWindSpeedCriticality and etc 
            List<FunctionalResourceEvent<?>> frEvents =
                    FunctionalResourceMetadata.getInstance().createEvents(frType, instanceNumber);

            // add FR's parameters to the MD collection
            for (Event frEvent : frEvents)
            {
                ret.addEvent(frEvent);
            }

            // keep FR instance number
            frTypeInstanceNumbers.put(frType, instanceNumber);
        }
        return ret;
    }

    /**
     * Create a simple MD collection
     * 
     * @return MD collection
     * @throws Exception
     */
    public static MdCollection createSimpleMdCollection() throws Exception
    {
        MdCollection ret = new MdCollection();

        // parameter
        ObjectIdentifier antActualAzimuthId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1000, 1, 3, 1});
        FunctionalResourceType antActualAzimuthType = FunctionalResourceType.of(antActualAzimuthId);
        FunctionalResourceName antActualAzimuthName = FunctionalResourceName.of(antActualAzimuthType, 1);
        FunctionalResourceIntegerParameter antActualAzimuth =
                new FunctionalResourceIntegerParameter(antActualAzimuthId, antActualAzimuthName);
        ret.addParameter(antActualAzimuth);

        ObjectIdentifier antMeanWindSpeedId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1000, 1, 18, 1});
        FunctionalResourceType antMeanWindSpeedType = FunctionalResourceType.of(antMeanWindSpeedId);
        FunctionalResourceName antMeanWindSpeedName = FunctionalResourceName.of(antMeanWindSpeedType, 1);
        FunctionalResourceIntegerParameter antMeanWindSpeed =
                new FunctionalResourceIntegerParameter(antMeanWindSpeedId, antMeanWindSpeedName);
        ret.addParameter(antMeanWindSpeed);

        // event
        ObjectIdentifier antWindSpeedWarningId = ObjectIdentifier.of(new int[] { 1, 3, 112, 4, 4, 2, 1, 1000, 2, 3, 1});
        FunctionalResourceType antWindSpeedWarningType = FunctionalResourceType.of(antWindSpeedWarningId);
        FunctionalResourceName antWindSpeedWarningName = FunctionalResourceName.of(antWindSpeedWarningType, 1);
        Event antWindSpeedWarning = new Event(antWindSpeedWarningId, antWindSpeedWarningName);
        ret.addEvent(antWindSpeedWarning);

        return ret;
    }

    /**
     * Add FR parameter to MD collection
     * 
     * @param parameter FR parameter
     * @throws Exception
     */
    public void addParameter(FunctionalResourceParameter parameter) throws Exception
    {
        synchronized (this.parameters)
        {
            if (this.parameters.containsKey(parameter.getName()))
            {
                throw new Exception("MD Collection already contains parameter " + parameter.getName());
            }

            this.parameters.put(parameter.getName(), parameter);
        }
    }

    /**
     * Add FR parameters to MD collection
     * 
     * @param parameters FR parameters
     * @throws Exception
     */
    public void addParameters(FunctionalResourceParameter... parameters) throws Exception
    {
        for (FunctionalResourceParameter parameter : parameters)
        {
            addParameter(parameter);
        }
    }

    /**
     * Add FR event to MD collection
     * 
     * @param event FR event
     * @throws Exception
     */
    public void addEvent(Event event) throws Exception
    {
        synchronized (this.events)
        {
            if (this.parameters.containsKey(event.getName()))
            {
                throw new Exception("MD Collection already contains event " + event.getName());
            }

            this.events.put(event.getName(), event);
        }
    }

    /**
     * Add FR events to MD collection
     * 
     * @param event FR events
     * @throws Exception
     */
    public void addEvents(Event... events) throws Exception
    {
        for (Event event : events)
        {
            addEvent(event);
        }
    }

    /**
     * Get FR parameters from MD collection
     * 
     * @return The FR parameters
     */
    public Collection<FunctionalResourceParameter> getParameters()
    {
        Collection<FunctionalResourceParameter> ret;

        synchronized (this.parameters)
        {
            ret = new ArrayList<FunctionalResourceParameter>(this.parameters.values());
        }

        return ret;
    }

    /**
     * Get FR event from MD collection
     * 
     * @return The FR events
     */
    public Collection<Event> getEvents()
    {
        Collection<Event> ret;

        synchronized (this.events)
        {
            ret = new ArrayList<Event>(this.events.values());
        }

        return ret;
    }

    /**
     * Get the name of the first parameter in MD collection
     * @return
     */
    public Name getFirstParameterName()
    {
        Name ret;
        synchronized (this.parameters)
        {
            FunctionalResourceParameter par = this.parameters.values().iterator().next();
            ret = par.getName();
        }

        return ret;
    }

    /**
     * Get the set of FR parameters names from MD collection
     * @return FR parameter names
     */
    public ListOfParameters getParameterNameSet()
    {
        ListOfParameters ret;
        synchronized (this.parameters)
        {
            ret = ListOfParameters.of(this.parameters.keySet().toArray(new Name[this.parameters.size()]));
        }

        return ret;
    }

    /**
     * Get the set of FR parameters labels from MD collection
     * @return FR parameter labels
     */
    public ListOfParameters getParameterLabelSet()
    {
        ListOfParameters ret;
        synchronized (this.parameters)
        {
            List<Label> labels = new ArrayList<Label>(this.parameters.size());
            this.parameters.values().forEach(par -> labels.add(par.getLabel()));
            ret = ListOfParameters.of(labels.toArray(new Label[labels.size()]));
        }

        return ret;
    }

    /**
     * Get the set of FR event names from MD collection
     * @return FR event names
     */
    public ListOfParameters getEventNames()
    {
        ListOfParameters ret;
        synchronized (this.events)
        {
            ret = ListOfParameters.of(this.events.keySet().toArray(new Name[this.events.size()]));
        }

        return ret;
    }

    public ListOfParameters getEventLabelSet()
    {
        ListOfParameters ret;
        synchronized (this.events)
        {
            List<Label> labels = new ArrayList<Label>(this.events.size());
            this.events.values().forEach(par -> labels.add(par.getLabel()));
            ret = ListOfParameters.of(labels.toArray(new Label[labels.size()]));
        }

        return ret;
    }

    /**
     * Fire all FR events in MD collection
     */
    public void fireAllEvents()
    {
        synchronized (this.events)
        {
            this.events.values().forEach(e -> e.fire());
        }
    }

    /**
     * Get the value of an FR parameter from MD collection
     * @param name FR parameter name
     * @return FR parameter value
     */
    public FunctionalResourceParameter getParameter(Name name)
    {
        FunctionalResourceParameter ret = null;

        synchronized (this.parameters)
        {
            ret = this.parameters.get(name);
        }

        return ret;
    }

    /**
     * Get the value of an FR parameter from MD collection
     * @param name FR parameter name
     * @return FR parameter value
     */
    public QualifiedParameter getQualifiedParameter(Name name)
    {
        QualifiedParameter ret = null;

        synchronized (this.parameters)
        {
            ret = this.parameters.get(name).toQualifiedParameter();
        }

        return ret;
    }

    /**
     * Get the value of the first FR parameter /w the label from MD collection
     * @param  FR parameter label
     * @return FR parameter value
     */
    public QualifiedParameter getFirstQualifiedParameter(Label label)
    {
        QualifiedParameter ret = null;

        synchronized (this.parameters)
        {
            Optional<FunctionalResourceParameter> result = this.parameters.values().stream()
                    .filter(p -> p.getLabel().equals(label)).findFirst();
            if (result.isPresent())
            {
                ret = result.get().toQualifiedParameter();
            }
        }

        return ret;
    }

    /**
     * Get the value of an FR parameter from MD collection
     * @param name FR parameter name
     * @return FR parameter value
     */
    public List<QualifiedParameter> getQualifiedParameters()
    {
        List<QualifiedParameter> ret = new ArrayList<QualifiedParameter>();

        synchronized (this.parameters)
        {
            for (FunctionalResourceParameter parameter : this.parameters.values())
            {
                ret.add(parameter.toQualifiedParameter());
            }
        }

        return ret;
    }

    /**
     * Update the value of an FR integer parameter in MD collection
     * @param name FR parameter name
     * @param value The new value
     */
    public void updateIntegerParameter(Name name, long value)
    {
        synchronized (this.parameters)
        {
            this.parameters.get(name).setValue(value);
        }
    }

    public void setParameterValue(Name name, ICstsValue value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        FunctionalResourceParameter parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }

        if (!(parameter instanceof FunctionalResourceParameterEx<?>))
        {
            throw new UnsupportedOperationException("Parameter " + name + " is not an instance of FunctionalResourceParameterEx<?>");
        }

        ((FunctionalResourceParameterEx<?>) parameter).setCstsValue(value);
    }

    public ICstsValue getParameterValue(Name name) throws IllegalArgumentException, IllegalAccessException
    {
        FunctionalResourceParameter parameter = getParameter(name);
        if (parameter == null)
        {
            throw new NullPointerException("Parameter " + name + " is not available in MD collection");
        }

        if (!(parameter instanceof FunctionalResourceParameterEx<?>))
        {
            throw new UnsupportedOperationException("Parameter " + name + " is not an instance of FunctionalResourceParameterEx<?>");
        }

        return ((FunctionalResourceParameterEx<?>) parameter).getCstsValue();
    }
}
