package org.vt.smssec.cmd;

import android.content.Context;

public class PhotoCmdCreator implements CmdCreator {

	@Override
	public Cmd create(Context ctx, String cmddata) {
		return new PhotoCmd(ctx);
	}

}
