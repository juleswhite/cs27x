package org.cs27x.dropbox.test;

import static org.junit.Assert.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.Files;

import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileState;
import org.cs27x.filewatcher.FileStates;
import org.junit.Test;


import static org.cs27x.dropbox.test.TestData.*;

public class FileStatesTest {

	@Test
	public void testInsert() throws Exception {
		FileStates states = new FileStates();
		states.insert(TEST_FILE);
		
		FileState state = states.getState(TEST_FILE);
		assertEquals(Files.size(TEST_FILE),state.getSize());
		assertEquals(Files.getLastModifiedTime(TEST_FILE),state.getLastModificationDate());
	}
	
	@Test
	public void testFilter() throws Exception {
		FileStates states = new FileStates();
		states.insert(TEST_FILE);
		
		FileEvent evt = new FileEvent(ENTRY_CREATE, TEST_FILE);
		assertNotNull(states.getState(evt.getFile()));
		
		evt = states.filter(evt);
		assertNull(evt);
		
		evt = new FileEvent(ENTRY_CREATE, TEST_FILE2);
		evt = states.filter(evt);
		assertNotNull(evt);
		
		evt = new FileEvent(ENTRY_CREATE, TEST_FILE);
		FileState st = states.getState(evt.getFile());
		st.setSize(st.getSize() - 1);
		evt = states.filter(evt);
		assertNotNull(evt);
		
		evt = new FileEvent(ENTRY_MODIFY, TEST_FILE);
		evt = states.filter(evt);
		assertNotNull(evt);
		
		st.setSize(st.getSize() + 1);
		evt = new FileEvent(ENTRY_MODIFY, TEST_FILE);
		evt = states.filter(evt);
		assertNull(evt);
		
		evt = new FileEvent(ENTRY_DELETE, TEST_FILE);
		evt = states.filter(evt);
		assertNotNull(evt);
		
		st.setSize(-1);
		evt = new FileEvent(ENTRY_DELETE, TEST_FILE);
		evt = states.filter(evt);
		assertNull(evt);
	}

}
