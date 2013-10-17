package org.vt.smssec.cmd;

import android.util.Log;

public class PrefixedCmdFormat implements CmdFormat {

	private final String prefix_;
	
	public PrefixedCmdFormat(String prefix){
		prefix_ = prefix;
	}
	
	@Override
	public CmdInfo parse(String rawsms) {
		String alldata = rawsms.substring(prefix_.length());
		
		Log.d("SMSSec", "Rawsms: "+rawsms);
		
		int split = alldata.indexOf(" ");
		
		Log.d("SMSSec", "Split: "+split);
		
		CmdInfo info = null;
		
		if(split > 0){
			String type = alldata.substring(0,split);
			String data = alldata.substring(split+1);
			
			Log.d("SMSSec", "Type: "+type);
			Log.d("SMSSec", "Data: "+data);
			
			info = new CmdInfo(data, type);
		}
		
		return info;
	}
	
	public boolean isCmd(String msg) {
		return ("" + msg).startsWith(prefix_);
	}

}
