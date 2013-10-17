package org.vt.smssec.cmd;

public interface CmdFormat {

	public boolean isCmd(String rawsms);
	
	public CmdInfo parse(String rawsms);
	
}
