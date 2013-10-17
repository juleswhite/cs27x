package org.vt.smssec.cmd;

public class CmdInfo {

	private final String cmdData_;

	private final String cmdType_;

	public CmdInfo(String cmdData, String cmdType) {
		super();
		cmdData_ = cmdData;
		cmdType_ = cmdType;
	}

	public String getCmdData() {
		return cmdData_;
	}

	public String getCmdType() {
		return cmdType_;
	}

}
