package esa.egos.csts.api.operations.impl;

import ccsds.csts.common.operations.pdus.NotifyInvocation;
import ccsds.csts.common.types.Extended;
import esa.egos.csts.api.exception.ApiException;
import esa.egos.csts.api.operations.AbstractOperation;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.parameters.impl.EventValue;
import esa.egos.csts.api.types.IName;
import esa.egos.csts.api.types.impl.Name;
import esa.egos.csts.api.types.impl.Time;
import esa.egos.csts.api.util.impl.ExtensionUtils;

public class Notify extends AbstractOperation implements INotify {

	private final static int versionNumber = 1;

	private Time eventTime;
	private IName eventName;
	private EventValue eventValue;

	public Notify() {
		super(versionNumber, false);
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
	public IName getEventName() {
		return eventName;
	}

	@Override
	public void setEventName(IName eventName) {
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
		return encodeNotifyInvocation(ExtensionUtils.nonUsedExtension());
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
