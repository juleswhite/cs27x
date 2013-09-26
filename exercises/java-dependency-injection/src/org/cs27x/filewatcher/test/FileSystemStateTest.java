package org.cs27x.filewatcher.test;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileSystemStateImpl;
import org.cs27x.filewatcher.FileSystemState;
import org.junit.Before;
import org.junit.Test;

public class FileSystemStateTest {

	private static final String TEST_FILE = "foo";
	private FileSystemState state_;
	
	@Before
	public void setUp(){
		state_ = new FileSystemStateImpl();
	}
	
	@Test
	public void testRemoteCmdCreateIngoreWhenInSync() {
		
		byte[] fileData = new byte[1024];
		
	    FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, Paths.get(TEST_FILE), fileData);
		
		assertTrue(state_.updateState(evt));
		
		DropboxCmd cmd = new DropboxCmd(OpCode.ADD, TEST_FILE, fileData);
		
		assertFalse(state_.updateState(cmd));
	}
	
	@Test
	public void testRemoteCmdRemoveAcceptWhenNotInSync() {
		
		byte[] fileData = new byte[1024];
		FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, Paths.get(TEST_FILE), fileData);
		
		assertTrue(state_.updateState(evt));
		
		DropboxCmd cmd = new DropboxCmd(OpCode.REMOVE, TEST_FILE);
		assertTrue(state_.updateState(cmd));
		assertFalse(state_.updateState(cmd));
	}
	
	@Test
	public void testRemoteCmdRemoveAcceptWithUnknownFile() {

		DropboxCmd cmd = new DropboxCmd(OpCode.REMOVE, TEST_FILE);
		assertTrue(state_.updateState(cmd));
		assertFalse(state_.updateState(cmd));
	}
	
	@Test
	public void testRemoteCmdUpdateAcceptWithUnknownFile() {

		DropboxCmd cmd = new DropboxCmd(OpCode.UPDATE, TEST_FILE);
		assertTrue(state_.updateState(cmd));
		assertFalse(state_.updateState(cmd));
	}
	
	@Test
	public void testRemoteCmdCreateAcceptWhenNotInSync() {
		
		byte[] fileData = new byte[1024];
		
		FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, Paths.get(TEST_FILE), fileData);
		
		assertTrue(state_.updateState(evt));
		
		DropboxCmd cmd = new DropboxCmd(OpCode.ADD, TEST_FILE, new byte[1023]);
		assertTrue(state_.updateState(cmd));
		
		fileData[fileData.length-1] = 1;
		DropboxCmd cmd2 = new DropboxCmd(OpCode.ADD, TEST_FILE, fileData);
		assertTrue(state_.updateState(cmd2));
		
		fileData[fileData.length-1] = 0;
		DropboxCmd cmd3 = new DropboxCmd(OpCode.ADD, TEST_FILE, fileData);
		assertTrue(state_.updateState(cmd3));
		assertFalse(state_.updateState(cmd3));
		assertFalse(state_.updateState(cmd3));
	}
	
	@Test
	public void testRemoteCmdUpdateAcceptWhenNotInSync() {
		
		byte[] fileData = new byte[1024];
		
		FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, Paths.get(TEST_FILE), fileData);
		
		assertTrue(state_.updateState(evt));
		
		DropboxCmd cmd = new DropboxCmd(OpCode.UPDATE, TEST_FILE, new byte[1023]);
		assertTrue(state_.updateState(cmd));
		
		fileData[fileData.length-1] = 1;
		DropboxCmd cmd2 = new DropboxCmd(OpCode.UPDATE, TEST_FILE, fileData);
		assertTrue(state_.updateState(cmd2));
		
		fileData[fileData.length-1] = 0;
		DropboxCmd cmd3 = new DropboxCmd(OpCode.UPDATE, TEST_FILE, fileData);
		assertTrue(state_.updateState(cmd3));
		assertFalse(state_.updateState(cmd3));
		assertFalse(state_.updateState(cmd3));
	}
	
	
	@Test
	public void testLocalEventIgnoreWhenInSync() {
		
		final byte[] fileData = new byte[1024];
		
		final FileEvent evt = new FileEvent(StandardWatchEventKinds.ENTRY_CREATE, Paths.get(TEST_FILE), fileData);
		
		assertTrue(state_.updateState(evt));
		
		DropboxCmd cmd = new DropboxCmd(OpCode.UPDATE, TEST_FILE, new byte[1023]);
		assertTrue(state_.updateState(cmd));
		
		FileEvent evt2 = new FileEvent(StandardWatchEventKinds.ENTRY_MODIFY, Paths.get(TEST_FILE), new byte[1023]);
		assertFalse(state_.updateState(evt2));
		
		fileData[fileData.length-1] = 1;
		DropboxCmd cmd2 = new DropboxCmd(OpCode.UPDATE, TEST_FILE, fileData);
		assertTrue(state_.updateState(cmd2));
		
		FileEvent evt3 = new FileEvent(StandardWatchEventKinds.ENTRY_MODIFY, Paths.get(TEST_FILE), fileData);
		assertFalse(state_.updateState(evt3));
		
		fileData[fileData.length-1] = 0;
		DropboxCmd cmd3 = new DropboxCmd(OpCode.UPDATE, TEST_FILE, fileData);
		assertTrue(state_.updateState(cmd3));
		assertFalse(state_.updateState(cmd3));
		assertFalse(state_.updateState(cmd3));
		
		FileEvent evt4 = new FileEvent(StandardWatchEventKinds.ENTRY_MODIFY, Paths.get(TEST_FILE), fileData);
		assertFalse(state_.updateState(evt4));
	}


}
