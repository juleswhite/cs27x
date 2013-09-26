package org.cs27x.filewatcher;

/**
 * This interface should be implemented by classes that would
 * like to receive FileEvents from a FileEventSource. A
 * FileEventSource will dispatch FileEvents to all of its registered
 * listeners each time that it determines a file system change
 * has occurred.
 * 
 * @author jules
 *
 */
public interface FileEventListener {

	public void handleEvent(FileEvent evt);
	
}
