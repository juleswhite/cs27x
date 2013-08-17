/* 
**
** Copyright 2013, Jules White
**
** 
*/
package org.magnum.soda.example.sms;

public interface SMSManager {

	public void addListener(SMSListener l);
	public void removeListener(SMSListener l);
	public void sendSMS(SMS sms);
	
}
