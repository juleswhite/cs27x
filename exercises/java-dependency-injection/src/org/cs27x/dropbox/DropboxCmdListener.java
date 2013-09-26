package org.cs27x.dropbox;

/**
 * The DropboxCmdListener interface should be implemented
 * by classes that are going to handle incoming DropboxCmds
 * from remote hosts.
 * 
 * @author jules
 *
 */
public interface DropboxCmdListener {

	public void handleCmd(DropboxCmd cmd);
	
}
