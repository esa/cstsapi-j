package esa.egos.csts.sim.impl.prv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
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


    private MdCollection()
    {
        this.parameters = new LinkedHashMap<Name, FunctionalResourceParameter>();
        this.events = new LinkedHashMap<Name, Event>();
    }

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

    public void addParameters(FunctionalResourceParameter... parameters) throws Exception
    {
        for (FunctionalResourceParameter parameter : parameters)
        {
            addParameter(parameter);
        }
    }

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

    public void addEvents(Event... events) throws Exception
    {
        for (Event event : events)
        {
            addEvent(event);
        }
    }

    public Collection<FunctionalResourceParameter> getParameters()
    {
        Collection<FunctionalResourceParameter> ret;

        synchronized (this.parameters)
        {
            ret = new ArrayList<FunctionalResourceParameter>(this.parameters.values());
        }

        return ret;
    }

    public Collection<Event> getEvents()
    {
        Collection<Event> ret;

        synchronized (this.events)
        {
            ret = new ArrayList<Event>(this.events.values());
        }

        return ret;
    }

    public Name getParameterName()
    {
        Name ret;
        synchronized (this.parameters)
        {
            FunctionalResourceParameter par = this.parameters.values().iterator().next();
            ret = par.getName();
        }

        return ret;
    }

    public ListOfParameters getParameterNameSet()
    {
        ListOfParameters ret;
        synchronized (this.parameters)
        {
            ret = ListOfParameters.of(this.parameters.keySet().toArray(new Name[this.parameters.size()]));
        }

        return ret;
    }

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

    public ListOfParameters getEventNames()
    {
        ListOfParameters ret;
        synchronized (this.events)
        {
            ret = ListOfParameters.of(this.events.keySet().toArray(new Name[this.events.size()]));
        }

        return ret;
    }

    public void fireAllEvents()
    {
        synchronized (this.events)
        {
            this.events.values().forEach(e -> e.fire());
        }
    }

    public QualifiedParameter getQualifiedParameter(Name name)
    {
        QualifiedParameter ret = null;

        synchronized (this.parameters)
        {
            ret = this.parameters.get(name).toQualifiedParameter();
        }

        return ret;
    }

    public void updateIntegerParameter(Name name, long value)
    {
        synchronized (this.parameters)
        {
            this.parameters.get(name).setValue(value);
        }
    }

}
