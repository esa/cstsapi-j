package esa.egos.csts.api.operations.impl.b1;

import com.beanit.jasn1.ber.types.BerType;

import b1.ccsds.csts.common.operations.pdus.NotifyInvocation;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.extensions.Extension;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;

/**
 * This class represents a NOTIFY operation.
 */
public class Notify extends AbstractOperation implements INotify {

	private static final OperationType TYPE = OperationType.NOTIFY;

	/**
	 * The invocation extension
	 */
	private Extension invocationExtension;

	/**
	 * The time of an event
	 */
	private Time eventTime;

	/**
	 * The name of an event
	 */
	private Name eventName;

	/**
	 * The value of an event
	 */
	private EventValue eventValue;

	/**
	 * The constructor of a NOTIFY operation
	 */
	public Notify() {
		invocationExtension = Extension.notUsed();
	}

	@Override
	public OperationType getType() {
		return TYPE;
	}

	@Override
	public Time getEventTime() {
		return eventTime;
	}

	@Override
	public void setEventTime(Time eventTime) {
		this.eventTime = eventTime;
	}

	@Override
	public Name getEventName() {
		return eventName;
	}

	@Override
	public void setEventName(Name eventName) {
		this.eventName = eventName;
	}

	@Override
	public EventValue getEventValue() {
		return eventValue;
	}

	@Override
	public void setEventValue(EventValue eventValue) {
		this.eventValue = eventValue;
	}

	@Override
	public Extension getInvocationExtension() {
		return invocationExtension;
	}

	@Override
	public void setInvocationExtension(EmbeddedData embedded) {
		invocationExtension = Extension.of(embedded);
	}

	@Override
	public void verifyInvocationArguments() throws ApiException {
		if (eventTime == null) {
			throw new ApiException("Invalid NOTIFY invocation arguments.");
		}
		if (eventName == null) {
			throw new ApiException("Invalid NOTIFY invocation arguments.");
		}
		if (eventValue == null) {
			throw new ApiException("Invalid NOTIFY invocation arguments.");
		}
	}

	@Override
	public String print(int i) {
		StringBuilder sb = new StringBuilder(i);
		sb.append("\nOperation                      : NOTIFY\n");
		sb.append(super.print(i));
		sb.append("Confirmed Operation            : true\n");
		sb.append("Diagnostic Type                : no diagnostic\n");
		sb.append("Common Diagnostics             : Invalid\n");
		sb.append("Event time                     : ").append(this.eventTime.toInstant()).append('\n');
		sb.append("Event name                     : ").append(this.eventName.toString()).append('\n');
		sb.append("Event type                     : ").append(this.eventValue.getType()).append('\n');
		sb.append("Event value                    : ").append(this.eventValue.toFormattedString(this.eventName)).append('\n');

		return sb.toString();
	}
	
	@Override
	public NotifyInvocation encodeNotifyInvocation() {
		NotifyInvocation notifyInvocation = new NotifyInvocation();
		notifyInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		notifyInvocation.setEventTime(eventTime.encode(new b1.ccsds.csts.common.types.Time()));
		notifyInvocation.setEventName(eventName.encode(new b1.ccsds.csts.common.types.Name()));
		notifyInvocation.setEventValue(eventValue.encode(new b1.ccsds.csts.common.types.EventValue()));
		notifyInvocation.setNotifyInvocationExtension(invocationExtension.encode(
				new b1.ccsds.csts.common.types.Extended()));
		return notifyInvocation;
	}

	@Override
	public void decodeNotifyInvocation(BerType notifyInvocation) {
		NotifyInvocation notifyInvocation1 = (NotifyInvocation)notifyInvocation;
		decodeStandardInvocationHeader(notifyInvocation1.getStandardInvocationHeader());
		eventTime = Time.decode(notifyInvocation1.getEventTime());
		eventName = Name.decode(notifyInvocation1.getEventName());
		eventValue = EventValue.decode(notifyInvocation1.getEventValue());
		invocationExtension = Extension.decode(notifyInvocation1.getNotifyInvocationExtension());
	}

	@Override
	public String toString() {
		return "Notify [invocationExtension=" + invocationExtension + ", eventTime=" + eventTime + ", eventName="
				+ eventName + ", eventValue=" + eventValue + "]";
	}

}
