package org.vt.smssec.cmd;

import android.content.Context;

public class TalkCmdCreator implements CmdCreator {

	@Override
	public Cmd create(Context ctx, String cmddata) {
		return new TalkCmd(ctx, cmddata);
	}

}
