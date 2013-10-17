package org.vt.smssec.cmd;

import android.content.Context;

public interface CmdCreator {

	public Cmd create(Context ctx, String cmddata);
	
}
