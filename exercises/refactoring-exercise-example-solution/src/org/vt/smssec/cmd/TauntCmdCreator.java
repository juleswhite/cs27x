package org.vt.smssec.cmd;

import android.content.Context;
import android.widget.Toast;

public class TauntCmdCreator implements CmdCreator {

	@Override
	public Cmd create(Context ctx, String cmddata) {
		
		String msg = getText(cmddata);
		
		return new TauntCmd(ctx, msg, Toast.LENGTH_LONG);
	}
	
	private String getText(String cmddata){
		int start = cmddata.indexOf("\"");
		int end = cmddata.indexOf("\"", start+1);
		return (start > 0 && end > start)? cmddata.substring(start,end) : null;
	}

}
