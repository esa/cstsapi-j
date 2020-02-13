package esa.egos.csts.api.operations;

import b1.ccsds.csts.common.operations.pdus.NotifyInvocation;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

/**
 * This interface represents a NOTIFY operation.
 */
public interface INotify extends IOperation {

	/**
	 * Returns the event time.
	 * 
	 * @return the event time
	 */
	Time getEventTime();

	/**
	 * Sets the event time.
	 * 
	 * @param eventTime the event time
	 */
	void setEventTime(Time eventTime);

	/**
	 * Returns the event name.
	 * 
	 * @return the event name
	 */
	Name getEventName();

	/**
	 * Sets the event time.
	 * 
	 * @param eventTime the event time
	 */
	void setEventName(Name eventName);

	/**
	 * Returns the event value.
	 * 
	 * @return the event value
	 */
	EventValue getEventValue();

	/**
	 * Sets the event time.
	 * 
	 * @param eventTime the event time
	 */
	void setEventValue(EventValue eventValue);

	/**
	 * Returns the invocation extension.
	 * 
	 * @return the invocation extension
	 */
	Extension getInvocationExtension();

	/**
	 * Sets the invocation extension.
	 * 
	 * @param embedded the embedded data to extend this operation
	 */
	void setInvocationExtension(EmbeddedData embedded);

	/**
	 * Encodes this operation into a CCSDS NotifyInvocation.
	 * 
	 * @return this operation encoded into a CCSDS NotifyInvocation
	 */
	NotifyInvocation encodeNotifyInvocation();

	/**
	 * Decodes a specified CCSDS NotifyInvocation into this operation.
	 * 
	 * @param notifyInvocation the specified CCSDS NotifyInvocation
	 */
	void decodeNotifyInvocation(NotifyInvocation notifyInvocation);

}
