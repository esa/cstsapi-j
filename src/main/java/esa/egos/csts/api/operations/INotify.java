package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.NotifyInvocation;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.parameters.impl.EventValue;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.Time;

public interface INotify extends IOperation{

	void setEventValue(EventValue eventValue);

	EventValue getEventValue();

	void setEventName(IName eventName);

	IName getEventName();

	void setEventTime(Time eventTime);

	Time getEventTime();

	void decodeNotifyInvocation(NotifyInvocation notifyInvocation);

	NotifyInvocation encodeNotifyInvocation();

	NotifyInvocation encodeNotifyInvocation(Extended extension);

}
