package org.vt.smssec.auth;

import org.vt.smssec.cmd.CmdInfo;

/**
 * This is the interface for classes that determines whether or
 * not a command received in an SMS can be executed by the device.
 * Implementers can use the originating phone number and/or contents
 * of the command to determine if it can be executed.
 * 
 * @author jules
 *
 */
public interface AuthManager {

	public boolean canExecute(String originator, CmdInfo info);
	
}
