/*************************************************************************
 * Copyright 2010 Jules White
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/
 * LICENSE-2.0 Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions
 * and limitations under the License.
 **************************************************************************/

package org.vt.smssec;

import java.util.ArrayList;
import java.util.List;

import org.vt.smssec.auth.AuthManager;
import org.vt.smssec.auth.HardcodedAuthManager;
import org.vt.smssec.cmd.CmdFactory;
import org.vt.smssec.cmd.CmdFormat;
import org.vt.smssec.cmd.CmdInfo;
import org.vt.smssec.cmd.Cmd;
import org.vt.smssec.cmd.DefaultCmdFactory;
import org.vt.smssec.cmd.PrefixedCmdFormat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private static final String ENABLE_SMS_SEC = "EnableSMSSec";
	private static final String CMD_PREFIX = "cmd:";
	private static final String MESSAGES = "pdus";

	private CmdFormat cmdFormat_ = new PrefixedCmdFormat(CMD_PREFIX);
	private CmdFactory cmdFactory_ = new DefaultCmdFactory();
	private AuthManager authManager_ = new HardcodedAuthManager();

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e("SMSSec", "Recv'd msg");

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		boolean enabled = prefs.getBoolean(ENABLE_SMS_SEC, true);

		if (!enabled) {
			Log.e("SMSSec", "Not enabled");
			return;
		}

		// make sure the sms app doesn't get the message
		// and create a notification that the thief might
		// see
		Log.e("SMSSec", "Aborting broadcast...");
		this.abortBroadcast();

		// get the data associated with the intent
		Bundle bundle = intent.getExtras();

		// extract the list of sms messages from the data
		Object messages[] = (Object[]) bundle.get(MESSAGES);
		
		List<Cmd> cmds = new ArrayList<Cmd>();

		// iterate through the sms messages and look for any
		// commands that need to be executed
		for (int i = 0; i < messages.length; i++) {
			SmsMessage msg = SmsMessage.createFromPdu((byte[]) messages[i]);
			String rawsms = msg.getDisplayMessageBody();

			if (cmdFormat_.isCmd(rawsms)) {
				CmdInfo info = cmdFormat_.parse(rawsms);
				if (info != null
						&& authManager_.canExecute(msg.getOriginatingAddress(),
								info)) {
					Cmd cmd = cmdFactory_.create(context, info);
					if(cmd != null){cmds.add(cmd);};
				}
			}
		}

		// if we didn't find any commands, we need
		// to cancel the abort so that the sms app
		// receives the message and the user (thief)
		// doesn't know that we are silently monitoring
		// sms messages
		if (cmds.size() == 0) {
			this.clearAbortBroadcast();
		} else {
			for(Cmd cmd : cmds){
				cmd.execute();
			}
		}

	}

}