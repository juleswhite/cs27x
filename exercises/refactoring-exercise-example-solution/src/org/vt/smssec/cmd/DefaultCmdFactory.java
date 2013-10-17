package org.vt.smssec.cmd;


public class DefaultCmdFactory extends CmdFactory{

	public DefaultCmdFactory() {
		super();
		
		setCmdCreatorForType(PhotoCmd.TYPE, new PhotoCmdCreator());
		setCmdCreatorForType(TalkCmd.TYPE, new TalkCmdCreator());
		setCmdCreatorForType(TauntCmd.TYPE, new TauntCmdCreator());
	}
	
}
