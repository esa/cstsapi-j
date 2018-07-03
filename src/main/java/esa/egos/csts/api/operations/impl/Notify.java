package esa.egos.csts.api.operations.impl;

import ccsds.csts.common.operations.pdus.NotifyInvocation;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.types.Time;
import esa.egos.csts.api.util.impl.CSTSUtils;

public class Notify extends AbstractOperation implements INotify {

	private final OperationType type = OperationType.NOTIFY;
	
	private final static int versionNumber = 1;

	private Time eventTime;
	private Name eventName;
	private EventValue eventValue;

	public Notify() {
		super(versionNumber, false);
	}
	
	@Override
	public OperationType getType() {
		return type;
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
		return "Notify [eventTime=" + eventTime + ", eventName=" + eventName + ", eventValue=" + eventValue + "]";
	}

	@Override
	public NotifyInvocation encodeNotifyInvocation() {
		return encodeNotifyInvocation(CSTSUtils.nonUsedExtension());
	}

	@Override
	public NotifyInvocation encodeNotifyInvocation(Extended extension) {
		NotifyInvocation notifyInvocation = new NotifyInvocation();
		notifyInvocation.setStandardInvocationHeader(encodeStandardInvocationHeader());
		notifyInvocation.setEventTime(eventTime.encode());
		notifyInvocation.setEventName(eventName.encode());
		notifyInvocation.setEventValue(eventValue.encode());
		notifyInvocation.setNotifyInvocationExtension(extension);
		return notifyInvocation;
	}
	
	@Override
	public void decodeNotifyInvocation(NotifyInvocation notifyInvocation) {
		decodeStandardInvocationHeader(notifyInvocation.getStandardInvocationHeader());
		eventTime = Time.decode(notifyInvocation.getEventTime());
		eventName = Name.decode(notifyInvocation.getEventName());
		eventValue = EventValue.decode(notifyInvocation.getEventValue());
	}

}
