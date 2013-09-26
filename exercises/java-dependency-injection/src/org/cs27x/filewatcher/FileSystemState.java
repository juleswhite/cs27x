package org.cs27x.filewatcher;


/**
 * A FileSystemState is responsible for holding what the Dropbox application
 * believes should be the correct state of the shared file system. The FileSystemState
 * holds what it believes *will* be the correct state when all DropboxCmds have
 * finished processing. The FileSystemState is updated by DropboxCmds before the
 * cmds are actually executed and affect the actual file system. 
 * 
 * The actual file system state on the local host may differ from the FileSystemState's
 * view of the shared file system. A key responsibility of the FileSystemState is to
 * identify which local FileEvents represent changes that are bringing the local file
 * system in-line with the share file system state (e.g., a remote change that was
 * enacted on the local file system) and which events represent true changes external
 * to the application that are altering the local file system (e.g., a user saves a
 * file to a directory and it needs to be synch'd into the shared file system state).
 * For example, the FileSystemState will receive a file add command before the
 * command is actually executed and the new file added to the file system. A
 * FileEventSource may generate a new FileEvent as a result of the added file.
 * It is the FileSystemState's responsibility to determine that the event represents
 * a change that is due to a file system modification in response to a DropboxCmd's
 * execution.
 * 
 * @author jules
 *
 */
public interface FileSystemState {

	/**
	 * This method determines if the FileChangeEvent describes a change in the shared
	 * file system state that the FileSystemState is not already aware of. If it is
	 * not aware of the change, it updates its knowledge of the shared file system
	 * state and then returns true indicating that the event should be propagated. If
	 * the change is already known, the method returns false indicating that the event
	 * should not be propagated.
	 * 
	 * 
	 * @param evt
	 * @return
	 */
	public boolean updateState(FileChangeEvent cmd);
	
}
