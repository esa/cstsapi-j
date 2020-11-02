package esa.egos.csts.api.events;

import java.util.ArrayList;
import java.util.List;

import com.beanit.jasn1.ber.types.BerNull;

import b1.ccsds.csts.common.types.SequenceOfQualifiedValues;
import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.events.impl.FunctionalResourceEvent;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceMetadata;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.types.Name;

/**
 * This class represents the CCSDS EventValue type.
 */
public class EventValue {

	private final EventValueType type;
	private final List<QualifiedValues> qualifiedValues;
	private EmbeddedData embeddedData;

	/**
	 * Instantiates a new Event Value object with the specified Event Value type.
	 * 
	 * @param type
	 *            the specified Event Value type
	 */
	public EventValue(EventValueType type) {
		this.type = type;
		if (this.type == EventValueType.QUALIFIED_VALUES) {
			qualifiedValues = new ArrayList<>();
		} else {
			qualifiedValues = null;
		}
	}

	/**
	 * Creates a new empty Event Value.
	 * 
	 * @return a new empty Event Value
	 */
	public static EventValue empty() {
		return new EventValue(EventValueType.EMPTY);
	}

	/**
	 * Returns the selected Event Value type.
	 * 
	 * @return the select Event Value type
	 */
	public EventValueType getType() {
		return type;
	}

	/**
	 * Returns the list of Qualified Values.
	 * 
	 * @return the list of Qualified Values
	 */
	public List<QualifiedValues> getQualifiedValues() {
		return qualifiedValues;
	}

	/**
	 * Returns the Extension.
	 * 
	 * @return the Extension
	 */
	public EmbeddedData getExtension() {
		return embeddedData;
	}

	/**
	 * Sets the Extension with the specified Embedded Data.
	 * 
	 * @param extension
	 *            the specified Embedded Data
	 */
	public void setExtension(EmbeddedData extension) {
		this.embeddedData = extension;
	}

	/**
	 * Encodes this EventValue into a CCSDS EventValue type.
	 * 
	 * @return the CCSDS EventValue type representing this object
	 */
	public b1.ccsds.csts.common.types.EventValue encode() {
		b1.ccsds.csts.common.types.EventValue eventValue = new b1.ccsds.csts.common.types.EventValue();
		switch (type) {
		case QUALIFIED_VALUES:
			SequenceOfQualifiedValues qualifiedValues = new SequenceOfQualifiedValues();
			for (QualifiedValues qualifiedValue : this.qualifiedValues) {
				qualifiedValues.getQualifiedValues().add(qualifiedValue.encode());
			}
			eventValue.setQualifiedValues(qualifiedValues);
			break;
		case EMPTY:
			eventValue.setEmpty(new BerNull());
			break;
		case EXTENDED:
			eventValue.setEventValueExtension(embeddedData.encode());
			break;
		}
		return eventValue;
	}

	/**
	 * Decodes a specified EventValue CCSDS type.
	 * 
	 * @param value
	 *            the specified CCSDS EventValue type
	 * @return a new EventValue decoded from the specified CCSDS EventValue type
	 */
	public static EventValue decode(b1.ccsds.csts.common.types.EventValue value) {

		EventValue eventValue = null;

		if (value.getEmpty() != null) {
			eventValue = new EventValue(EventValueType.EMPTY);
		}

		if (value.getQualifiedValues() != null) {
			if (value.getQualifiedValues().getQualifiedValues() != null) {
				eventValue = new EventValue(EventValueType.QUALIFIED_VALUES);
				for (b1.ccsds.csts.common.types.QualifiedValues qualifiedValues : value.getQualifiedValues().getQualifiedValues()) {
					eventValue.getQualifiedValues().add(QualifiedValues.decode(qualifiedValues));
				}
			}
		}

		if (value.getEventValueExtension() != null) {
			eventValue = new EventValue(EventValueType.EXTENDED);
			eventValue.setExtension(EmbeddedData.decode(value.getEventValueExtension()));
		}

		return eventValue;
	}

	@Override
	public String toString()
	{
	    String ret = "EventValue [type="+this.type.toString()+", qualifiedValues=[";
	    if (this.qualifiedValues != null)
	    {
            ret += this.qualifiedValues.toString();
	    }
	    ret += "]]";
	    return ret;
	}
	
	public String toFormattedString(Name name)
	{
        StringBuilder sb = new StringBuilder("\n");
		switch (type) {
		case QUALIFIED_VALUES:
            sb.append("Number of QualifiedValues: ")
                    .append(this.qualifiedValues.size()).append('\n');
			for (QualifiedValues qualifiedValue : this.qualifiedValues) {
			    for (ParameterValue parameterValue : qualifiedValue.getParameterValues())
			    {
                    sb.append(parameterValue.toString());
			    }
			}
			break;
		case EMPTY:
			break;
		case EXTENDED:
		    try
		    {
		        FunctionalResourceEvent<?, FunctionalResourceValue<?>> fre = FunctionalResourceMetadata.getInstance().createEvent(name);
		        sb.append(fre.valueToString());
		    }
		    catch(Exception e)
		    {
                e.printStackTrace();
		    }
			break;
		}
        return sb.toString();
	}
	
}
