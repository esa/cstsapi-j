package esa.egos.csts.api.events;

import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public class EventData {
	
	public EventValue value;
	
	public Name name;
	
	public Time time;
	
	public static EventData of(EventValue value, Name name, Time time) {
		EventData eventData = new EventData();
		eventData.value = value;
		eventData.name = name;
		eventData.time = time;
		return eventData;
	}

}
