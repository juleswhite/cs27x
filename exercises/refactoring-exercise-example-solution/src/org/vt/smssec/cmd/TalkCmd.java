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

package org.vt.smssec.cmd;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TalkCmd implements OnInitListener, Cmd{

	public static final String TYPE = "say";
	
	/**
	 * Example: cmd:say: "Your name has been sent to the police, you will rot in jail"
	 */
	
	private TextToSpeech textToSpeech_;
	private boolean ready_ = false;
	private boolean pendingExec_ = false;
	private String message_;
	
	public TalkCmd(Context ctx, String msg) {
		message_ = msg;
		
		textToSpeech_ = new TextToSpeech(ctx,this);
	}

	@Override
	public void onInit(int arg0) {
		ready_ = true;
		if(pendingExec_){
			talk();
			pendingExec_ = false;
		}
	}

	public void execute() {
		if(ready_){
			talk();
		}
		else {
			pendingExec_ = true;
		}
	}
	
	public void talk(){
		HashMap<String, String> myHashAlarm = new HashMap<String,String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
		        String.valueOf(AudioManager.STREAM_ALARM));
		textToSpeech_.speak(message_, TextToSpeech.QUEUE_ADD, myHashAlarm);
		
		try{
			Thread.sleep(3000);
		}catch (Exception e) {
		}
	}

}
