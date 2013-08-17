/* 
 **
 ** Copyright 2013, Jules White
 **
 ** 
 */
package org.magnum.soda.example.sms;

import org.magnum.soda.proxy.SodaByValue;

@SodaByValue
public class SMSEvent {

	public enum EVENT_TYPE {
		SEND, RECEIVE
	}

	private EVENT_TYPE eventType_;

	private SMS sms_;

	public SMSEvent() {
	}

	public SMSEvent(EVENT_TYPE eventType) {
		super();
		eventType_ = eventType;
	}

	public SMSEvent(EVENT_TYPE eventType, SMS sms) {
		super();
		eventType_ = eventType;
		sms_ = sms;
	}

	public EVENT_TYPE getEventType() {
		return eventType_;
	}

	public void setEventType(EVENT_TYPE eventType) {
		eventType_ = eventType;
	}

	public SMS getSms() {
		return sms_;
	}

	public void setSms(SMS sms) {
		sms_ = sms;
	}

}
