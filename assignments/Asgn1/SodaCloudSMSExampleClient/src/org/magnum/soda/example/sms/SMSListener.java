/* 
**
** Copyright 2013, Jules White
**
** 
*/
package org.magnum.soda.example.sms;

import org.magnum.soda.proxy.SodaAsync;

public interface SMSListener {

        @SodaAsync	
	public void smsEvent(SMSEvent evt);
	
	@SodaAsync
	public void smsSenderAdded(SMSSender sender);
	
}
