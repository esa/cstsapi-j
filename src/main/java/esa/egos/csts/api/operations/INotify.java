package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.NotifyInvocation;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

public interface INotify extends IOperation {

	EventValue getEventValue();

	void setEventValue(EventValue eventValue);

	Name getEventName();

	void setEventName(Name eventName);

	Time getEventTime();

	void setEventTime(Time eventTime);

	NotifyInvocation encodeNotifyInvocation();

	void decodeNotifyInvocation(NotifyInvocation notifyInvocation);

	void setInvocationExtension(EmbeddedData embedded);

	Extension getInvocationExtension();

}
