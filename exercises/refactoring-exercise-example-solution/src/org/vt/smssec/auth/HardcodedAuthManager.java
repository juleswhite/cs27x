package org.vt.smssec.auth;

import org.vt.smssec.cmd.CmdInfo;

public class HardcodedAuthManager implements AuthManager {

	@Override
	public boolean canExecute(String originator, CmdInfo info) {
		return originator != null
				&& (originator.equals("5555555555") || originator
						.equals("5555555556"));
	}

}
