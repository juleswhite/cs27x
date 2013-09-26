package org.cs27x.dropbox;

/**
 * 
 * A DropboxTransport is a communication mechanism
 * for sending/receiving DropboxCmds to/from other
 * clients.
 * 
 * @author jules
 *
 */
public interface DropboxTransport {

	public void connect(String host);
	
	public void disconnect();
	
	public boolean isConnected();
	
	public void publish(DropboxCmd cmd);
	
	public void addCmdListener(DropboxCmdListener hdlr);
	
	public void removeCmdListener(DropboxCmdListener hdlr);
	
	public void addTransportListener(DropboxTransportListener l);
	
	public void removeTransportListener(DropboxTransportListener l);
	
}
