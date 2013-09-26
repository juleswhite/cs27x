package org.cs27x.dropbox;

public interface DropboxTransportListener {

	public void connected(DropboxTransport t);
	
	public void disconnected(DropboxTransport t);
}
