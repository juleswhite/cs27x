package org.vt.smssec.cmd;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class CmdFactory {

	private Map<String, CmdCreator> creatorsByType_ = new HashMap<String, CmdCreator>();

	public CmdFactory() {
		super();
	}

	public Cmd create(Context ctx, CmdInfo info) {
		assert(info != null);
		assert(ctx != null);

		Cmd rslt = null;
		String type = info.getCmdType();
		String cmddata = info.getCmdData();

		if (type != null) {
			CmdCreator creator = creatorsByType_.get(type);
			rslt = (creator != null) ? creator.create(ctx, cmddata) : null;
		}

		return rslt;
	}

	public CmdCreator getCmdCreatorForType(Object type) {
		return creatorsByType_.get(type);
	}

	public CmdCreator setCmdCreatorForType(String type, CmdCreator value) {
		return creatorsByType_.put(type, value);
	}

	public CmdCreator removeCmdCreatorForType(Object type) {
		return creatorsByType_.remove(type);
	}

	
}
