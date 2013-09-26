package org.cs27x.filewatcher;

/**
 * A FileChangeEvent represents a change to
 * a file either locally or in the shared file
 * system.
 * 
 * @author jules
 *
 */
public interface FileChangeEvent {

	public String getPath();
	public byte[] getData();

}