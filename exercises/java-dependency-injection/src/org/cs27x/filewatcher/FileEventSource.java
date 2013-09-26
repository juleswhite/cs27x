package org.cs27x.filewatcher;

import java.io.IOException;

/**
 * 
 * This is the interface for classes that can watch a file system
 * for changes and notify listeners of those changes. A FileEventSource
 * will create FileEvents and dispatch them to each added FileEventListener
 * when a file system change occurs. 
 * 
 * A FileEventSource should guarantee that a file system change generates
 * exactly one FileEvent. FileEventSource implementations should handle
 * exceptions thrown by listeners and ensure that any remaining listeners
 * still receive the FileEvent.
 * 
 * @author jules
 *
 */
public interface FileEventSource {

	/**
	 * Adds a listener to the FileEventSource. The FileEventListener
	 * will receive future FileEvents, but will not receive any events
	 * that are actively being dispatched.
	 * 
	 * @param listener
	 */
	public void addListener(FileEventListener listener);
	

	/**
	 * Removes a listener from the FileEventSource. The FileEventListener
	 * should immediately stop receiving events, including events that
	 * are in the process of being dispatched.
	 * 
	 * @param listener
	 */
	public void removeListener(FileEventListener listener);
	
	
	/**
	 * Start sending events.
	 */
	public void start() throws IOException;
	
	/**
	 * Stop sending events.
	 * @throws IOException
	 */
	public void stop();
	
}
